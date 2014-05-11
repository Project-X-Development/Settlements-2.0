package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.ClaimedChunk;
import me.projectx.Settlements.Utils.ClaimType;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockEvent implements Listener{

	/*
	 * TODO
	 * Add exceptions to these if 2 settlements are at war
	 */

	@EventHandler
	public void onBreak(final BlockBreakEvent e){
		Chunk c = e.getBlock().getLocation().getChunk();
		int x = c.getX();
		int z = c.getZ();

		if (ChunkManager.getInstance().isClaimed(x, z)){
			ClaimedChunk chunk = ChunkManager.getInstance().getChunk(x, z);
			Player p = Bukkit.getServer().getPlayer(chunk.getOwner());
			if (chunk.getType() == ClaimType.SAFEZONE){
				e.setCancelled(true);
				e.getPlayer().sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GOLD + "You cannot place blocks in a SafeZone");
				return;
			}
			if (!SettlementManager.getManager().getPlayerSettlement(e.getPlayer().getName()).isInWar()){
				if (p != e.getPlayer()){
					e.setCancelled(true);
					e.getPlayer().sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "You are not allowed to build here!");;
				}
			}
		}
	}

	@EventHandler
	public void onPlace(final BlockPlaceEvent e){
		Chunk c = e.getBlock().getLocation().getChunk();
		int x = c.getX();
		int z = c.getZ();

		if (ChunkManager.getInstance().isClaimed(x, z)){
			ClaimedChunk chunk = ChunkManager.getInstance().getChunk(x, z);
			Player p = Bukkit.getServer().getPlayer(chunk.getOwner());
			if (chunk.getType() == ClaimType.SAFEZONE){
				e.setCancelled(true);
				e.getPlayer().sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GOLD + "You cannot place blocks in a SafeZone");
				return;
			}
			if (!SettlementManager.getManager().getPlayerSettlement(e.getPlayer().getName()).isInWar()){
				if (p != e.getPlayer()){
					e.setCancelled(true);
					e.getPlayer().sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "You are not allowed to build here!");;
				}
			}

		}


	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			Chunk c = e.getClickedBlock().getLocation().getChunk();
			int x = c.getX();
			int z = c.getZ();

			if (ChunkManager.getInstance().isClaimed(x,  z)){
				ClaimedChunk chunk = ChunkManager.getInstance().getChunk(x, z);
				Player p = Bukkit.getServer().getPlayer(chunk.getOwner());
				if (!SettlementManager.getManager().getPlayerSettlement(e.getPlayer().getName()).isInWar()){
					if (!(p == e.getPlayer())){
						e.setCancelled(true);
						e.getPlayer().sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "You cannot interact with blocks in this territory!");
					}
				}
			}
		}
	}
}
