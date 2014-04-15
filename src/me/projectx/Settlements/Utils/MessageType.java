package me.projectx.Settlements.Utils;

import org.bukkit.ChatColor;

public enum MessageType {
	
	PREFIX("&8[&bSettlements&8] "),
	NO_PERM(PREFIX.getMsg() + "&4You don't have permission to use that command!");
	
	private String msg;
	
	MessageType(String m){
		this.msg = m;
	}
	
	public String getMsg(){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
