package me.projectx.settlements.events;

import me.projectx.settlements.Main;
import me.projectx.settlements.enums.ClaimType;
import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.MapManager;
import me.projectx.settlements.models.ClaimedChunk;
import me.projectx.settlements.models.Settlement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(final PlayerMoveEvent e){
		if (e.getFrom().getChunk() != e.getTo().getChunk()){
	/*From*/ClaimedChunk c = ChunkManager.getManager().getChunk(e.getFrom().getChunk().getX(), e.getFrom().getChunk().getZ(), e.getFrom().getChunk().getWorld());
	/*To*/	ClaimedChunk d = ChunkManager.getManager().getChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ(), e.getTo().getChunk().getWorld());
			if(c==null&&d==null){
			}
			else if(c != null && d == null){ //Leaving b to non claimed
				e.getPlayer().sendMessage(ChatColor.GREEN + "~Wilderness");
			}
			else if(d != null && c== null){ //Entering claimed land from unclaimed
				Settlement a = d.getSettlement();		
				switch(d.getType()){
					case NORMAL:
						e.getPlayer().sendMessage(ChatColor.AQUA + a.getName() + ChatColor.RED + " ~ " + ChatColor.GRAY + a.getDescription());
						break;
					case SAFEZONE:
						e.getPlayer().sendMessage(ChatColor.GOLD + "SafeZone ~ Safe from PvP and Monsters");
						break;
					default:
						break;
				}
			}else if(c != null && d != null){ //Entering one claim to another claim
				Settlement a = c.getSettlement();
				Settlement b = d.getSettlement();

				if (c.getType() == ClaimType.SAFEZONE && d.getType() != ClaimType.SAFEZONE){
					e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Leaving " + ChatColor.GOLD 
							+ "SafeZone" + ChatColor.DARK_AQUA + ", entering " + ChatColor.AQUA + b.getName());
				}

				if (a != null && b!= null){
					if(a.getId() != b.getId() && d.getType() == ClaimType.NORMAL){ 
						e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Leaving " + ChatColor.AQUA 
								+ a.getName() + ChatColor.DARK_AQUA + " entering " + ChatColor.AQUA + b.getName());
					}
				}
				else if (c.getType() == ClaimType.NORMAL && d.getType() == ClaimType.SAFEZONE) {
					e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Entering " + ChatColor.GOLD + "SafeZone");
				}
			} //add for safezone & battleground
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), new Runnable(){

				@Override
				public void run() {
					MapManager.getInstance().remove(e.getPlayer());
				}
			},1);
		}

		if (ChunkManager.getManager().isAutoClaiming(e.getPlayer())){
			Chunk c = e.getPlayer().getLocation().getChunk();
			if (!ChunkManager.getManager().isClaimed(c.getX(), c.getZ(), c.getWorld())){
				ChunkManager.getManager().claim(e.getPlayer(), ChunkManager.getManager().getAutoclaimType(e.getPlayer()));
			}
		}
	}
}
