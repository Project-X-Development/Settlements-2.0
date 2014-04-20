package me.projectx.Settlements.Events;

import java.sql.SQLException;

import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Models.ClaimedChunk;
import me.projectx.Settlements.Models.Settlement;

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
		
		if (ChunkManager.getInstance().isAutoClaiming(e.getPlayer())){
			try {
				ChunkManager.getInstance().claimChunk(e.getPlayer().getName(), 
						e.getPlayer().getLocation().getChunk().getX(), e.getPlayer().getLocation().getChunk().getZ(), e.getPlayer().getWorld());
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
