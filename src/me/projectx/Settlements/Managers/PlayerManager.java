package me.projectx.Settlements.Managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.projectx.Settlements.Models.Players;

public class PlayerManager {
	
	private static PlayerManager pm = new PlayerManager();
	
	public static PlayerManager getInstance(){
		return pm;
	}

	private List<Players> playerobjects = new ArrayList<Players>();
	
	public Players addPlayer(Player player){
		Players pl = new Players(player);
		this.playerobjects.add(pl);
		return pl;
	}
	
	public Players getPlayer(Player player){
		for(Players p : playerobjects){
			if(p.getString("uuid").equals(player.getUniqueId().toString())){
				return p; 
			}
		}
		return addPlayer(player);
	}
}
