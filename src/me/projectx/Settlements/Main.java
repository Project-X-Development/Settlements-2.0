package me.projectx.Settlements;

import java.sql.SQLException;

import me.projectx.Settlements.Utils.DatabaseUtils;
import me.projectx.Settlements.Utils.Startup;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main plugin;

	@Override
	public void onEnable(){
		plugin = this;	
		saveDefaultConfig();
		try {
			Startup.runStartup();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable(){
		DatabaseUtils.closeConnection();
		plugin = null;
	}

	public static Main getInstance(){
		return plugin;
	} 
}
