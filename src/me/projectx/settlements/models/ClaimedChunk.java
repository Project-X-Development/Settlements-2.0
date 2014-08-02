package me.projectx.settlements.models;

import java.util.UUID;

import me.projectx.settlements.enums.ClaimType;
import me.projectx.settlements.managers.SettlementManager;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class ClaimedChunk {

	private final int x, z;
	public String world;
	private UUID owner;
	private long setId;
	private ClaimType ct;

	public ClaimedChunk(int x, int z, UUID owner, long setId, String w, ClaimType ct){
		this.x = x;
		this.z = z;
		this.world = w;
		this.owner = owner;
		this.setId = setId;
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

	public UUID getOwner(){
		return this.owner;
	}
	
	public void setOwner(UUID owner){
		this.owner = owner;
	}

	public Settlement getSettlement(){
		return SettlementManager.getManager().getSettlement(setId);
	}

	public void setSettlement(Settlement set){
		this.setId = set.getId();
	}
	
	public ClaimType getType(){
		return this.ct;
	}
	
	public void setType(ClaimType ct){
		this.ct = ct;
	}
}
