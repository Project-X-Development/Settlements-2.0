package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.PlayerManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.Players;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Players pl = PlayerManager.getInstance().addPlayer(e.getPlayer());
		pl.setInt("map", 25);
		
		Settlement s = SettlementManager.getManager().getPlayerSettlement(e.getPlayer().getUniqueId());
		if (s != null){
			e.setJoinMessage(null);
			s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.GREEN + e.getPlayer().getName() + ChatColor.GOLD + " has joined the game");
			s.sendAllianceMessage(MessageType.PREFIX.getMsg() + ChatColor.LIGHT_PURPLE + e.getPlayer().getName() + ChatColor.GOLD + " has joined the game");
		}
	}
}
