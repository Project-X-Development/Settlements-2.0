package me.projectx.settlements.events;

import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.managers.MapManager;
import me.projectx.settlements.managers.PlayerManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.Players;
import me.projectx.settlements.models.Settlement;
import me.projectx.settlements.utils.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();

		Players pl = PlayerManager.getInstance().addPlayer(p);
		pl.setInt("map", 25);
		
		MapManager.getInstance().add(e.getPlayer());
		
		boolean has = false;
		for(ItemStack is : e.getPlayer().getInventory().getContents()){
			if(is!=null&&is.getItemMeta()!=null&&is.getItemMeta().getDisplayName()!=null){
				if(is.getItemMeta().getDisplayName().equals(ChatColor.RED + "Dab " + ChatColor.BLUE + "Maps")){
					e.getPlayer().getInventory().remove(is);
					has = true;
				}
			}
		}
		if(has)MapManager.getInstance().givePlayerMap(e.getPlayer());

		Settlement s = SettlementManager.getManager().getPlayerSettlement(p.getUniqueId());
		if (s != null){
			s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.GREEN + p.getName() + ChatColor.GOLD + " has joined the game");
			s.sendAllianceMessage(MessageType.PREFIX.getMsg() + ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.GOLD + " has joined the game");
		}
		
		e.setJoinMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.GRAY + " has joined the server");

		if (!p.hasPlayedBefore()){
			p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GOLD + "Welcome to the server " + p.getName() + "!");
			FancyMessage msg = new FancyMessage("Be sure to check out the wiki to learn how to play!").color(ChatColor.AQUA).link("http://project-x.me/x/?p=6^Wiki");
			msg.send(p);
		}
	}
}
