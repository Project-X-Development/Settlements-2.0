package me.projectx.Settlements.Events;

import java.util.HashMap;

import me.projectx.Settlements.Land.ChunkManager;
import me.projectx.Settlements.Land.ClaimedChunk;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener{

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if (ChunkManager.getInstance().isInChunk(e.getPlayer())){
			if (e.getFrom().getChunk() != e.getTo().getChunk()){
				//if (ClaimedChunk.instances.)
				ChunkManager.getInstance().sendInChunkMsg(e.getPlayer());
			}
		}	
	}
}
