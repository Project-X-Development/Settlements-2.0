package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.PlayerManager;
import me.projectx.Settlements.Models.Players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Players pl = PlayerManager.getInstance().addPlayer(e.getPlayer());
		pl.setInt("map", 25);
		/*SettlementManager sm = SettlementManager.getManager();
		PlayerCache.getCache().put(e.getPlayer().getName(), e.getPlayer().getUniqueId());
		
		if (sm.getPlayerSettlement(e.getPlayer().getName()) != null){
			Settlement s = sm.getPlayerSettlement(e.getPlayer().getName());
			
			if (WarManager.getInstance().hasRequest(s)){
				Bukkit.getPlayer(s.getLeader()).sendMessage(WarManager.getInstance().getRequests().get(s) + " has requested to go to war!");
			}
		}*/
	}
}
