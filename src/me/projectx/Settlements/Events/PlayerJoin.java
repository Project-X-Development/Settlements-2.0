package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.PlayerManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Managers.WarManager;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.PlayerCache;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		PlayerManager.getInstance().addPlayer(e.getPlayer());
		SettlementManager sm = SettlementManager.getManager();
		PlayerCache.getCache().put(e.getPlayer().getName(), e.getPlayer().getUniqueId());
		
		if (sm.getPlayerSettlement(e.getPlayer().getName()) != null){
			Settlement s = sm.getPlayerSettlement(e.getPlayer().getName());
			
			if (WarManager.getInstance().hasRequest(s)){
				Bukkit.getPlayer(s.getLeader()).sendMessage(WarManager.getInstance().getRequests().get(s) + " has requested to go to war!");
			}
		}
	}
}
