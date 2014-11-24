package me.projectx.settlements.events;

import java.util.List;

import me.projectx.settlements.Main;
import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.models.ClaimedChunk;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkUnload implements Listener{
	
	private ChunkManager cm = ChunkManager.getManager();
	
	@EventHandler
	public void onUnload(ChunkUnloadEvent e){
		final Chunk c = e.getChunk();
		new BukkitRunnable(){
			@Override
			public void run(){
				ClaimedChunk chunk = cm.getChunk(c.getX(), c.getZ(), c.getWorld());
				List<ClaimedChunk> cc = cm.setClaims.get(chunk.getSettlement().getName());
				if (cc.contains(chunk)) cc.remove(chunk);
				cm.claimedChunks.remove(chunk);
				System.out.println("[Settlements] Unloaded chunk type " + chunk.getType() + " at x:" + chunk.getX() + " z:" + chunk.getZ() + " in the world " + chunk.getWorld().getName());
				chunk = null;
				this.cancel();
			}
		}.runTaskAsynchronously(Main.getInstance());
	}
	
}
