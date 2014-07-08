package me.projectx.Settlements.Managers;

import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.Fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatManager {
	
	private static ChatManager cm = new ChatManager();
	
	public static ChatManager getManager(){
		return cm;
	}
	
	//Need to fix this
	public void formatMessage(Player player, String message){
		Settlement s = SettlementManager.getManager().getPlayerSettlement(player.getUniqueId());
		if (s != null){
			new FancyMessage("[")
				.color(ChatColor.DARK_GRAY)
			.then(s.getName())
				.tooltip(s.getDescription())
				.color(ChatColor.AQUA)
			.then("]")
				.color(ChatColor.DARK_GRAY)
			.then(player.getDisplayName() + ": ")
			.then(message)
			.send(player);
		}
	}
	
}
