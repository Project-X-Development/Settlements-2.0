package me.projectx.Settlements.Events;

import me.projectx.Settlements.API.Settlement;
import me.projectx.Settlements.Land.ChunkManager;
import me.projectx.Settlements.Land.ClaimedChunk;

import org.bukkit.ChatColor;
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
					e.getPlayer().sendMessage("Null"); //never sent
				}else if(c!=null&&d==null){
					Settlement b = c.getSettlement();
					//Leaving b to non claimed
					e.getPlayer().sendMessage(ChatColor.GREEN + "~Wilderness"); //sent when leaving claimed land
				}else if(d!=null&&c==null){
					Settlement a = d.getSettlement();
					//Entering a from unclaimed
					e.getPlayer().sendMessage("Test~Wilderness"); //never sent
				}else if(c!=null&&d!=null){
					Settlement a = d.getSettlement();
					Settlement b = c.getSettlement();
					if(!a.equals(b)){
						//Entering b
						ChunkManager.getInstance().sendInChunkMsg(e.getPlayer()); //never sent
					}      
				}
			}
		}      
	}
}
