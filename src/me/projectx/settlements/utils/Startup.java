package me.projectx.settlements.utils;

import java.sql.SQLException;

import me.projectx.settlements.Main;
import me.projectx.settlements.commands.CommandSettlementAdmin;
import me.projectx.settlements.commands.CommandSettlementPlayer;
import me.projectx.settlements.enums.CommandType;
import me.projectx.settlements.events.BlockEvent;
import me.projectx.settlements.events.InventoryClick;
import me.projectx.settlements.events.MapInitialize;
import me.projectx.settlements.events.PlayerChat;
import me.projectx.settlements.events.PlayerDamage;
import me.projectx.settlements.events.PlayerDeath;
import me.projectx.settlements.events.PlayerInteract;
import me.projectx.settlements.events.PlayerJoin;
import me.projectx.settlements.events.PlayerMove;
import me.projectx.settlements.events.PlayerQuit;
import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.EconomyManager;
import me.projectx.settlements.managers.MapManager;
import me.projectx.settlements.managers.PlayerManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.managers.WarManager;
import me.projectx.settlements.models.Players;
import me.projectx.settlements.runtime.SettlementRuntime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;


public class Startup {

	public static void runStartup() throws SQLException{
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new BlockEvent(), Main.getInstance());
		pm.registerEvents(new MapInitialize(), Main.getInstance());
		pm.registerEvents(new PlayerChat(), Main.getInstance());
		pm.registerEvents(new PlayerDamage(), Main.getInstance());
		pm.registerEvents(new PlayerDeath(), Main.getInstance());
		pm.registerEvents(new PlayerInteract(), Main.getInstance());
		pm.registerEvents(new PlayerJoin(), Main.getInstance());
		pm.registerEvents(new PlayerQuit(), Main.getInstance());
		pm.registerEvents(new PlayerMove(), Main.getInstance());
		pm.registerEvents(new InventoryClick(), Main.getInstance());

		Main.getInstance().getCommand("s").setExecutor(new CommandSettlementPlayer());
		Main.getInstance().getCommand("sa").setExecutor(new CommandSettlementAdmin());

		CommandType.prepareCommandList();

		DatabaseUtils.setupConnection();
		DatabaseUtils.setupMySQL();

		SettlementManager.getManager().loadSettlmentsFromDB();

		ChunkManager.getManager().loadChunks();

		WarManager.getInstance().loadWarsFromDB();

		for(Player p : Bukkit.getOnlinePlayers()){
			Players pl = PlayerManager.getInstance().addPlayer(p);
			pl.setInt("map", 25);
		}

		MapView m = Bukkit.getServer().getMap((short)0);
		for(org.bukkit.map.MapRenderer r : m.getRenderers()){
			m.removeRenderer(r);
		}
		m.addRenderer(MapManager.getInstance().getRenderMap());

		SettlementRuntime.getRuntime().scheduleSorting(1); //Every 1 minute for now

		EconomyManager.getManager().scheduleTaxCollection(1); //Every 1 minute for debug purposes
	}
}
