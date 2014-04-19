package me.projectx.Settlements.Land;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	public HashMap<Settlement, List<ClaimedChunk>> map = new HashMap<Settlement, List<ClaimedChunk>>();

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
				ClaimedChunk c = new ClaimedChunk(x, z, player, set, world);
				if(!map.containsKey(set)){
					List<ClaimedChunk> l = new ArrayList<ClaimedChunk>();
					l.add(c);
					map.put(set, l);
				}else{
					List<ClaimedChunk> l = map.get(set);
					l.add(c);
					map.put(set, l);
				}
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
				ClaimedChunk c = new ClaimedChunk((int)x, (int)z, player, set, world);
				if(!map.containsKey(set)){
					List<ClaimedChunk> l = new ArrayList<ClaimedChunk>();
					l.add(c);
					map.put(set, l);
				}else{
					List<ClaimedChunk> l = map.get(set);
					l.add(c);
					map.put(set, l);
				}
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
			Settlement set = chunk.getSettlement();
			if(map.containsKey(set)){
				List<ClaimedChunk> cc = map.get(set);
				if(cc.contains(chunk)){
					cc.remove(chunk);
				}
				map.put(set, cc);
			}
			ClaimedChunk.instances.remove(chunk);
			return true;
		}
		else{
			return false;
		}
	}

	public ClaimedChunk changeChunkOwnership(ClaimedChunk chunk, String player){
		Settlement first = chunk.getSettlement();
		if(map.containsKey(first)){
			List<ClaimedChunk> cc = map.get(first);
			if(cc.contains(chunk)){
				cc.remove(chunk);
			}
			map.put(first, cc);
		}
		Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
		if(map.containsKey(set)){
			List<ClaimedChunk> cc = map.get(set);
			if(cc.contains(chunk)){
				cc.remove(chunk);
			}
			map.put(set, cc);
		}
		chunk.setOwner(player);
		chunk.setSettlement(set);
		return chunk;
	}

	public boolean isClaimed(int chunkx, int chunkz){
		for (ClaimedChunk tempChunk : ClaimedChunk.instances){
			if (tempChunk.getX() == chunkx && tempChunk.getZ() == chunkz){
				return true;
			}
		}
		return false;
	}

	public ClaimedChunk getChunk(int chunkx, int chunkz){
		for (ClaimedChunk tempChunk : ClaimedChunk.instances){
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
				player.sendMessage(ChatColor.GRAY + "-------------------" + ChatColor.DARK_GRAY + 
						" [" + ChatColor.AQUA + "Settlement Map" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "-------------------");
				player.sendMessage("Temporary reference: you are always in the middle of the top row");
				for (int x = 0; x < 5; x++){
					String send = "";
					for (int z = -7; z < 8; z++){
						int xx = playerx + x;
						int zz = playerz + z;
						if (isClaimed(xx, zz)){ 
							/*if (xx == player.getLocation().getChunk().getX() && zz == player.getLocation().getChunk().getZ())
								send = send + ChatColor.YELLOW + "+";
							else*/
								send = send + ChatColor.GREEN + "+"; 
						}else
							send = send + ChatColor.RED + "-";	
					}
					player.sendMessage(send);
				}	
			}
		}.start();
	}
	
	public boolean isInChunk(Player player){
		if (isClaimed(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ()))
			return true;
		return false;
	}
	
	public void sendInChunkMsg(Player player){
		if (isInChunk(player)){
			//player.sendMessage(); TODO need method to get the settlement that owns the chunk
			//Example msg: <Settlement Name> ~ <Settlement Description>
			for (ClaimedChunk cc : ClaimedChunk.instances){
				if (cc.getX() == player.getLocation().getChunk().getX() 
						&& cc.getZ() == player.getLocation().getChunk().getZ() && cc.getWorld() == player.getLocation().getWorld()){
					player.sendMessage(cc.getSettlement().getName() + " ~ " + cc.getSettlement().getDescription());
				}
			}
		}	
	}
}
