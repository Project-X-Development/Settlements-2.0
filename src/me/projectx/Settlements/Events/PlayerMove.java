package me.projectx.Settlements.Events;

import java.util.HashMap;

import me.projectx.Settlements.API.Settlement;
import me.projectx.Settlements.API.SettlementManager;
import me.projectx.Settlements.Land.ChunkManager;
import me.projectx.Settlements.Land.ClaimedChunk;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener{

	/*@EventHandler
	public void onMove(PlayerMoveEvent e){
		if (ChunkManager.getInstance().isInChunk(e.getPlayer())){
			if (e.getFrom().getChunk() != e.getTo().getChunk()){
				if (ChunkManager.getInstance().isClaimed(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ())){
					/*if (!ChunkManager.getChunk(e.getFrom().getChunk().getX(), e.getFrom().getChunk().getZ()).getSettlement().equals(
	                                      ChunkManager.getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ()).getSettlement())){
						ChunkManager.getInstance().sendInChunkMsg(e.getPlayer());      
					//}
				}
			}
		}	
	}*/
	
	@EventHandler
    public void onMove(PlayerMoveEvent e){
		if (ChunkManager.getInstance().isInChunk(e.getPlayer())){
			if (e.getFrom().getChunk() != e.getTo().getChunk()){
				Settlement a = ChunkManager.getInstance().getChunk(e.getFrom().getChunk().getX(), e.getFrom().getChunk().getZ()).getSettlement();
				//Settlement b = ChunkManager.getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ()).getSettlement();
				if (a != null && ChunkManager.getInstance().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ()).getSettlement() !=null && a!=ChunkManager.getInstance().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ()).getSettlement()){
					ChunkManager.getInstance().sendInChunkMsg(e.getPlayer());      
				}else if(ChunkManager.getInstance().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ()).getSettlement()!=null && a==null){
					ChunkManager.getInstance().sendInChunkMsg(e.getPlayer());
				}else if(a!=null&&ChunkManager.getInstance().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ()).getSettlement()==null){
						e.getPlayer().sendMessage("Leaving claimed territory");
				}
			}
		}      
    }
}
