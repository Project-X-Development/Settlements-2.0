package me.projectx.Settlements.Utils;

import org.bukkit.ChatColor;

public enum MessageType {

	PREFIX("&8[&bSettlements&8] "),
	NO_INVITE(PREFIX.getMsg() + "&4You don't have any pending invites :("),
	INVITE_NOT_RANK(PREFIX.getMsg() + "&4You must be an Officer or Leader in order to invite someone to your Settlement!"),
	DESCRIPTION_NOT_RANK(PREFIX.getMsg() + "&4 You must be an Officer or Leader in order to change the Settlement's description!"),
	DELETE_NOT_LEADER(PREFIX.getMsg() + "&4You must be the leader of a Settlement to delete it!"),
	KICK_NOT_LEADER(PREFIX.getMsg() + "&4You cannot kick your leader!"),
	CURRENTLY_IN_SETTLEMENT(PREFIX.getMsg() + "&4You must leave your Settlement to join another one!"),
	SETTLEMENT_EXISTS(PREFIX.getMsg() + "&4That Settlement already exists!"),
	SETTLEMENT_NOT_EXIST(PREFIX.getMsg() + "&4That Settlement could not be found :("),
	CREATE_IN_SETTLEMENT(PREFIX.getMsg() + "&4You cannot be a member of a Settlement when creating a new one!"),
	NOT_IN_SETTLEMENT(PREFIX.getMsg() + "&4You are not in a Settlement!"),
	MUST_APPOINT_NEW_LEADER(PREFIX.getMsg() + "&e Please appoint a new leader before leaving!"),
	NOT_PLAYER(PREFIX.getMsg() + "&4You must be a player to use that command!"),
	CHUNK_CLAIM_SUCCESS(PREFIX.getMsg() + "&7Successfully claimed your current chunk!"),
	CHUNK_CLAIM_SAFEZONE(PREFIX.getMsg() + "&6Claimed SafeZone"),
	CHUNK_CLAIM_OWNED(PREFIX.getMsg() + "&4Someone already owns this chunk!"),
	CHUNK_UNCLAIM_SUCCESS(PREFIX.getMsg() + "&7Successfully unclaimed your current chunk!"),
	CHUNK_UNCLAIM_FAIL(PREFIX.getMsg() + "&4You can't unclaim a chunk you don't own!"),
	NO_PERM(PREFIX.getMsg() + "&4You don't have permission to use that command!");

	private String msg;

	MessageType(String m){
		this.msg = m;
	}

	public String getMsg(){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
