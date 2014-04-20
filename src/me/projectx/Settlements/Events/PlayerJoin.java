package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.PlayerCache;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		PlayerCache.getCache().put(e.getPlayer().getName(), e.getPlayer().getUniqueId());
		if (SettlementManager.getManager().isCitizenOfSettlement(e.getPlayer().getName())){
			Settlement set = SettlementManager.getManager().getPlayerSettlement(e.getPlayer().getName());
			set.getTeam().addPlayer(e.getPlayer());
		}
	}
}
