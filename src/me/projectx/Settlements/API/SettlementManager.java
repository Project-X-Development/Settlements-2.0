package me.projectx.Settlements.API;

import java.util.ArrayList;
import java.util.HashMap;

import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SettlementManager {
	
	private ArrayList<Settlement> settlements = new ArrayList<Settlement>();
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
	 * Still need to make database call to permanently save the settlement
	 * 
	 * @param name : The name of the new settlement
	 * @param sender : Who issued the creation of the settlement
	 */
	public void createSettlement(String name, CommandSender sender){
		if (!settlementExists(name)){
			if (getPlayerSettlement(sender.getName()) == null){
				Settlement s = new Settlement(name);
				s.setLeader(sender.getName());
				settlements.add(s);
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
	 * Still need to make database call to permanently save the settlement
	 * 
	 * @param name : The name of the settlement to delete
	 * @param sender : Who issued the deletion of the settlement
	 */
	public void deleteSettlement(CommandSender sender){
		if (settlementExists(getPlayerSettlement(sender.getName()).getName())){
			Settlement s = getPlayerSettlement(sender.getName());
			if (s.isLeader(sender.getName())){
				sender.sendMessage("Successfully deleted " + s.getName());
				settlements.remove(s);
				s = null; //Idk if this is necessary, but it's here ;)
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
	 */
	public void acceptInvite(String player){
		if (hasInvite(player)){
			if (getPlayerSettlement(player) == null){
				Settlement s = invitedPlayers.get(player);
				s.giveCitizenship(player);
				invitedPlayers.remove(player);
				
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
	 */
	public void leaveSettlement(String name){
		if (!(getPlayerSettlement(name) == null)){
			Bukkit.getPlayer(name).sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully left " + getPlayerSettlement(name).getName());
			getPlayerSettlement(name).sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + name + ChatColor.GRAY + " left the Settlement :(");
			getPlayerSettlement(name).revokeCitizenship(name);
		}else
			Bukkit.getPlayer(name).sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
	}
}