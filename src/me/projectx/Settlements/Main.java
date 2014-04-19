package me.projectx.Settlements;

import java.sql.SQLException;

import me.projectx.Settlements.Utils.DatabaseUtils;
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
		DatabaseUtils.closeConnection();
		plugin = null;
	}
	
	public static Main getInstance(){
		return plugin;
	} 
}
