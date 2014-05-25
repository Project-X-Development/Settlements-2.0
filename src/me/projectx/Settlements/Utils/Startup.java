package me.projectx.Settlements.Utils;

import java.sql.SQLException;

import me.projectx.Settlements.Main;
import me.projectx.Settlements.Commands.CommandSettlementAdmin;
import me.projectx.Settlements.Commands.CommandSettlementPlayer;
import me.projectx.Settlements.Events.BlockEvent;
import me.projectx.Settlements.Events.MapInitialize;
import me.projectx.Settlements.Events.PlayerChat;
import me.projectx.Settlements.Events.PlayerDamage;
import me.projectx.Settlements.Events.PlayerDeath;
import me.projectx.Settlements.Events.PlayerJoin;
import me.projectx.Settlements.Events.PlayerMove;
import me.projectx.Settlements.Events.PlayerQuit;
import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Managers.SettlementManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
//import me.projectx.Settlements.Scoreboard.NameBoard;

public class Startup extends Thread {

	public static void runStartup() throws SQLException{
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new BlockEvent(), Main.getInstance());
		pm.registerEvents(new MapInitialize(), Main.getInstance());
		pm.registerEvents(new PlayerChat(), Main.getInstance());
		pm.registerEvents(new PlayerDamage(), Main.getInstance());
		pm.registerEvents(new PlayerDeath(), Main.getInstance());
		pm.registerEvents(new PlayerJoin(), Main.getInstance());
		pm.registerEvents(new PlayerQuit(), Main.getInstance());
		pm.registerEvents(new PlayerMove(), Main.getInstance());

		Main.getInstance().getCommand("s").setExecutor(new CommandSettlementPlayer());
		Main.getInstance().getCommand("sa").setExecutor(new CommandSettlementAdmin());

		CommandType.prepareCommandList();

		DatabaseUtils.setupConnection();
		DatabaseUtils.setupMySQL();

		loadSettlements();

		new ChunkManager(); 
		ChunkManager.getInstance().loadChunks();

		//new NameBoard();
	}

	private static void loadSettlements() {
		new Thread() {
			@Override
			public void run() {
				try {
					SettlementManager.getManager().loadSettlmentsFromDB();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
