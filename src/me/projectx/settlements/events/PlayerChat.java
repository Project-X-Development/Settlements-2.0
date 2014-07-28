package me.projectx.settlements.events;

import me.projectx.settlements.managers.ChatManager;
import me.projectx.settlements.managers.PlayerManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.Settlement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Settlement s = SettlementManager.getManager().getPlayerSettlement(e.getPlayer().getUniqueId());
		if (s != null){
			e.setCancelled(true);
			if (PlayerManager.getInstance().isAllianceChatting(e.getPlayer())){
				s.sendAllianceMessage(ChatColor.LIGHT_PURPLE + "[" + e.getPlayer().getDisplayName() + 
						ChatColor.LIGHT_PURPLE + "] " + e.getFormat());
				System.out.println("[Settlements] Alliance Chat: " + e.getPlayer().getName() + " - " + e.getMessage());
			}else if (PlayerManager.getInstance().isSettlementChatting(e.getPlayer())){
				s.sendSettlementMessage(ChatColor.GREEN + "[" + e.getPlayer().getDisplayName() + 
						ChatColor.GREEN + "] " + e.getMessage());
				System.out.println("[Settlements] Settlement Chat: " + e.getPlayer().getName() + " - " + e.getMessage());
			}else{
				for (Player p : Bukkit.getOnlinePlayers())
					ChatManager.getManager().formatMessage(p, e.getPlayer().getName(), e.getMessage());
				System.out.println("[" + s.getName() + "] " + e.getPlayer().getName() + ": " + e.getMessage());
			}
		}
	}
}
