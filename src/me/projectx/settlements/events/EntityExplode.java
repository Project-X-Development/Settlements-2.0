package me.projectx.settlements.events;

import me.projectx.settlements.managers.ChunkManager;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener{

	@EventHandler
	public void onExplode(EntityExplodeEvent e){
		Chunk c = e.getLocation().getChunk();
		e.setCancelled(ChunkManager.getManager().isClaimed(c.getX(), c.getZ(), c.getWorld()));
	}
}
