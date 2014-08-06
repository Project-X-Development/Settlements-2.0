package me.projectx.settlements.enums;

import org.bukkit.ChatColor;

public enum MessageType {

	PREFIX("&8[&bSettlements&8] "),
	NO_INVITE(PREFIX.getMsg() + "&4You don't have any pending invites :("),
	INVITE_NOT_RANK(PREFIX.getMsg() + "&4You must be an Officer or Leader in order to invite someone to your Settlement!"),
	DESCRIPTION_NOT_RANK(PREFIX.getMsg() + "&4 You must be an Officer or Leader in order to change the Settlement's description!"),
	DELETE_NOT_LEADER(PREFIX.getMsg() + "&4You must be the leader of a Settlement to delete it!"),
	KICK_NOT_LEADER(PREFIX.getMsg() + "&4You cannot kick your leader!"),
	ALLIANCE_CHAT_START(PREFIX.getMsg() + "&7Now chatting privately with alliance members"),
	ALLIANCE_CHAT_END(PREFIX.getMsg() + "&7No longer privately chatting with alliance members"),
	ALLIANCE_MEMBER_DAMAGE(PREFIX.getMsg() + "&cYou cannot hurt members of your own alliance!"),
	ALLIANCE_INVITE_SENT(PREFIX.getMsg() + "&7Alliance invite sent to &b<settlement>"),
	ALLIANCE_INVITE_PENDING(PREFIX.getMsg() + "&eYou've already sent an alliance request to &c<settlement> &ePlease wait for them to accept/deny it."),
	CURRENTLY_IN_SETTLEMENT(PREFIX.getMsg() + "&4You must leave your Settlement to join another one!"),
	SETTLEMENT_EXISTS(PREFIX.getMsg() + "&4That Settlement already exists!"),
	SETTLEMENT_NOT_EXIST(PREFIX.getMsg() + "&4That Settlement could not be found :("),
	SETTLEMENT_CHAT_START(PREFIX.getMsg() + "&7Now privately chatting with your Settlement members"),
	SETTLEMENT_CHAT_END(PREFIX.getMsg() + "&7No longer privately chatting with your Settlement members"),
	SETTLEMENT_MEMBER_DAMAGE(PREFIX.getMsg() + "&7You cannot hurt members of your own Settlement!"),
	SETTLEMENT_NO_MEMBER(PREFIX.getMsg() + "&4That player is not in your Settlement"),
	SETTLEMENT_NO_CLAIMS(PREFIX.getMsg() + "&eThat Settlement has no land claims!"),
	SETTLEMENT_BALANCE(PREFIX.getMsg() + "&7Settlement balance is &b$<bal>"),
	SETTLEMENT_BALANCE_NOT_ENOUGH(PREFIX.getMsg() + "&cYou don't have that much money to deposit into your Settlement."),
	SETTLEMENT_HOME_TP(PREFIX.getMsg() + "&7Teleported to your Settlement's home!"),
	SETTLEMENT_NO_HOME(PREFIX.getMsg() + "&4Your Settlement doesn't have a home set!"),
	SETTLEMENT_DELETE_CONFIRM(PREFIX.getMsg() + "&eAre you sure you want to delete your Settlement? Type &c/s delete &eagain to confirm or &c/s cancel &e to cancel"),
	SETTLEMENT_DELETE_CANCEL(PREFIX.getMsg() + "&eCancelled deletion"),
	CREATE_IN_SETTLEMENT(PREFIX.getMsg() + "&4You cannot be a member of a Settlement when creating a new one!"),
	NOT_IN_SETTLEMENT(PREFIX.getMsg() + "&4You are not in a Settlement!"),
	MUST_APPOINT_NEW_LEADER(PREFIX.getMsg() + "&e Please appoint a new leader before leaving!"),
	NOT_PLAYER(PREFIX.getMsg() + "&4You must be a player to use that command!"),
	CHUNK_CLAIM_SUCCESS(PREFIX.getMsg() + "&7Successfully claimed your current chunk!"),
	CHUNK_CLAIM_SAFEZONE(PREFIX.getMsg() + "&6Claimed SafeZone"),
	CHUNK_CLAIM_OWNED(PREFIX.getMsg() + "&4Someone already owns this chunk!"),
	CHUNK_UNCLAIM_SUCCESS(PREFIX.getMsg() + "&7Successfully unclaimed your current chunk!"),
	CHUNK_UNCLAIM_FAIL(PREFIX.getMsg() + "&4You can't unclaim a chunk you don't own!"),
	CHUNK_UNCLAIM_ERROR(PREFIX.getMsg() + "&4An error occured: Claim map doesn't contain this chunk!"),
	CHUNK_NOT_ADMIN(PREFIX.getMsg() + "&4You must be an admin in order to do that!"),
	CHUNK_AUTOCLAIM_NORMAL_START(PREFIX.getMsg() + ChatColor.GRAY + "Now auto-claiming land for your Settlement"),
	CHUNK_AUTOCLAIM_NORMAL_END(PREFIX.getMsg() + ChatColor.GRAY + "No longer auto-claiming land for your Settlement"),
	CHUNK_AUTOCLAIM_SZONE_START(PREFIX.getMsg() + "&6Now auto-claiming land for SafeZone"),
	CHUNK_AUTOCLAIM_SZONE_END(PREFIX.getMsg() + "&6No longer auto-claiming land for SafeZone"),
	COMMAND_INVALID_ARGS(PREFIX.getMsg() + "&4Invalid arguments. "),
	COMMAND_INVALID_ARGUMENT(PREFIX.getMsg() + "&4Invalid command argument specified"),
	NO_PERM(PREFIX.getMsg() + "&4You don't have permission to use that command!"),
	ISSSUE_MAP(PREFIX.getMsg() + "&eYou have been issued a Settlement map!");

	private String msg;

	MessageType(String m){
		this.msg = m;
	}

	public String getMsg(){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
