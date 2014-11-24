package me.projectx.settlements.events;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.projectx.settlements.Main;
import me.projectx.settlements.enums.ClaimType;
import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.ClaimedChunk;
import me.projectx.settlements.models.Settlement;
import me.projectx.settlements.utils.DatabaseUtils;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkLoad implements Listener {

	private ChunkManager cm = ChunkManager.getManager();
	private PreparedStatement select_chunks;

	public ChunkLoad(){
		try{
			select_chunks = DatabaseUtils.getConnection().prepareStatement("SELECT * FROM chunks WHERE x=? AND z=?;");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onLoad(ChunkLoadEvent e){
		final Chunk c = e.getChunk();
		new BukkitRunnable(){
			@Override
			public void run() {
				try {
					select_chunks.setInt(1, c.getX());
					select_chunks.setInt(2, c.getZ());
					ResultSet result = DatabaseUtils.queryIn(select_chunks);
					int x = result.getInt("x");
					int z = result.getInt("z");
					UUID player = null;
					if (!result.getString("player").equals("null")){
						player = UUID.fromString(result.getString("player"));
					}
					long setid = result.getLong("settlement");
					String w = result.getString("world");
					Settlement s = SettlementManager.getManager().getSettlement(setid);
					ClaimedChunk cc = new ClaimedChunk(x, z, player, setid, w, ClaimType.valueOf(result.getString("type")));
					cm.claimedChunks.add(cc);
					if (s != null){
						cm.setClaims.get(s.getName()).add(cc);
					}else{
						if (cm.setClaims.containsKey(null)){
							cm.setClaims.get(null).add(cc);
						}else{
							List<ClaimedChunk> list = new ArrayList<ClaimedChunk>();
							list.add(cc);
							cm.setClaims.put(null, list);
						}
					}
					System.out.println("[Settlements] Loaded Chunk Type" + cc.getType() + " at x:" + cc.getX() + " z:" + cc.getZ() + " in the world " + cc.getWorld().getName());
				} catch(SQLException e) {
					e.printStackTrace();
				}
				this.cancel();
			}
		}.runTaskAsynchronously(Main.getInstance());
	}
}
