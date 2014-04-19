package me.projectx.Settlements.Events;

import me.projectx.Settlements.API.Settlement;
import me.projectx.Settlements.Land.ChunkManager;
import me.projectx.Settlements.Land.ClaimedChunk;

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
				ClaimedChunk c = ChunkManager.getInstance().getChunk(e.getFrom().getChunk().getX(), e.getFrom().getChunk().getZ());
				ClaimedChunk d = ChunkManager.getInstance().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ());
				if(c==null&&d==null){
					//both arent claimed
				}else if(c!=null&&d==null){
					Settlement b = ChunkManager.getInstance().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ()).getSettlement();
					//Entering b from non claimed
				}else if(d!=null&&c==null){
					Settlement a = ChunkManager.getInstance().getChunk(e.getFrom().getChunk().getX(), e.getFrom().getChunk().getZ()).getSettlement();
					//Entering nonclaimed from a
				}else if(c!=null&&d!=null){
					Settlement a = ChunkManager.getInstance().getChunk(e.getFrom().getChunk().getX(), e.getFrom().getChunk().getZ()).getSettlement();
					Settlement b = ChunkManager.getInstance().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ()).getSettlement();

					if(a!=b){
						//Entering b
						ChunkManager.getInstance().sendInChunkMsg(e.getPlayer());
					}      
				}
			}
		}      
	}
}
