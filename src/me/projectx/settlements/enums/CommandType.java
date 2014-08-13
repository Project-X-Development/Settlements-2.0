package me.projectx.settlements.enums;

import java.util.ArrayList;
import java.util.Arrays;

import me.projectx.settlements.managers.SettlementManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum CommandType {
	
	SETTLEMENT_CREATE("/s create <name>", "Create a new Settlement", "settlements.player.create"),
	SETTLEMENT_DELETE("/s delete", "Delete a Settlement", "settlements.player.delete"),
	SETTLEMENT_DESC("/s desc <description>", "Set a description for the Settlement", "settlements.player.desc"),
	SETTLEMENT_MEMBER_ADD("/s invite <player>", "Invite a player to join the Settlement", "settlements.player.invite"),
	SETTLEMENT_INVITE_ACCEPT("/s accept", "Accept an invitation to join a Settlement", "settlements.player.accept"),
	SETTLEMENT_INVITE_DECLINE("/s decline", "Decline an invitation to join a Settlement", "settlements.player.decline"),	
	SETTLEMENT_MEMBER_LIST("/s members", "List all of the players in your Settlement", "settlements.player.list"),
	SETTLEMENT_LIST("/s list", "List all Settlements", "settlements.player.slist"),
	SETTLEMENT_MEMBER_REMOVE("/s kick <player>", "Kick a player from the Settlement", "settlements.player.kick"),
	SETTLEMENT_LEAVE("/s leave", "Leave your current Settlement", "settlements.player.leave"),
	SETTLEMENT_CLAIM("/s claim", "Claim the chunk you are standing in", "settlements.player.claim"),
	SETTLEMENT_UNCLAIM("/s unclaim", "Unlaim the chunk you are standing in", "settlements.player.unclaim"),
	SETTLEMENT_MAP("/s map", "Issues a map that displays claimed chunks around you", "settlements.player.map"),
	SETTLEMENT_ALLY("/s ally <Settlement>", "Send a request to ally another Settlement", "settlements.player.ally"), 
	SETTLEMENT_ALLY_LIST("/s allies", "Display all of your Settlement's allies", "settlements.player.allies"),
	SETTLEMENT_NO_ALLY("/s removeally <Settlement>", "Remove a Settlement from your alliance", "settlements.player.unally"),
	SETTLEMENT_CHAT("/s chat", "Toggle private Settlement chatting on/off", "settlements.player.chat"),
	SETTTLEMENT_SETHOME("/s sethome", "Set your Settlement's home to your location", "settlements.player.home.set"),
	SETTLEMENT_HOME("/s home", "Teleport to your Settlement's home", "settlements.player.home.tp");
	
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
	
	public static void displayCommands(Player player){
		Inventory i = Bukkit.createInventory(null, SettlementManager.getInventorySize(list.size()), ChatColor.DARK_RED + "Settlement Commands");
		ItemStack is = new ItemStack(Material.BEACON);
		ItemMeta im = is.getItemMeta();
		int slot = 0;
		
		for (CommandType ct : CommandType.values()){
			im.setDisplayName(ChatColor.AQUA + ct.getUsage());
			im.setLore(Arrays.asList(ChatColor.GOLD + ct.getInfo()));
			is.setItemMeta(im);
			i.setItem(slot, is);
			slot++;
		}
		player.openInventory(i);
	}
}
