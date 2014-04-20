package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.SettlementManager;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		String name = e.getPlayer().getName();

		if (!(SettlementManager.getManager().getPlayerSettlement(name) == null)){
			e.setFormat(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + 
					SettlementManager.getManager().getPlayerSettlement(name).getTag() + 
					ChatColor.DARK_GRAY + "] " + e.getFormat());
		}
	}	
}
