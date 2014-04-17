package me.projectx.Settlements.API;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import me.projectx.Settlements.Utils.DatabaseUtils;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SettlementManager {

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


	//Database methods for saving and loading Settlements//

	/**
	 * Load settlements (Call onEnable)
	 * 
	 * @return Returns true if successful, false otherwise
	 * @throws SQLException 
	 */
	public static void loadSettlmentsFromDB() throws SQLException{
		ResultSet result = DatabaseUtils.queryIn("SELECT * FROM settlements;"); 
		while (result.next()){
			String name = result.getString("name");
			Settlement set = new Settlement(name);
			set.setLeader(result.getString("leader"));
			set.setDescription(result.getString("description"));
			set.setOfficer(result.getString("officers"));
			set.giveCitizenship(result.getString("citizens"));
			settlements.add(set);
		}
	}

	/**
	 * Save settlements (Call onDisable)
	 * 
	 * @return Returns true if successful, false otherwise
	 * @throws SQLException 
	 */
	public static void saveSettlements() throws SQLException{
		for (Settlement set : settlements){
			String tempName = set.getName();
			long tempId = set.getId();
			String tempLeader = set.getLeader();
			String tempDesc = set.getDescription();
			ArrayList<String> tempCits = set.getCitizens();
			ArrayList<String> tempOffs = set.getOfficers();
			DatabaseUtils.queryOut("UPDATE settlements"
					+ "SET name='"+ tempName + "'"
					+ ", leader='"+ tempLeader +"'"
					+ ", description='"+ tempDesc +"'"
					+ ", citizens='"+ tempCits +"'"
					+ ", officers='"+ tempOffs +"'"
					+ "WHERE id='" + tempId + "';");
		}
	}

	/**
	 * Get the settlement of a player
	 * 
	 * @param name : The name of the player to get the settlement for
	 * @return The player's settlement. If they are not a member of a settlement, this will return null
	 */
	public Settlement getPlayerSettlement(String name){
		for (Settlement s : settlements){
			if (s.isCitizen(name) || s.isOfficer(name) || s.isLeader(name)) 
				return s;
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
			if (s.getName().equalsIgnoreCase(name)) 
				return true;
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
			if (s.getName().equalsIgnoreCase(name)) 
				return s;
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
			if (s.getId() == id)
				return s;
		}	
		return null;
	}

	/**
	 * Determine if a player is a citezen of a settlement
	 * 
	 * @param name : The name of the player to check
	 * @return True if the player is a citizen of a settlement
	 */
	public boolean isCitizenOfSettlement(String name){
		for (Settlement s : settlements){
			if (s.getCitizens().contains(name))
				return true;
		}
		return false;
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
				Settlement s = new Settlement(name);
				s.setLeader(sender.getName());
				settlements.add(s);
				DatabaseUtils.queryOut("INSERT INTO settlements (id, name, leader)"
						+ "VALUES ('" + s.getId() + "','" + s.getName() + "','" + s.getLeader() + "');");
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully created " + ChatColor.AQUA + s.getName());
				sender.sendMessage(ChatColor.GREEN + "You can now set a description by doing " + ChatColor.RED + "/s desc <description>");
				sender.sendMessage(ChatColor.GREEN + "For more things you can do, type " + ChatColor.RED + "/s");
			}else 
				sender.sendMessage(MessageType.CREATE_IN_SETTLEMENT.getMsg());
		}else
			sender.sendMessage(MessageType.SETTLEMENT_EXISTS.getMsg());
	}

	/**
	 * Delete a settlement. Only works if the settlement exists and the sender is the leader of the settlement
	 * 
	 * @param name : The name of the settlement to delete
	 * @param sender : Who issued the deletion of the settlement
	 * @throws SQLException 
	 */
	public void deleteSettlement(CommandSender sender) throws SQLException{
		if (settlementExists(getPlayerSettlement(sender.getName()).getName())){
			Settlement s = getPlayerSettlement(sender.getName());
			if (s.isLeader(sender.getName())){
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully deleted " + ChatColor.AQUA + s.getName());
				settlements.remove(s);
				DatabaseUtils.queryOut("DELETE FROM settlements WHERE id=" + s.getId() + ";");
			}else 
				sender.sendMessage(MessageType.DELETE_NOT_LEADER.getMsg());
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
					}else 
						sender.sendMessage(MessageType.INVITE_NOT_RANK.getMsg());
				}else
					sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + player + ChatColor.GRAY + " is already in your Settlement!");
			}else
				sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	/**
	 * Accept an invite to a Settlement. Will only work if the player has a pending invite
	 * 
	 * @param player : The player who is accepting the invite
	 * @throws SQLException 
	 */
	@SuppressWarnings("deprecation")
	public void acceptInvite(String player) throws SQLException{
		if (hasInvite(player)){
			if (getPlayerSettlement(player) == null){
				Settlement s = invitedPlayers.get(player);
				s.giveCitizenship(player);
				invitedPlayers.remove(player);

				DatabaseUtils.queryOut("UPDATE settlements SET citizens='"+ s.getCitizens() +"'WHERE id='" + s.getId() + "';");

				Bukkit.getPlayer(player).sendMessage(MessageType.PREFIX.getMsg() + 
						ChatColor.GRAY + "Successfully joined " + ChatColor.AQUA + getPlayerSettlement(player).getName()); 
				//getPlayerSettlement() to confirm their settlement instead of s.getName()
				s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + player + ChatColor.GRAY + " joined the Settlement!");	
			}else 
				Bukkit.getPlayer(player).sendMessage(MessageType.CURRENTLY_IN_SETTLEMENT.getMsg());
		}else
			Bukkit.getPlayer(player).sendMessage(MessageType.NO_INVITE.getMsg());
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
		}else
			Bukkit.getPlayer(player).sendMessage(MessageType.NO_INVITE.getMsg());
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
			Settlement s = getPlayerSettlement(name);
			if (!s.isLeader(name)){
				Bukkit.getPlayer(name).sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully left " + s.getName());
				s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + name + ChatColor.GRAY + " left the Settlement :(");
				s.revokeCitizenship(name);

				//Remove member form database
				DatabaseUtils.queryOut("UPDATE settlements SET citizens='"+ s.getCitizens() +"'WHERE id=" + s.getId() + ";");
			}else
				Bukkit.getPlayer(name).sendMessage(MessageType.MUST_APPOINT_NEW_LEADER.getMsg());
		}else 
			Bukkit.getPlayer(name).sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
	}
	
	/**
	 * Set the description for a Settlement
	 * 
	 * @param sender : Who issued the command
	 * @param desc : The description for the Settlement
	 * @throws SQLException
	 */
	public void setDescription(CommandSender sender, String desc) throws SQLException{
		if (!(getPlayerSettlement(sender.getName()) == null)){
			Settlement s = getPlayerSettlement(sender.getName());
			if (s.isOfficer(sender.getName()) || s.isLeader(sender.getName())){
				s.setDescription(desc);
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Set your Settlement's description to " + desc);
				
				DatabaseUtils.queryOut("UPDATE settlements SET description='" + desc + "' WHERE id=" + s.getId() + ";");
			}else
				sender.sendMessage(MessageType.DESCRIPTION_NOT_RANK.getMsg());
		}else
			sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
	}
	
	/**
	 * List all the members of a given Settlement
	 * 
	 * @param sender : Who sent the command and will receive the list
	 * @param settlement : The Settlement
	 */
	public void listMembers(CommandSender sender, Settlement settlement){
		if (settlementExists(settlement.getName())){
			sender.sendMessage(MessageType.PREFIX.getMsg() + 
					ChatColor.GRAY + "All members in the Settlement " + 
					ChatColor.AQUA + settlement.getName() + ": " + settlement.memberSize());
			sender.sendMessage(ChatColor.GREEN + "Leader:");
			sender.sendMessage(ChatColor.DARK_AQUA + "-" + settlement.getLeader());
			sender.sendMessage(ChatColor.GREEN + "Officers:");
			for (String s : settlement.getOfficers())
				sender.sendMessage(ChatColor.DARK_AQUA + "-" + s);
			sender.sendMessage(ChatColor.GREEN + "Citizens");
			for (String s : settlement.getCitizens())
				sender.sendMessage(ChatColor.DARK_AQUA + "-" +  s);
		}else
			sender.sendMessage(MessageType.SETTLEMENT_NOT_EXIST.getMsg());
	}
}