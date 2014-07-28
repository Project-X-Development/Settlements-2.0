package me.projectx.settlements.events;

import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.managers.WarManager;
import me.projectx.settlements.models.Settlement;

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
