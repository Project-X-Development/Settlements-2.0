package me.projectx.Settlements.Land;

import me.projectx.Settlements.API.Settlement;
import me.projectx.Settlements.API.SettlementManager;

import org.bukkit.World;

public class ChunkManager {

	public static ChunkManager instance;

	public ChunkManager(){
		instance = this;
	}

	public static ChunkManager getInstance(){
		return instance;
	}

	//Temporary return value, eventually will be an enum
	public int claimChunk(String player, int x, int z, World world){
		if (SettlementManager.getManager().isCitizenOfSettlement(player)){
			Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
			if (!ClaimedChunk.isClaimed(x, z)){
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
		if (SettlementManager.getManager().isCitizenOfSettlement(player)){
			Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
			if (!ClaimedChunk.isClaimed((int) x, (int) z)){
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
		if (ClaimedChunk.isClaimed(x, z)){
			ClaimedChunk chunk = ClaimedChunk.getChunk(x, z);
			ClaimedChunk.getInstances().remove(chunk);
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

}
