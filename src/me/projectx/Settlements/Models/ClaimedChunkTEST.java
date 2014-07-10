package me.projectx.Settlements.Models;

import me.projectx.Settlements.Utils.ClaimType;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class ClaimedChunkTEST {

	private final int x, z;
	private World w;
	private String owner;
	private Settlement set;
	private ClaimType ct;

	public ClaimedChunkTEST(int x, int z, String owner, Settlement set, World w, ClaimType ct){
		this.x = x;
		this.z = z;
		this.w = w;
		this.owner = owner;
		this.set = set;
		this.ct = ct;
	}

	public int getX(){
		return this.x;
	}

	public int getZ(){
		return this.z;
	}

	public World getWorld(){
		return this.w;
	}

	public void setWorld(String world){
		this.w = Bukkit.getWorld(world);
	}

	public void setWorld(World world){
		this.w = world;
	}

	public String getOwner(){
		return this.owner;
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
	
	public ClaimType getType(){
		return this.ct;
	}
	
	public void setType(ClaimType ct){
		this.ct = ct;
	}

	public void deleteChunk() throws Throwable{
		this.finalize();
	}
}
