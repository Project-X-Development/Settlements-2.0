package me.projectx.Settlements.Events;

import me.projectx.Settlements.API.Settlement;
import me.projectx.Settlements.Land.ChunkManager;
import me.projectx.Settlements.Land.ClaimedChunk;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove extends Thread implements Listener{
	
	@EventHandler
	public void onMove(final PlayerMoveEvent e){
		new Thread() {
			@Override
			public void run() {
				if (e.getFrom().getChunk() != e.getTo().getChunk()){
					ClaimedChunk c = ChunkManager.getInstance().getChunk(e.getFrom().getChunk().getX(), e.getFrom().getChunk().getZ());
					ClaimedChunk d = ChunkManager.getInstance().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ());
					if(c==null&&d==null){
					}else if(c!=null&&d==null){ //Leaving b to non claimed
						e.getPlayer().sendMessage(ChatColor.GREEN + "~Wilderness");
					}else if(d!=null&&c==null){ //Entering a from unclaimed
						Settlement a = d.getSettlement();
						e.getPlayer().sendMessage(a.getName() + " ~ " + a.getDescription());
					}else if(c!=null&&d!=null){ //Entering one claim to another claim
						Settlement a = d.getSettlement();
						Settlement b = c.getSettlement();
						if(!a.equals(b))
							e.getPlayer().sendMessage(ChatColor.GREEN + "Leaving " + a.getName() + " entering " + b.getName());    
					}
				}	     
			}
		}.start();
	}
}
