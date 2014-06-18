package me.projectx.Settlements.Managers;

import org.bukkit.entity.Player;

import me.projectx.Settlements.Utils.RenderMap;

public class MapManager {

	private static MapManager mm = new MapManager();
	private RenderMap rm = new RenderMap();
	
	public static MapManager getInstance(){
		return mm;
	}
	
	public RenderMap getRenderMap(){
		return this.rm;
	}
	
	public void add(Player pl){
		this.rm.add(pl);
	}
	
	public void remove(Player pl){
		this.rm.remove(pl);
	}
	
}
