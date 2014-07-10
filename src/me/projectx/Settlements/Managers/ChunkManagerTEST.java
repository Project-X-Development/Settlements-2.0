package me.projectx.Settlements.Managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.projectx.Settlements.Main;
import me.projectx.Settlements.Models.ClaimedChunkTEST;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.ClaimType;
import me.projectx.Settlements.Utils.DatabaseUtils;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkManagerTEST {
	
	private Map<String, List<ClaimedChunkTEST>> setClaims = new HashMap<String, List<ClaimedChunkTEST>>();
	private List<ClaimedChunkTEST> claimedChunks = new ArrayList<ClaimedChunkTEST>();
	private List<String> autoClaim = new ArrayList<String>();
	private final int BASE_CHUNK_COST = 50;
	private static ChunkManagerTEST cm = new ChunkManagerTEST();
	
	public static ChunkManagerTEST getManager(){
		return cm;
	}
	
	public int claimChunk(final String owner, final World world, final int x, final int z, final ClaimType type){
		final Settlement s = SettlementManager.getManager().getPlayerSettlement(owner);
		if (s != null){
			if (!isClaimed(x, z)){
				new BukkitRunnable(){
					public void run(){
						ClaimedChunkTEST cc = new ClaimedChunkTEST(x, z, owner, s, world, type);
						claimedChunks.add(cc);
						
						/*
						 * Charge the base chunk cost and tack on an additional amount for the amount of chunks they already own.
						 * More chunks = higher cost
						 */
						EconomyManager.getManager().withdrawFromSettlement(s, BASE_CHUNK_COST + setClaims.get(s).size());
						
						if (!setClaims.containsKey(s.getName())){
							List<ClaimedChunkTEST> claims = new ArrayList<ClaimedChunkTEST>();
							claims.add(cc);
							setClaims.put(s.getName(), claims);
						}else{
							setClaims.get(s.getName()).add(cc);
						}
					}
				}.runTaskAsynchronously(Main.getInstance());
				return 2;
			}else
				return 1;	
		}else
			return 0;
	}
	
	public void unclaimChunk(final String player, final int x, final int z){
		if (isClaimed(x, z)){
			new BukkitRunnable(){
				public void run(){
					ClaimedChunkTEST cc = getChunk(x, z);
					if (player != null){
						Settlement s = SettlementManager.getManager().getPlayerSettlement(player);
						if (cc.getSettlement().getName().equals(s.getName())){
							if (setClaims.containsKey(s.getName())){
								List<ClaimedChunkTEST> list = setClaims.get(s.getName());
								if (list.contains(cc)){
									list.remove(cc);
									claimedChunks.remove(cc);
								}
							}
						}
					}else{
						if (setClaims.containsKey(cc.getSettlement().getName())){
							List<ClaimedChunkTEST> list = setClaims.get(cc.getSettlement().getName());
							if (list.contains(cc)){
								list.remove(cc);
								claimedChunks.remove(cc);
							}
						}
					}
				}
			}.runTaskAsynchronously(Main.getInstance());
		}
	}
	
	public boolean isClaimed(int x, int z){
		for (ClaimedChunkTEST cc : claimedChunks){
			if (cc.getX() == x && cc.getZ() == z){
				return true;
			}
		}
		return false;
	}
	
	public ClaimedChunkTEST getChunk(int x, int z){
		for (ClaimedChunkTEST cc : claimedChunks){
			if (cc.getX() == x && cc.getZ() == z){
				return cc;
			}
		}
		return null;
	}
	
	public List<ClaimedChunkTEST> getClaims(Settlement s){
		return setClaims.get(s.getName());
	}
	
	public void loadChunks(){
		new BukkitRunnable(){
			public void run() {
				try {
					ResultSet result = DatabaseUtils.queryIn("SELECT * FROM chunks;");
					while (result.next()){
						int x = result.getInt("x");
						int z = result.getInt("z");
						String player = result.getString("player");
						long setid = result.getLong("settlement");
						String w = result.getString("world");
						new ClaimedChunkTEST(x,	z , player, SettlementManager.getManager().getSettlement(setid) , Bukkit.getWorld(w), ClaimType.valueOf(result.getString("type")));
					}	
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.getInstance());
	}
	
	public void claim(Player player, ClaimType ct){	
		int i = 0;
		Chunk c = player.getLocation().getChunk();
		
		switch(ct){
			case NORMAL:
				i = claimChunk(player.getName(), player.getWorld(), c.getX(), c.getZ(), ClaimType.NORMAL);
				break;
			case SAFEZONE:
				i = claimChunk(null, player.getWorld(), c.getX(), c.getZ(), ClaimType.SAFEZONE);
				break;
			default:
				break;
		}	
		
		switch(i){
			case 2:
				if (ct == ClaimType.SAFEZONE)
					player.sendMessage(MessageType.CHUNK_CLAIM_SAFEZONE.getMsg());
				else if (ct == ClaimType.NORMAL)
					player.sendMessage(MessageType.CHUNK_CLAIM_SUCCESS.getMsg());
				break;
			case 1:
				player.sendMessage(MessageType.CHUNK_CLAIM_OWNED.getMsg());
				break;
			case 0:
				player.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
				break;
		}

	}
	
	public void setAutoClaiming(Player p){ //TODO Add different ClaimTypes
		if (autoClaim.contains(p.getName())) {
			autoClaim.remove(p.getName());
			p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "No longer auto-claiming land for your Settlement");
		} else {
			autoClaim.add(p.getName());
			p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Now auto-claiming land for your Settlement");
		}
	}

	public boolean isAutoClaiming(Player p){
		return autoClaim.contains(p.getName());
	}
}
