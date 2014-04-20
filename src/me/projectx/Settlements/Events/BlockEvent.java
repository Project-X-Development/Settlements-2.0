package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Models.ClaimedChunk;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvent implements Listener{
	@EventHandler
	public void onBreak(final BlockBreakEvent e){
		Chunk c = e.getBlock().getLocation().getChunk();
		int x = c.getX();
		int z = c.getZ();

		if (ChunkManager.getInstance().isClaimed(x, z)){
			ClaimedChunk chunk = ChunkManager.getInstance().getChunk(x, z);
			Player p = Bukkit.getServer().getPlayer(chunk.getOwner());
			if (p != e.getPlayer()){
				e.setCancelled(true);
				p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "You are not allowed to build here!");;
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
			if (p != e.getPlayer()){
				e.setCancelled(true);
				p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "You are not allowed to build here!");;
			}
		}
	}
}
