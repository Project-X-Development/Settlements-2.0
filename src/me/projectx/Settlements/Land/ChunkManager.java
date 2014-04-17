package me.projectx.Settlements.Land;

import me.projectx.Settlements.API.Settlement;
import me.projectx.Settlements.API.SettlementManager;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ChunkManager extends Thread{

	/*
	 * TODO
	 * Multithread as much of this as possible, especially DB calls & loops
	 * 
	 */
	public static ChunkManager instance;

	public ChunkManager(){
		instance = this;
	}

	public static ChunkManager getInstance(){
		return instance;
	}

	//Temporary return value, eventually will be an enum
	public int claimChunk(String player, int x, int z, World world){
		if (!(SettlementManager.getManager().getPlayerSettlement(player) == null)){
			Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
			if (!isClaimed(x, z)){
				new ClaimedChunk(x, z, player, set, world);
				return 2;
			}
			else{
				return 1;
			}
		}
		else{
			return 0;
		}
	}

	//Temporary return value, eventually will be an enum
	public int claimChunk(String player, double x, double z, World world) {
		if (!(SettlementManager.getManager().getPlayerSettlement(player) == null)){
			Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
			if (!isClaimed((int) x, (int) z)){
				new ClaimedChunk((int) x, (int) z, player, set, world);
				return 2;
			}
			else{
				return 1;
			}
		}
		else{
			return 0;
		}
	}

	public boolean unclaimChunk(int x, int z){
		if (isClaimed(x, z)){
			ClaimedChunk chunk = getChunk(x, z);
			ClaimedChunk.getCC().getChunks().remove(chunk);
			return true;
		}
		else{
			return false;
		}
	}

	public ClaimedChunk changeChunkOwnership(ClaimedChunk chunk, String player){
		chunk.setOwner(player);
		Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
		chunk.setSettlement(set);
		return chunk;
	}

	public boolean isClaimed(int chunkx, int chunkz){
		for (ClaimedChunk tempChunk : ClaimedChunk.getCC().getChunks()){
			if (tempChunk.getX() == chunkx && tempChunk.getZ() == chunkz){
				return true;
			}
		}
		return false;
	}

	public static ClaimedChunk getChunk(int chunkx, int chunkz){
		for (ClaimedChunk tempChunk : ClaimedChunk.getCC().getChunks()){
			if (tempChunk.getX() == chunkx && tempChunk.getZ() == chunkz){
				return tempChunk;
			}
		}
		return null;
	}

	public void printMap(final Player player){
		final int playerx = player.getLocation().getChunk().getX();
		final int playerz = player.getLocation().getChunk().getZ();
		new Thread() {
			@Override
			public void run() {
				for (int x = -7; x < 7; x++){
					String send = null;
					for (int z = -7; z < 7; z++){
						if (isClaimed(playerx + x, playerz + z)){
							send = send + ChatColor.GREEN+"+";                                      
						}else{
							send = send + ChatColor.RED + "-";
						}
					}
					player.sendMessage(send);
				}	
			}
		}.start();
	}
}