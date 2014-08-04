package me.projectx.settlements.managers;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.models.Players;
import me.projectx.settlements.utils.RenderMap;

public class MapManager {

	private static MapManager mm = new MapManager();
	private Map<String, RenderMap> players = new HashMap<String, RenderMap>();

	public static MapManager getInstance(){
		return mm;
	}


	@SuppressWarnings("deprecation")
	public void add(Player pl){
		MapView m = Bukkit.getServer().createMap(Bukkit.getWorlds().get(0));
		for(org.bukkit.map.MapRenderer r : m.getRenderers()){
			m.removeRenderer(r);
		}
		RenderMap rm = new RenderMap();
		rm.setMapID(m.getId());
		rm.setUUID(pl.getUniqueId().toString());
		m.addRenderer(rm);
		players.put(pl.getUniqueId().toString(), rm);
		System.out.print("Added " + pl.getName());
	}

	public void remove(Player pl){
		File f = new File(Bukkit.getWorldContainer(), Bukkit.getWorlds().get(0).getName() + "/data/map_" + getPlayerMapID(pl));
		f.delete();
		players.remove(pl.getUniqueId().toString());
	}

	public void refresh(Player pl){
		players.get(pl.getUniqueId().toString()).setSeen(false);
	}
	
	public int getPlayerMapID(Player pl){
		return players.get(pl.getUniqueId().toString()).getMapID();
	}

	public void givePlayerMap(Player pl){
		MapManager.getInstance().refresh(pl);
		ItemStack item = new ItemStack(Material.MAP, 1, (short)getPlayerMapID(pl));
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.RED + "Dab " + ChatColor.BLUE + "Maps");
		im.setLore(Arrays.asList(new String[]{ChatColor.RED + pl.getName()}));
		item.setItemMeta(im);
		if (!pl.getInventory().contains(item)){
			pl.getInventory().addItem(item);
			pl.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "You have been issued a Settlement map");
		}else
			pl.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.YELLOW + "Derp, you already have a map. Check your inventory again.");
	}

	public void increment(Player pl){
		Players p = PlayerManager.getInstance().getPlayer(pl);
		int i = p.getInt("map");
		switch(i){
		case 1:
			p.setInt("map", 3);
			refresh(pl);
			break;
		case 3:
			p.setInt("map", 5);
			refresh(pl);
			break;
		case 5:
			p.setInt("map", 7);
			refresh(pl);
			break;
		case 7:
			p.setInt("map", 9);
			refresh(pl);
			break;
		case 9:
			p.setInt("map", 11);
			refresh(pl);
			break;
		case 11:
			p.setInt("map", 13);
			refresh(pl);
			break;
		case 13:
			p.setInt("map", 17);
			refresh(pl);
			break;
		case 17:
			p.setInt("map", 25);
			refresh(pl);
			break;
		case 25:
			p.setInt("map", 41);
			refresh(pl);
			break;
		case 41:
			p.setInt("map", 125);
			refresh(pl);	
			break;		
		}
	}

	public void decrement(Player pl){
		Players p = PlayerManager.getInstance().getPlayer(pl);
		int i = p.getInt("map");
		switch(i){
		case 3:
			p.setInt("map", 1);
			refresh(pl);
			break;
		case 5:
			p.setInt("map", 3);
			refresh(pl);
			break;
		case 7:
			p.setInt("map", 5);
			refresh(pl);
			break;
		case 9:
			p.setInt("map", 7);
			refresh(pl);
			break;
		case 11:
			p.setInt("map", 9);
			refresh(pl);
			break;
		case 13:
			p.setInt("map", 11);
			refresh(pl);
			break;
		case 17:
			p.setInt("map", 13);
			refresh(pl);
			break;
		case 25:
			p.setInt("map", 17);
			refresh(pl);
			break;
		case 41:
			p.setInt("map", 25);
			refresh(pl);
			break;
		case 125:
			p.setInt("map", 41);
			refresh(pl);
			break;
		}
	}

}
