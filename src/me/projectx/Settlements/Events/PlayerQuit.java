package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.MapManager;
import me.projectx.Settlements.Managers.PlayerManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener{
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		MapManager.getInstance().remove(e.getPlayer());
		PlayerManager.getInstance().removePlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerKickEvent e){
		MapManager.getInstance().remove(e.getPlayer());
		PlayerManager.getInstance().removePlayer(e.getPlayer());
	}
}
