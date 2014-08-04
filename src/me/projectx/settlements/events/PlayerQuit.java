package me.projectx.settlements.events;

import java.io.File;

import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.MapManager;
import me.projectx.settlements.managers.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener{
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		MapManager.getInstance().remove(e.getPlayer());
		File f = new File(Bukkit.getWorldContainer(), Bukkit.getWorlds().get(0).getName() + "/data/map_" + MapManager.getInstance().getPlayerMapID(e.getPlayer()));
		f.delete();
		PlayerManager.getInstance().removePlayer(e.getPlayer());
		ChunkManager.getManager().autoClaim.remove(e.getPlayer().getName());
	}
	
	@EventHandler
	public void onQuit(PlayerKickEvent e){
		MapManager.getInstance().remove(e.getPlayer());
		File f = new File(Bukkit.getWorldContainer(), Bukkit.getWorlds().get(0).getName() + "/data/map_" + MapManager.getInstance().getPlayerMapID(e.getPlayer()));
		f.delete();
		PlayerManager.getInstance().removePlayer(e.getPlayer());
		ChunkManager.getManager().autoClaim.remove(e.getPlayer().getName());
	}
}
