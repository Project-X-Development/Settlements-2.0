package me.projectx.Settlements.API;

import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.World;

/*
 * 
 * TODO Class moved to me.projectx.Settlements.Land.ClaimedChunk
 * Modifications here won't do anything.
 * 
 */

public class Claim {
	private final Settlement s;
	private final int x, z;
	private final World w;
	private final HashMap<Settlement, Chunk> claim = new HashMap<Settlement, Chunk>();

	public Claim(Chunk c, Settlement s) {
		this.s = s;
		this.x = c.getX();
		this.z = c.getZ();
		this.w = c.getWorld();
	}


}
