package me.projectx.Settlements.Models;

import me.projectx.Settlements.enums.ClaimType;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class ClaimedChunk {

	private final int x, z;
	//private World w;
	private String owner, world;
	private Settlement set;
	private ClaimType ct;

	public ClaimedChunk(int x, int z, String owner, Settlement set, String w, ClaimType ct){
		this.x = x;
		this.z = z;
		this.world = w;
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
		return Bukkit.getWorld(world);
	}

	public void setWorld(String world){
		this.world = world;
	}

	public void setWorld(World world){
		this.world = world.getName();
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
}
