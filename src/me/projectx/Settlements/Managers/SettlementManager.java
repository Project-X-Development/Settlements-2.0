package me.projectx.Settlements.Managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.projectx.Settlements.Models.ClaimedChunk;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.ClaimType;
//import me.projectx.Settlements.Scoreboard.NameBoard;
import me.projectx.Settlements.Utils.DatabaseUtils;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettlementManager extends Thread {

	private static ArrayList<Settlement> settlements = new ArrayList<Settlement>();
	private final HashMap<String, Settlement> invitedPlayers = new HashMap<String, Settlement>();
	private static SettlementManager sm = new SettlementManager();

	/**
	 * Get an instance of the SettlementManager class
	 * 
	 * @return SettlementManager class instance
	 */
	public static SettlementManager getManager(){
		return sm;
	}

	/**
	 * Get a list of all Settlment objects
	 * 
	 * @return Settlement class instance
	 */
	public static ArrayList<Settlement> getSettlements(){
		return settlements;
	}

	/**
	 * Load settlements (Call onEnable)
	 * 
	 * @return Returns true if successful, false otherwise
	 * @throws SQLException 
	 */
	public static void loadSettlmentsFromDB() throws SQLException{
		new Thread() {
			@Override
			public void run() {
				ResultSet result = null;
				try {
					result = DatabaseUtils.queryIn("SELECT * FROM settlements;");
					while (result.next()){
						String name = result.getString("name");
						Settlement set = new Settlement(name);
						set.setLeader(result.getString("leader"));
						set.setDescription(result.getString("description"));
						set.setOfficer(result.getString("officers"));
						set.giveCitizenship(result.getString("citizens"));
						settlements.add(set);
					}
				} catch(SQLException e) {e.printStackTrace();}
			}
		}.start();
	}

	/**
	 * Save settlements (Call onDisable)
	 * 
	 * @return Returns true if successful, false otherwise
	 * @throws SQLException 
	 */
	public static void saveSettlements() throws SQLException{
		new Thread() {
			@Override
			public void run() {
				for (Settlement set : settlements){
					String tempName = set.getName();
					long tempId = set.getId();
					String tempLeader = set.getLeader();
					String tempDesc = set.getDescription();
					ArrayList<String> tempCits = set.getCitizens();
					ArrayList<String> tempOffs = set.getOfficers();
					try {
						DatabaseUtils.queryOut("UPDATE settlements"
								+ "SET name='"+ tempName + "', leader='"+ tempLeader 
								+ "', description='"+ tempDesc +"', citizens='"+ tempCits 
								+ "', officers='"+ tempOffs +"'WHERE id='" + tempId + "';");
					} catch(SQLException e) {e.printStackTrace();}
				}
			}
		}.start();
	}

	/**
	 * Get the settlement of a player
	 * 
	 * @param name : The name of the player to get the settlement for
	 * @return The player's settlement. If they are not a member of a settlement, this will return null
	 */
	public Settlement getPlayerSettlement(String name){
		for (Settlement s : settlements){
			if (s.isCitizen(name) || s.isOfficer(name) || s.isLeader(name)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Determine if a settlement exists
	 * 
	 * @param name : The name of the settlement to check
	 * @return True if the settlement exists
	 */
	public boolean settlementExists(String name){
		for (Settlement s : settlements){
			if (s.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get a settlement by name
	 * 
	 * @param name : The name of the settlement to get
	 * @return The designated settlement. Returns null if it doesn't exist
	 */
	public Settlement getSettlement(String name){
		for (Settlement s : settlements){
			if (s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}	
		return null;
	}

	/**
	 * Get a settlement by id
	 * 
	 * @param id : The id of the settlement to get
	 * @return The designated settlement. Returns null if it doesn't exist
	 */
	public Settlement getSettlement(long id){
		for (Settlement s : settlements){
			if (s.getId() == id) {
				return s;
			}
		}	
		return null;
	}

	/**
	 * Create a new settlement. The settlement will only get created if the settlement 
	 * doesn't already exist and if the sender isn't a member of a different settlement
	 * 
	 * @param name : The name of the new settlement
	 * @param sender : Who issued the creation of the settlement
	 * @throws SQLException 
	 */
	public void createSettlement(String name, CommandSender sender) throws SQLException{
		if (!settlementExists(name)){
			if (getPlayerSettlement(sender.getName()) == null){
				final Settlement s = new Settlement(name);
				s.setLeader(sender.getName());
				new Thread() {
					@Override
					public void run() {
						settlements.add(s);
						try {
							DatabaseUtils.queryOut("INSERT INTO settlements (id, name, leader)"
									+ "VALUES ('" + s.getId() + "','" + s.getName() + "','" + s.getLeader() + "');");
						} catch(SQLException e) {e.printStackTrace();}
					}
				}.start();
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully created " + ChatColor.AQUA + s.getName());
				sender.sendMessage(ChatColor.GRAY + "You can now set a description by doing " + ChatColor.AQUA + "/s desc <description>");
				sender.sendMessage(ChatColor.GRAY + "For more things you can do, type " + ChatColor.AQUA + "/s");
			} else {
				sender.sendMessage(MessageType.CREATE_IN_SETTLEMENT.getMsg());
			}
		} else {
			sender.sendMessage(MessageType.SETTLEMENT_EXISTS.getMsg());
		}
	}

	/**
	 * Delete a settlement. Only works if the settlement exists and the sender is the leader of the settlement
	 * 
	 * @param name : The name of the settlement to delete
	 * @param sender : Who issued the deletion of the settlement
	 * @throws SQLException 
	 */
	public void deleteSettlement(final CommandSender sender) throws SQLException{
		if (settlementExists(getPlayerSettlement(sender.getName()).getName())){
			if (getPlayerSettlement(sender.getName()).isLeader(sender.getName())){
				new Thread() {
					@Override
					public void run() {
						try {
							Settlement s = getPlayerSettlement(sender.getName());
							DatabaseUtils.queryOut("DELETE FROM settlements WHERE id=" + s.getId() + ";");
							DatabaseUtils.queryOut("DELETE FROM chunks WHERE settlement=" + s.getId() + ";");

							List<ClaimedChunk> cc = ChunkManager.getInstance().map.get(s);
							if (cc != null){
								ClaimedChunk.instances.removeAll(cc);
								ChunkManager.getInstance().map.remove(s);
							}

							if (invitedPlayers.containsValue(s)) {
								invitedPlayers.remove(s);
							}

							settlements.remove(s);

							sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully deleted " + ChatColor.AQUA + s.getName());

						} catch(SQLException e) {e.printStackTrace();} catch (Throwable e) {
							e.printStackTrace();
						}	
					}
				}.start();
			} else {
				sender.sendMessage(MessageType.DELETE_NOT_LEADER.getMsg());
			}
		} else {
			sender.sendMessage(MessageType.SETTLEMENT_NOT_EXIST.getMsg());
		}
	}

	/**
	 * Invite a player to join the Settlement. The command sender must be an Officer or higher in the Settlement
	 * 
	 * @param player : The player to invite
	 * @param sender : Who issued the invite
	 */
	@SuppressWarnings("deprecation")
	public void inviteCitizen(String player, CommandSender sender){
		if (!invitedPlayers.containsKey(player)){
			if (!(getPlayerSettlement(sender.getName()) == null)){
				Settlement s = getPlayerSettlement(sender.getName());	
				if (!s.hasMember(player)){
					if (s.isOfficer(sender.getName()) || s.isLeader(sender.getName())){
						invitedPlayers.put(player, getPlayerSettlement(sender.getName()));		
						sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + 
								"Invited " + ChatColor.AQUA + player + ChatColor.GRAY + " to your Settlement");
						Bukkit.getPlayer(player).sendMessage(MessageType.PREFIX.getMsg() + 
								ChatColor.AQUA + sender.getName() + ChatColor.GRAY + " invited you to join " + 
								ChatColor.AQUA + getPlayerSettlement(sender.getName()).getName()); //throws NPE if player is null/not online
					} else {
						sender.sendMessage(MessageType.INVITE_NOT_RANK.getMsg());
					}
				} else {
					sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + player + ChatColor.GRAY + " is already in your Settlement!");
				}
			} else {
				sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
			}
		}
	}

	/**
	 * Accept an invite to a Settlement. Will only work if the player has a pending invite
	 * 
	 * @param player : The player who is accepting the invite
	 * @throws SQLException 
	 */
	@SuppressWarnings("deprecation")
	public void acceptInvite(final String player) throws SQLException{
		if (hasInvite(player)){
			if (getPlayerSettlement(player) == null){
				final Settlement s = invitedPlayers.get(player);
				s.giveCitizenship(player);
				new Thread() {
					@Override
					public void run() {
						invitedPlayers.remove(player);
						try {
							DatabaseUtils.queryOut("UPDATE settlements SET citizens='"+ s.getCitizens() +"' WHERE id='" + s.getId() + "';");
						} catch(SQLException e) {e.printStackTrace();}
					}
				}.start();
				Bukkit.getPlayer(player).sendMessage(MessageType.PREFIX.getMsg() + 
						ChatColor.GRAY + "Successfully joined " + ChatColor.AQUA + getPlayerSettlement(player).getName()); 
				s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + player + ChatColor.GRAY + " joined the Settlement!");	
			} else {
				Bukkit.getPlayer(player).sendMessage(MessageType.CURRENTLY_IN_SETTLEMENT.getMsg());
			}
		} else {
			Bukkit.getPlayer(player).sendMessage(MessageType.NO_INVITE.getMsg());
		}
	}

	/**
	 * Determine if a player has a pending invite
	 * 
	 * @param player : The player to check
	 * @return True if the player has an invite
	 */
	public boolean hasInvite(String player){
		return invitedPlayers.containsKey(player);
	}

	/**
	 * Decline an invite to a Settlement. Player must have a valid invite
	 * 
	 * @param player : The player who is declining the invite
	 */
	@SuppressWarnings("deprecation")
	public void declineInvite(String player){
		if (hasInvite(player)){
			Bukkit.getPlayer(player).sendMessage(MessageType.PREFIX.getMsg() + 
					ChatColor.GRAY + "You declined an invite to join " + ChatColor.AQUA + invitedPlayers.get(player).getName());
			invitedPlayers.remove(player);
		} else {
			Bukkit.getPlayer(player).sendMessage(MessageType.NO_INVITE.getMsg());
		}
	}

	/**
	 * Remove a player from a Settlement
	 * 
	 * @param name : The player to remove from the Settlement
	 * @throws SQLException 
	 */
	@SuppressWarnings("deprecation")
	public void leaveSettlement(String name) throws SQLException{
		if (!(getPlayerSettlement(name) == null)){
			final Settlement s = getPlayerSettlement(name);
			if (!s.isLeader(name)){
				Bukkit.getPlayer(name).sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully left " + ChatColor.AQUA + s.getName());
				s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + name + ChatColor.GRAY + " left the Settlement :(");
				s.revokeCitizenship(name);
				new Thread() {
					@Override
					public void run() {
						try {
							DatabaseUtils.queryOut("UPDATE settlements SET citizens='"+ s.getCitizens() +"' WHERE id=" + s.getId() + ";");
						} catch(SQLException e) {e.printStackTrace();}
					}
				}.start();	
			} else {
				Bukkit.getPlayer(name).sendMessage(MessageType.MUST_APPOINT_NEW_LEADER.getMsg());
			}
		} else {
			Bukkit.getPlayer(name).sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	@SuppressWarnings("deprecation")
	public void kickPlayer(CommandSender sender, String name){
		if (!(getPlayerSettlement(sender.getName()) == null)){
			final Settlement s = getPlayerSettlement(sender.getName());
			if (s.hasMember(name)){
				if (!s.isLeader(name)){
					s.revokeCitizenship(name);
					if (Bukkit.getPlayer(name).isOnline()){
						Bukkit.getPlayer(name).sendMessage(MessageType.PREFIX.getMsg() + 
								ChatColor.GRAY + "You have been kicked from " + ChatColor.AQUA + s.getName());
						s.sendSettlementMessage(MessageType.PREFIX.getMsg() + 
								ChatColor.AQUA + sender.getName() + ChatColor.GRAY + " kicked " + 
								ChatColor.AQUA + name + ChatColor.GRAY + " from the Settlement!");
						new Thread() {
							@Override
							public void run() {
								try {
									DatabaseUtils.queryOut("UPDATE settlements SET citizens='"+ s.getCitizens() +"' WHERE id=" + s.getId() + ";");
									DatabaseUtils.queryOut("UPDATE settlements SET officers='" + s.getOfficers() + "' WHERE id=" + s.getId() + ";");
								} catch(SQLException e) {e.printStackTrace();}
							}
						}.start();
					}
				} else {
					sender.sendMessage(MessageType.KICK_NOT_LEADER.getMsg());
				}
			} else {
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "That player is not in your Settlement!");
			}
		} else {
			sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	/**
	 * Set the description for a Settlement
	 * 
	 * @param sender : Who issued the command
	 * @param desc : The description for the Settlement
	 * @throws SQLException
	 */
	public void setDescription(CommandSender sender, final String desc) throws SQLException{
		if (!(getPlayerSettlement(sender.getName()) == null)){
			final Settlement s = getPlayerSettlement(sender.getName());
			if (s.isOfficer(sender.getName()) || s.isLeader(sender.getName())){
				s.setDescription(desc);
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Set your Settlement's description to " + desc);
				new Thread() {
					@Override
					public void run() {
						try {
							DatabaseUtils.queryOut("UPDATE settlements SET description='" + desc + "' WHERE id=" + s.getId() + ";");
						} catch(SQLException e) {e.printStackTrace();}
					}
				}.start();

			} else {
				sender.sendMessage(MessageType.DESCRIPTION_NOT_RANK.getMsg());
			}
		} else {
			sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	/**
	 * Set the tag for a Settlement
	 * 
	 * @param sender : Who issued the command
	 * @param tag : The tag for the Settlement
	 * @throws SQLException
	 */
	/*public void setTag(CommandSender sender, final String tag) throws SQLException{
		if (!(getPlayerSettlement(sender.getName()) == null)){
			if (tag.length() <= 4){
				final Settlement s = getPlayerSettlement(sender.getName());
				if (s.isOfficer(sender.getName()) || s.isLeader(sender.getName())){
					s.setTag(tag);
					sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Set your Settlement's tag to [" + tag + "]");
					new Thread() {
						@Override
						public void run() {
							try {
								DatabaseUtils.queryOut("UPDATE settlements SET tag='" + tag + "' WHERE id=" + s.getId() + ";");
							} catch(SQLException e) {e.printStackTrace();}
						}
					}.start();

				} else {
					sender.sendMessage(MessageType.DESCRIPTION_NOT_RANK.getMsg());
				}
			}
			else{
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "Your clan tag must be less than 5 characters.");
			}
		} else {
			sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}*/

	/**
	 * List all the members of a given Settlement
	 * 
	 * @param sender : Who sent the command and will receive the list
	 * @param settlement : The Settlement
	 */
	public void listMembers(CommandSender sender, Settlement settlement){
		if (settlementExists(settlement.getName())){
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "All members in the Settlement");
			sender.sendMessage(ChatColor.GRAY + "Size: " + ChatColor.AQUA + settlement.memberSize());
			sender.sendMessage(ChatColor.GREEN + "Leader:");
			sender.sendMessage(ChatColor.RED + "-" + settlement.getLeader());
			sender.sendMessage(ChatColor.GREEN + "Officers:");
			sender.sendMessage(ChatColor.RED + settlement.getOfficers().toString().replace("[", "").replace("]", " "));
			sender.sendMessage(ChatColor.GREEN + "Citizens:");
			sender.sendMessage(ChatColor.RED +  settlement.getCitizens().toString().replace("[", "").replace("]", " "));
		} else {
			sender.sendMessage(MessageType.SETTLEMENT_NOT_EXIST.getMsg());
		}
	}

	public void listSettlements(CommandSender sender){
		sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.WHITE + "Listing all current Settlements...");
		for (int i = 0; i < settlements.size(); i++){
			sender.sendMessage(ChatColor.GOLD + "" + (i + 1) + ". " + 
					ChatColor.AQUA + settlements.get(i).getName() + ChatColor.RED + " ~ " + ChatColor.GRAY + settlements.get(i).getDescription());
		}
	}

	/**
	 * Calculate the power for a Settlement
	 * 
	 * @param s : The settlement to calculate the power for
	 */
	public void calculatePower(Settlement s){
		//int citizens = s.getCitizens().size();
		//int officers = s.getOfficers().size();
		for (ClaimedChunk cc : ClaimedChunk.instances){
			if (cc.getSettlement().equals(s)){
				s.setPower( /*((citizens + officers + 1)/*/ChunkManager.getInstance().map.get(s).size()); //will readd members after more testing
			}
		} 
	}

	/**
	 * Claim a chunk at a given player's location
	 * 
	 * @param player : The player claiming the chunk
	 * @throws SQLException 
	 */
	public void claimChunk(Player player, ClaimType ct) throws SQLException{
		int status = ChunkManager.getInstance().claimChunk(player.getName(), 
				player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ(), player.getLocation().getWorld(), ct);
		if (status == 2) {
			player.sendMessage(MessageType.CHUNK_CLAIM_SUCCESS.getMsg());
			if (ct == ClaimType.SAFEZONE)
				player.sendMessage(MessageType.CHUNK_CLAIM_SAFEZONE.getMsg());
		} else if (status == 1) {
			player.sendMessage(MessageType.CHUNK_CLAIM_OWNED.getMsg());
		} else if (status == 0) {
			player.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}
}