package me.projectx.settlements.events;

import me.projectx.settlements.enums.ClaimType;
import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.Settlement;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {
	
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e){
		Chunk c = e.getEntity().getLocation().getChunk();
		int x = c.getX();
		int z = c.getZ();

		if (ChunkManager.getManager().isClaimed(x, z, c.getWorld())){
			if (ChunkManager.getManager().getChunk(x, z, c.getWorld()).getType() == ClaimType.SAFEZONE){
				e.setCancelled(true);
			}
		}
		
		if (e.getEntity() instanceof Player){	
			if (e.getDamager() instanceof Player){
				Player damaged = (Player) e.getEntity();
				Player damager = (Player) e.getDamager();
				Settlement a = SettlementManager.getManager().getPlayerSettlement(damaged.getUniqueId());
				Settlement b = SettlementManager.getManager().getPlayerSettlement(damager.getUniqueId());
				
				if (a.hasAlly(b)){
					e.setCancelled(true);
					damager.sendMessage(MessageType.ALLIANCE_MEMBER_DAMAGE.getMsg());
				}
				
				if (a.getId() == b.getId()){
					e.setCancelled(true);
					damager.sendMessage(MessageType.SETTLEMENT_MEMBER_DAMAGE.getMsg());
				}
			}
		}
	}
}
