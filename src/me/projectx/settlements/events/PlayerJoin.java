package me.projectx.settlements.events;

import me.projectx.settlements.enums.MessageType;
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

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();

		Players pl = PlayerManager.getInstance().addPlayer(p);
		pl.setInt("map", 25);

		Settlement s = SettlementManager.getManager().getPlayerSettlement(p.getUniqueId());
		if (s != null){
			e.setJoinMessage(null);
			s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.GREEN + p.getName() + ChatColor.GOLD + " has joined the game");
			s.sendAllianceMessage(MessageType.PREFIX.getMsg() + ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.GOLD + " has joined the game");
		}

		if (!p.hasPlayedBefore()){
			p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GOLD + "Welcome to the server " + p.getName() + "!");
			FancyMessage msg = new FancyMessage("Be sure to check out the wiki to learn how to play!").color(ChatColor.AQUA).link("http://project-x.me/x/?p=6^Wiki");
			msg.send(p);
		}
	}
}
