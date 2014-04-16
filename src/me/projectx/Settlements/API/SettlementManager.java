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
	private HashMap<String, Settlement> invitedPlayers = new HashMap<String, Settlement>();
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
		ResultSet result = DatabaseUtils.query("SELECT * FROM settlements;");
		ArrayList<Settlement> tempSet = new ArrayList<Settlement>();  
		while (result.next()){
			String name = result.getString("name");
			Settlement set = new Settlement(name);
			tempSet.add(set);
		}
		settlements = tempSet;
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
			DatabaseUtils.query("UPDATE settlements"
					+ "SET name='"+ tempName + "'"
					+ ", leader='"+ tempLeader +"'"
					+ ", description='"+ tempDesc +"'"
					+ ", citizens='"+ tempCits +"'"
					+ ", officers='"+ tempOffs +"'"
					+ "WHERE id='" + tempId + "';");
		}
	}
	
	//End database stuff//
	
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
			if (s.getId() == id){
				return s;
			}
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
	 * <p>
	 * TODO
	 * <p>
	 * -- Still need to make database call to permanently save the settlement -- DONE
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
				
				//Create in database
				DatabaseUtils.query("INSERT INTO settlements (id, name, leader)"
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
	 *  <p>
	 * TODO
	 * <p>
	 * -- Still need to make database call to permanently save the settlement -- (DONE)
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
				
				//Remove from database - Checking leader for extra precaution
				DatabaseUtils.query("DELETE FROM settlements"
						+ "WHERE id='" + s.getId() + "' AND leader='" + s.getLeader() + "';");
				
			}else
				sender.sendMessage(MessageType.DELETE_NOT_LEADER.getMsg());
		}
	}
	
	/**
	 * Invite a player to join the Settlement. The command sender must be an Officer or higher in the Settlement
	 * 
	 * <p>
	 * TODO
	 * <p>
	 * Add notification system incase player is offline
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
				
				DatabaseUtils.query("UPDATE settlements"
						+ "SET citizens='"+ s.getCitizens() +"'"
						+ "WHERE id='" + s.getId() + "';");
				
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
				DatabaseUtils.query("UPDATE settlements"
						+ "SET citizens='"+ s.getCitizens() +"'"
						+ "WHERE id='" + s.getId() + "';");
			}else
				Bukkit.getPlayer(name).sendMessage(MessageType.MUST_APPOINT_NEW_LEADER.getMsg());
		}else
			Bukkit.getPlayer(name).sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
	}
}