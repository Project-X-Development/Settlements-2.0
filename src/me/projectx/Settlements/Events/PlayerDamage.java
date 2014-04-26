package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Utils.ClaimType;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {
	
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof Player){	
			Chunk c = e.getEntity().getLocation().getChunk();
			int x = c.getX();
			int z = c.getZ();

			if (ChunkManager.getInstance().isClaimed(x, z)){
				if (ChunkManager.getInstance().getChunk(x, z).getType() == ClaimType.SAFEZONE){
					e.setCancelled(true);
				}
			}
		}
	}
}
