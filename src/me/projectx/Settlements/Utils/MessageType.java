package me.projectx.Settlements.Utils;

import org.bukkit.ChatColor;

public enum MessageType {
	
	PREFIX("&8[&bSettlements&8] "),
	NO_INVITE(PREFIX.getMsg() + "&4You don't have any pending invites :("),
	INVITE_NOT_RANK(PREFIX.getMsg() + "&4You must be an Officer or Leader in order to invite someone to your Settlement!"),
	DELETE_NOT_LEADER(PREFIX.getMsg() + "&4You must be the leader of a Settlement to delete it!"),
	CURRENTLY_IN_SETTLEMENT(PREFIX.getMsg() + "&4You must leave your Settlement to join another one!"),
	SETTLEMENT_EXISTS(PREFIX.getMsg() + "&4That Settlement already exists!"),
	CREATE_IN_SETTLEMENT(PREFIX.getMsg() + "&4You cannot be a member a Settlement when creating a new one!"),
	NOT_IN_SETTLEMENT(PREFIX.getMsg() + "&4You are not in a Settlement!"),
	MUST_APPOINT_NEW_LEADER(PREFIX.getMsg() + "&7You must appoint a new leader before leaving!"),
	NO_PERM(PREFIX.getMsg() + "&4You don't have permission to use that command!");
	
	private String msg;
	
	MessageType(String m){
		this.msg = m;
	}
	
	public String getMsg(){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
