package me.projectx.settlements.managers;

import org.bukkit.entity.Player;

import me.projectx.settlements.models.Players;
import me.projectx.settlements.utils.RenderMap;

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

	public void increment(Player pl){
		Players p = PlayerManager.getInstance().getPlayer(pl);
		int i = p.getInt("map");
		switch(i){
		case 1:
			p.setInt("map", 3);
			remove(pl);
			break;
		case 3:
			p.setInt("map", 5);
			remove(pl);
			break;
		case 5:
			p.setInt("map", 7);
			remove(pl);
			break;
		case 7:
			p.setInt("map", 9);
			remove(pl);
			break;
		case 9:
			p.setInt("map", 11);
			remove(pl);
			break;
		case 11:
			p.setInt("map", 13);
			remove(pl);
			break;
		case 13:
			p.setInt("map", 17);
			remove(pl);
			break;
		case 17:
			p.setInt("map", 25);
			remove(pl);
			break;
		case 25:
			p.setInt("map", 41);
			remove(pl);
			break;
		case 41:
			p.setInt("map", 125);
			remove(pl);	
			break;		
		}
	}

	public void decrement(Player pl){
		Players p = PlayerManager.getInstance().getPlayer(pl);
		int i = p.getInt("map");
		switch(i){
		case 3:
			p.setInt("map", 1);
			remove(pl);
			break;
		case 5:
			p.setInt("map", 3);
			remove(pl);
			break;
		case 7:
			p.setInt("map", 5);
			remove(pl);
			break;
		case 9:
			p.setInt("map", 7);
			remove(pl);
			break;
		case 11:
			p.setInt("map", 9);
			remove(pl);
			break;
		case 13:
			p.setInt("map", 11);
			remove(pl);
			break;
		case 17:
			p.setInt("map", 13);
			remove(pl);
			break;
		case 25:
			p.setInt("map", 17);
			remove(pl);
			break;
		case 41:
			p.setInt("map", 25);
			remove(pl);
			break;
		case 125:
			p.setInt("map", 41);
			remove(pl);
			break;
		}
	}

}
