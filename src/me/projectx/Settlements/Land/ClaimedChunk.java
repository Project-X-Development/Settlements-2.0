package me.projectx.Settlements.Land;

import java.util.ArrayList;
import java.util.List;

import me.projectx.Settlements.API.Settlement;

public class ClaimedChunk {

	public static List<ClaimedChunk> instances = new ArrayList<ClaimedChunk>(); 
	private final int x ;
	private final int z;
	private String owner;
	private Settlement set;

	public ClaimedChunk(int x, int z, String owner, Settlement set){
		this.x = x;
		this.z = z;
		this.owner = owner;
		this.set = set;
	}


	//Static methods//

	public static List<ClaimedChunk> getInstances(){
		return instances;
	}

	public static ClaimedChunk getChunk(int chunkx, int chunkz){
		for (ClaimedChunk tempChunk : instances){
			if (tempChunk.getX() == chunkx && tempChunk.getZ() == chunkz){
				return tempChunk;
			}
		}
		return null;
	}

	public static boolean isClaimed(int chunkx, int chunkz){
		for (ClaimedChunk tempChunk : instances){
			if (tempChunk.getX() == chunkx && tempChunk.getZ() == chunkz){
				return true;
			}
		}
		return false;
	}

	//Local methods//

	public int getX(){
		return this.x;
	}

	public int getZ(){
		return this.z;
	}

	public String getOwner(){
		return owner;
	}

	public void setOwner(String owner){
		this.owner = owner;
	}

	public Settlement getSettlement(){
		return set;
	}

	public void setSettlement(Settlement set){
		this.set = set;
	}

}
