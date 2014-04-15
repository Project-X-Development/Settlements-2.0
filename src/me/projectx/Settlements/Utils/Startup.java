package me.projectx.Settlements.Utils;

import me.projectx.Settlements.Main;
import me.projectx.Settlements.Commands.*;
import me.projectx.Settlements.Events.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Startup extends Thread {
	
	public static void runStartup(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerJoin(), Main.getInstance());
		pm.registerEvents(new PlayerQuit(), Main.getInstance());
		pm.registerEvents(new PlayerChat(), Main.getInstance());
		
		Main.getInstance().getCommand("s").setExecutor(new CommandSettlementPlayer());
		
		loadSettlements();
		
		CommandType.prepareCommandList();
	}
	
	private static void loadSettlements() {
		new Thread() {
			public void run() {
				//loop through names in db & do something like "Settlement s = new Settlement(fromDB.toString())"
			}
		}.start();
	}
}
