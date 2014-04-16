package me.projectx.Settlements;

import me.projectx.Settlements.Utils.Startup;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main plugin;
	
	public void onEnable(){
		plugin = this;	
		Startup.runStartup();
		saveDefaultConfig();
	}
	
	public void onDisable(){
		plugin = null;
	}
	
	public static Main getInstance(){
		return plugin;
	} 
}
