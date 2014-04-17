package me.projectx.Settlements.Land;

import me.projectx.Settlements.API.Settlement;
import me.projectx.Settlements.API.SettlementManager;

import org.bukkit.World;

public class ChunkManager {

	public static boolean claimChunk(String player, int x, int z, World world){
		if (SettlementManager.getManager().isCitizenOfSettlement(player)){
			Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
			new ClaimedChunk(x, z, player, set, world);
			return true;
		}
		else{
			return false;
		}
	}

	public static boolean unclaimChunk(int x, int z){
		if (ClaimedChunk.isClaimed(x, z)){
			ClaimedChunk chunk = ClaimedChunk.getChunk(x, z);
			ClaimedChunk.getInstances().remove(chunk);
			return true;
		}
		else{
			return false;
		}
	}

	public static ClaimedChunk changeChunkOwnership(ClaimedChunk chunk, String player){
		chunk.setOwner(player);
		Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
		chunk.setSettlement(set);
		return chunk;
	}
}
