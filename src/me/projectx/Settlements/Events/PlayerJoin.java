package me.projectx.Settlements.Events;

import me.projectx.Settlements.Utils.PlayerCache;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		PlayerCache.getCache().put(e.getPlayer().getName(), e.getPlayer().getUniqueId());
	}
}
