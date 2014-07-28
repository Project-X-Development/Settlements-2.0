package me.projectx.settlements.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.projectx.settlements.Main;

import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerUtils {
	
	private static Map<UUID, String> response = new HashMap<UUID, String>();
	
	public static String getStatus(OfflinePlayer p){
		if (p.isOnline())
			return "Online";
		else
			return "Offline";
	}
	
	public static String getNameFromUUID(final UUID id){
		new BukkitRunnable(){
			public void run(){
				NameFetcher nf = new NameFetcher(Arrays.asList(id));
				try {
					response = nf.call();
				} catch(Exception e) {
					e.printStackTrace();
				}		
			}
		}.runTaskAsynchronously(Main.getInstance());
		String name = response.get(id);
		return name;
	}
}
