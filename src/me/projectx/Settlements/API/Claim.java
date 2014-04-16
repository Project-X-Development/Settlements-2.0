package me.projectx.Settlements.API;

import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.World;

public class Claim {
	private Settlement s;
	private int x, z;
	private World w;
	private HashMap<Settlement, Chunk> claim = new HashMap<Settlement, Chunk>();
	
	public Claim(Chunk c, Settlement s) {
		this.s = s;
		this.x = c.getX();
		this.z = c.getZ();
		this.w = c.getWorld();
	}
	
	
}
