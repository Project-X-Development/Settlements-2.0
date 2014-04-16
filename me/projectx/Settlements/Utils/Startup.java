package me.projectx.Settlements.Utils;

import src.me.projectx.Settlements.Main;
import src.me.projectx.Settlements.Commands.CommandSettlementPlayer;
import src.me.projectx.Settlements.Events.PlayerChat;
import src.me.projectx.Settlements.Events.PlayerJoin;
import src.me.projectx.Settlements.Events.PlayerQuit;

public class Startup extends Thread {

	public static void runStartup(){
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new PlayerJoin(), Main.getInstance());
		pm.registerEvents(new PlayerQuit(), Main.getInstance());
		pm.registerEvents(new PlayerChat(), Main.getInstance());

		Main.getInstance().getCommand("s").setExecutor(new CommandSettlementPlayer());

		loadSettlements();

		CommandType.prepareCommandList();

		//Setup database
		DatabaseUtils.setupConnection();

	}

	private static void loadSettlements() {
		new Thread() {
			@Override
			public void run() {
				//TODO: loop through names in db & do something like "Settlement s = new Settlement(fromDB.toString())"
			}
		}.start();
	}
}
