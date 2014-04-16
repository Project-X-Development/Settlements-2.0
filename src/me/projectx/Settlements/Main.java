package me.projectx.Settlements;

import java.sql.SQLException;

import me.projectx.Settlements.Utils.Startup;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main plugin;
	
	public void onEnable(){
		plugin = this;	
		try {
			Startup.runStartup();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		saveDefaultConfig();
	}
	
	public void onDisable(){
		
		plugin = null;
	}
	
	public static Main getInstance(){
		return plugin;
	} 
}
