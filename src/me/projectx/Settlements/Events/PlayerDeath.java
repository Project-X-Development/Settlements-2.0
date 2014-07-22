package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Managers.WarManager;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.enums.MessageType;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		if (e.getEntity().getKiller() instanceof Player){
			SettlementManager sm = SettlementManager.getManager();
			Player dead = e.getEntity();
			Player killer = dead.getKiller();
			
			if (sm.getPlayerSettlement(dead.getName()) != null){
				Settlement s = sm.getPlayerSettlement(dead.getName());
				WarManager.getInstance().sendRequest(sm.getPlayerSettlement(killer.getName()), s);
				killer.sendMessage(MessageType.PREFIX.getMsg() + "You sent a request of war to " + dead.getName() + "'s Settlement!");
			}	
		}
	}	
}
