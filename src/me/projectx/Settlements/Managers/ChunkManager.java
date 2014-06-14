package me.projectx.Settlements.Managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.projectx.Settlements.Models.ClaimedChunk;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.ClaimType;
import me.projectx.Settlements.Utils.DatabaseUtils;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ChunkManager extends Thread{
	private static ChunkManager instance = new ChunkManager();
	public HashMap<Settlement, List<ClaimedChunk>> map = new HashMap<Settlement, List<ClaimedChunk>>();
	private final ArrayList<String> autoClaim = new ArrayList<String>();

	public static ChunkManager getInstance(){
		return instance;
	}

	//Temporary return value, eventually will be an enum
	public int claimChunk(final String player, final int x, final int z, final World world, final ClaimType ct) throws SQLException{
		if (player != null){
			if (!(SettlementManager.getManager().getPlayerSettlement(player) == null)){
				final Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
				if (!isClaimed(x, z)){
					new Thread() {
						@Override
						public void run() {
							ClaimedChunk c = new ClaimedChunk(x, z, player, set, world, ct);
							long setid = set.getId();
							String w = world.getName();
							try {
								DatabaseUtils.queryOut("INSERT INTO chunks(x, z, player, settlement, world, type) VALUES('"
										+ x + "', '" + z + "','" + player + "','" + setid +"', '" + w + "','" + ct + "');");
							} catch(SQLException e) {
								e.printStackTrace();
							}

							if(!map.containsKey(set)){
								List<ClaimedChunk> l = new ArrayList<ClaimedChunk>();
								l.add(c);
								map.put(set, l);
							}else{
								List<ClaimedChunk> l = map.get(set);
								l.add(c);
								map.put(set, l);
							}
						}
					}.start();
					return 2;
				} else {
					return 1;
				}
			} else {
				return 0;
			}
		}
		else {
			if (!isClaimed(x, z)){
				final Settlement set = null;
				new Thread() {
					@Override
					public void run() {
						ClaimedChunk c = new ClaimedChunk(x, z, "*SERVER*", set, world, ct);
						String w = world.getName();
						try {
							DatabaseUtils.queryOut("INSERT INTO chunks(x, z, player, settlement, world, type) VALUES('"
									+ x + "', '" + z + "','" + null + "','" + -1 +"', '" + w + "','" + ct + "');");
						} catch(SQLException e) {
							e.printStackTrace();
						}

						if(!map.containsKey(set)){
							List<ClaimedChunk> l = new ArrayList<ClaimedChunk>();
							l.add(c);
							map.put(set, l);
						}else{
							List<ClaimedChunk> l = map.get(set);
							l.add(c);
							map.put(set, l);
						}
					}
				}.start();
				return 2;
			} else {
				return 1;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public boolean unclaimChunk(String player, int x, int z) throws SQLException{
		if (isClaimed(x, z)){
			if (getChunk(x, z).getSettlement().getName() == SettlementManager.getManager().getPlayerSettlement(player).getName()){
				final ClaimedChunk chunk = getChunk(x, z);
				final Settlement set = chunk.getSettlement();
				new Thread() {
					@Override
					public void run() {
						try {
							DatabaseUtils.queryOut("DELETE FROM chunks WHERE x=" + chunk.getX() + " AND z=" + chunk.getZ() + ";");
						} catch(SQLException e) {
							e.printStackTrace();
						}

						if(map.containsKey(set)){
							List<ClaimedChunk> cc = map.get(set);
							if(cc.contains(chunk)){
								cc.remove(chunk);
							}
							map.put(set, cc);
						}
						ClaimedChunk.instances.remove(chunk);

					}
				}.start();
			} else {
				Bukkit.getPlayer(player).sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + 
						"You cannot claim land from " + ChatColor.YELLOW + getChunk(x, z).getSettlement().getName());
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean unclaimChunk(int x, int z) throws SQLException{
		if (isClaimed(x, z)){
			final ClaimedChunk chunk = getChunk(x, z);
			final Settlement set = chunk.getSettlement();
			new Thread() {
				@Override
				public void run() {
					try {
						DatabaseUtils.queryOut("DELETE FROM chunks WHERE x=" + chunk.getX() + " AND z=" + chunk.getZ() + ";");
					} catch(SQLException e) {
						e.printStackTrace();
					}

					if(map.containsKey(set)){
						List<ClaimedChunk> cc = map.get(set);
						if(cc.contains(chunk)){
							cc.remove(chunk);							
							map.put(set, cc);
						}
					}
					ClaimedChunk.instances.remove(chunk);
				}
			}.start();
			return true;
		} else {
			return false;
		}
	}
	//add check for safezone & battleground
	public ClaimedChunk changeChunkOwnership(final ClaimedChunk chunk, final String player) throws SQLException{
		new Thread() {
			@Override
			public void run() {
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

				try {
					DatabaseUtils.queryOut("UPDATE chunks SET player='" + player + "', settlement='" + set.getId() + "' WHERE x=" + chunk.getX() + " AND z=" + chunk.getZ() + ";");
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();

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
			if (tempChunk.getX() == chunkx && tempChunk.getZ() == chunkz) {
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
				player.sendMessage("Reference: 'P' is your current location");
				Settlement set = SettlementManager.getManager().getPlayerSettlement(player.getName());
				for (int x = 0; x < 5; x++){
					String send = "";
					for (int z = -7; z < 8; z++){
						int xx = playerx + x;
						int zz = playerz + z;
						if (xx == player.getLocation().getChunk().getX() && zz == player.getLocation().getChunk().getZ()) {
							if (isClaimed(xx, zz)){ 
								if (getChunk(xx, zz).getType() == ClaimType.NORMAL && getChunk(xx, zz).getSettlement() == set){
									send = send + ChatColor.GREEN + "P"; 
								}
								else if (getChunk(xx, zz).getType() == ClaimType.SAFEZONE){
									send = send + ChatColor.GOLD + "P"; 
								}
								else if (getChunk(xx, zz).getType() == ClaimType.NORMAL && getChunk(xx, zz).getSettlement() != set){
									send = send + ChatColor.RED + "P";
								}
							} else {
								send = send + ChatColor.BLUE + "P";
							}	
						} else{
							if (isClaimed(xx, zz)){ 
								if (getChunk(xx, zz).getType() == ClaimType.NORMAL && getChunk(xx, zz).getSettlement() == set){
									send = send + ChatColor.GREEN + "+"; 
								}
								else if (getChunk(xx, zz).getType() == ClaimType.SAFEZONE){
									send = send + ChatColor.GOLD + "-"; 
								}
								
								else if (getChunk(xx, zz).getType() == ClaimType.NORMAL && getChunk(xx, zz).getSettlement() != set){
									send = send + ChatColor.RED + "-";
								}
							} else {
								send = send + ChatColor.BLUE + "=";
							}	
						}
					}
					player.sendMessage(send);
				}	
			}
		}.start();
	}

	public boolean isInChunk(Player player){
		if (isClaimed(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ())) {
			return true;
		}
		return false;
	}

	public void loadChunks() throws SQLException{
		new Thread() {
			@Override
			public void run() {
				try {
					ResultSet result = DatabaseUtils.queryIn("SELECT * FROM chunks;");
					while (result.next()){
						int x = result.getInt("x");
						int z = result.getInt("z");
						String player = result.getString("player");
						long setid = result.getLong("settlement");
						String w = result.getString("world");
						new ClaimedChunk(x,	z , player, SettlementManager.getManager().getSettlement(setid) , Bukkit.getWorld(w), ClaimType.valueOf(result.getString("type")));
					}	
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void setAutoClaiming(Player p){
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
	
	/**
	 * Claim a chunk at a given player's location
	 * 
	 * @param player : The player claiming the chunk
	 * @throws SQLException 
	 */
	public void claimChunk(Player player, ClaimType ct) throws SQLException{
		int status = ChunkManager.getInstance().claimChunk(player.getName(), 
				player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ(), player.getLocation().getWorld(), ct);
		if (status == 2) {
			player.sendMessage(MessageType.CHUNK_CLAIM_SUCCESS.getMsg());
		} else if (status == 1) {
			player.sendMessage(MessageType.CHUNK_CLAIM_OWNED.getMsg());
		} else if (status == 0) {
			player.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	public void claimSpecialChunk(Player player, ClaimType ct) throws SQLException{
		if (ct == ClaimType.SAFEZONE || ct == ClaimType.BATTLEGROUND){
			int status = ChunkManager.getInstance().claimChunk(null, player.getLocation().getChunk().getX(), 
					player.getLocation().getChunk().getZ(), player.getWorld(), ct);
			if (status == 2) {
				if (ct == ClaimType.SAFEZONE) {
					player.sendMessage(MessageType.CHUNK_CLAIM_SAFEZONE.getMsg());
				}
			}
		} else {
			player.sendMessage("DEBUG: You can only claim SafeZones and Battlegrounds with this command");
		}
	}
}
