package me.projectx.Settlements.Events;

import me.projectx.Settlements.Utils.PlayerCache;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener{
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		PlayerCache.getCache().playerMap.remove(e.getPlayer().getName());
	}
}
