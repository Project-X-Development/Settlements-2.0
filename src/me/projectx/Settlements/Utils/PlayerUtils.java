package me.projectx.Settlements.Utils;

import org.bukkit.entity.Player;

public class PlayerUtils {
	
	public static String getStatus(Player p){
		if (p.isOnline())
			return "Online";
		else
			return "Offline";
	}
	
}
