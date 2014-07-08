package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.PlayerManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.Settlement;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		String name = e.getPlayer().getName();
		/*ChatManager.getManager().formatMessage(e.getPlayer(), e.getMessage());
		
		new FancyMessage("Player ")
		.color(ChatColor.DARK_RED)
	.then(" changed ").color(ChatColor.DARK_RED)
	.then(" blocks. ").color(ChatColor.DARK_RED)
	.then("Roll back?")
		.color(ChatColor.GOLD)
		.style(ChatColor.UNDERLINE)
		.suggest("/rollenbacken ")
		.tooltip("Be careful, this might undo legitimate edits!")
	.then(" ")
	.then("Ban?")
		.color(ChatColor.RED)
		.style(ChatColor.UNDERLINE)
		.suggest("/banhammer ")
		.tooltip("Remember: only ban if you have photographic evidence of grief.").send(e.getPlayer());*/

		if (!(SettlementManager.getManager().getPlayerSettlement(name) == null)){
			Settlement s  = SettlementManager.getManager().getPlayerSettlement(name);
			
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
				e.setFormat(ChatColor.DARK_GRAY + "[" + getColor(e.getPlayer()) + 
						SettlementManager.getManager().getPlayerSettlement(name).getName() + 
						ChatColor.DARK_GRAY + "] " + e.getFormat());
			}
		}
	}
	
	private ChatColor getColor(Player player){
		Settlement s = SettlementManager.getManager().getPlayerSettlement(player.getUniqueId());
		if (s != null){
			String rank = s.getRank(player);
			if (rank.equals("Leader"))
				return ChatColor.GOLD;
			else if (rank.equals("Officer"))
				return ChatColor.BLUE;
			else if (rank.equals("Citizen"))
				return ChatColor.AQUA;
		}else{
			return ChatColor.AQUA;
		}
		return null;
	}
}
