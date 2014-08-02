package me.projectx.settlements.managers;

import me.projectx.settlements.models.Settlement;
import me.projectx.settlements.utils.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatManager {
	
	private static ChatManager cm = new ChatManager();
	
	public static ChatManager getManager(){
		return cm;
	}
	
	public void formatMessage(Player sendTo, String playerName, String message){
		Player p = Bukkit.getPlayer(playerName);
		Settlement s = SettlementManager.getManager().getPlayerSettlement(p.getUniqueId());
		if (s != null){
			new FancyMessage("[")
				.color(ChatColor.DARK_GRAY)
			.then(s.getName())
				.tooltip(ChatColor.DARK_GREEN + "Leader: " + ChatColor.RED + Bukkit.getPlayer(s.getLeader()).getName(),
						ChatColor.GOLD + "Description: " + ChatColor.GREEN + s.getDescription())
				.color(getColor(p))
			.then("] ")
				.color(ChatColor.DARK_GRAY)
			.then("\u25CF ")
				.color(ChatColor.WHITE)
			.then(p.getDisplayName() + " \u25CF ")
			.then(ChatColor.translateAlternateColorCodes('&', message))
			.send(sendTo);
		}
	}
	
	private ChatColor getColor(Player player){
		Settlement s = SettlementManager.getManager().getPlayerSettlement(player.getUniqueId());
		if (s != null){
			switch(s.getRank(player)){
				case "Leader":
					return ChatColor.GOLD;
				case "Officer":
					return ChatColor.YELLOW;
				default:
					return ChatColor.AQUA;
			}
		}
		return null;
	}
	
}
