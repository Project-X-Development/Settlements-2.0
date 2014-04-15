package me.projectx.Settlements.Utils;

import me.projectx.Settlements.Main;
import me.projectx.Settlements.Events.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Startup {
	
	public static void runStartup(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerJoin(), Main.getInstance());
		pm.registerEvents(new PlayerQuit(), Main.getInstance());
		
		//Add commands
	}
}
