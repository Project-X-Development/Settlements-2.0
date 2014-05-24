package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.PlayerManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.Settlement;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		String name = e.getPlayer().getName();

		if (!(SettlementManager.getManager().getPlayerSettlement(name) == null)){
			Settlement s  = SettlementManager.getManager().getPlayerSettlement(name);
			/*e.setFormat(new FancyMessage(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + 
					SettlementManager.getManager().getPlayerSettlement(name).getName() + 
					ChatColor.DARK_GRAY + "] ").toJSONString() + e.getFormat());*/
			
			if (PlayerManager.getInstance().isAllianceChatting(e.getPlayer())){
				e.setCancelled(true);
				s.sendAllianceMessage(ChatColor.LIGHT_PURPLE + "[" + e.getPlayer().getDisplayName() + 
						ChatColor.LIGHT_PURPLE + "] " + e.getFormat());
				System.out.println("[Settlements] Alliance Chat: " + e.getPlayer().getName() + " - " + e.getMessage());
			}else if (PlayerManager.getInstance().isSettlementChatting(e.getPlayer())){
				e.setCancelled(true);
				s.sendSettlementMessage(ChatColor.GREEN + "[" + e.getPlayer().getDisplayName() + 
						ChatColor.GREEN + "] " + e.getMessage());
				System.out.println("[Settlements] Settlement Chat: " + e.getPlayer().getName() + " - " + e.getMessage());
			}else{
				e.setFormat(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + 
						SettlementManager.getManager().getPlayerSettlement(name).getName() + 
						ChatColor.DARK_GRAY + "] " + e.getFormat());
			}
		}
	}
}
