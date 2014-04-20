package me.projectx.Settlements.Managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.projectx.Settlements.Models.ClaimedChunk;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.DatabaseUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ChunkManager extends Thread{
	public static ChunkManager instance;
	public HashMap<Settlement, List<ClaimedChunk>> map = new HashMap<Settlement, List<ClaimedChunk>>();
	private final ArrayList<String> autoClaim = new ArrayList<String>();

	public ChunkManager(){
		instance = this;
	}

	public static ChunkManager getInstance(){
		return instance;
	}

	//Temporary return value, eventually will be an enum
	public int claimChunk(final String player, final int x, final int z, final World world) throws SQLException{
		if (!(SettlementManager.getManager().getPlayerSettlement(player) == null)){
			final Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
			if (!isClaimed(x, z)){
				new Thread() {
					@Override
					public void run() {
						ClaimedChunk c = new ClaimedChunk(x, z, player, set, world);
						long setid = set.getId();
						String w = c.getWorld().getName();
						try {
							DatabaseUtils.queryOut("INSERT INTO chunks(x, z, player, settlement, world) VALUES('"
									+ x + "', '" + z + "','" + player
									+ "','"+ setid +"', '"+ w +"');");
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

	//Temporary return value, eventually will be an enum
	public int claimChunk(final String player, final double x, final double z, final World world) throws SQLException {
		if (!(SettlementManager.getManager().getPlayerSettlement(player) == null)){
			final Settlement set = SettlementManager.getManager().getPlayerSettlement(player);
			if (!isClaimed((int) x, (int) z)){
				new Thread() {
					@Override
					public void run() {
						ClaimedChunk c = new ClaimedChunk((int)x, (int)z, player, set, world);
						long setid = set.getId();
						String w = c.getWorld().getName();
						try {
							DatabaseUtils.queryOut("INSERT INTO chunks(x, z, player, settlement, world) VALUES('"
									+ x + "', '" + z + "','" + player
									+ "','"+ setid +"', '"+ w +"');");
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

	public boolean unclaimChunk(int x, int z) throws SQLException{
		if (isClaimed(x, z)){
			final ClaimedChunk chunk = getChunk(x, z);
			final Settlement set = chunk.getSettlement();
			new Thread() {
				@Override
				public void run() {
					try {
						DatabaseUtils.queryOut("DELETE FROM settlements WHERE x=" + chunk.getX() + " AND z=" + chunk.getZ() + ";");
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

			return true;
		} else {
			return false;
		}
	}

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
				player.sendMessage("Temporary reference: you are always in the middle of the top row");
				Settlement set = SettlementManager.getManager().getPlayerSettlement(player.getName());
				for (int x = 0; x < 5; x++){
					String send = "";
					for (int z = -7; z < 8; z++){
						int xx = playerx + x;
						int zz = playerz + z;
						if (isClaimed(xx, zz)){ 
							/*if (xx == player.getLocation().getChunk().getX() && zz == player.getLocation().getChunk().getZ())
								send = send + ChatColor.YELLOW + "+";
							else*/
							if (getChunk(xx, zz).getSettlement() != set){
								send = send + ChatColor.GREEN + "+"; 
							}
							else{
								send = send + ChatColor.RED + "-"; 
							}
						} else {
							send = send + ChatColor.BLUE + "-";
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
						new ClaimedChunk(x,	z , player, SettlementManager.getManager().getSettlement(setid) , Bukkit.getWorld(w));
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
		} else {
			autoClaim.add(p.getName());
		}
	}

	public boolean isAutoClaiming(Player p){
		return autoClaim.contains(p.getName());
	}
}
