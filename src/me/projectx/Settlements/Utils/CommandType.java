package me.projectx.Settlements.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum CommandType {
	
	SETTLEMENT_CREATE("/s create <name>", "Create a new settlement", "settlements.player.create"),
	SETTLEMENT_DELETE("/s delete <name>", "Delete a settlement", "settlements.player.delete"),
	SETTLEMENT_MEMBER_ADD("/s invite <player>", "Invite a player to join the settlement", "settlements.player.invite"),
	SETTLEMENT_MEMBER_REMOVE("/s remove <player>", "Remove a player from the settlement", "settlements.player.remove");
	
	private String usage, info, perm;
	private static ArrayList<String> list = new ArrayList<String>();
	
	CommandType(String u, String i, String p){
		this.usage = u;
		this.info = i;
		this.perm = p;
	}
	
	public String getUsage(){
		return usage;
	}
	
	public String getInfo(){
		return info;
	}
	
	public String getPerm(){
		return perm;
	}
	
	//use at startup
	public static void prepareCommandList(){	
		for (CommandType c : CommandType.values()){
			if (!list.contains(c.getUsage()))
				list.add(c.getUsage());
		}
	}

	public static void printList(CommandSender s){
		for (int i = 0; i < list.size(); i++)
			s.sendMessage(ChatColor.DARK_PURPLE + "" + (i + 1) + ". " + ChatColor.AQUA + list.get(i));
	}
}
