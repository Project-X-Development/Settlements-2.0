package me.projectx.settlements.events;

import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.ClaimedChunk;

import org.bukkit.Chunk;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener{
	
	private ChunkManager cm = ChunkManager.getManager();
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof ItemFrame && e.getDamager() instanceof Player){
			Chunk c = e.getEntity().getLocation().getChunk();
			if (cm.isClaimed(c.getX(), c.getZ(), c.getWorld())){
				ClaimedChunk cc = cm.getChunk(c.getX(), c.getZ(), c.getWorld());
				Player p = (Player) e.getDamager();
				if (cc.getSettlement().getId() != SettlementManager.getManager().getPlayerSettlement(p.getUniqueId()).getId()){
					e.setCancelled(true);
				}
			}
		}
	}
}
