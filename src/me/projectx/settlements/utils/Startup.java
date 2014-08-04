package me.projectx.settlements.utils;

import java.sql.SQLException;

import me.projectx.settlements.Main;
import me.projectx.settlements.commands.CommandSettlementAdmin;
import me.projectx.settlements.commands.CommandSettlementPlayer;
import me.projectx.settlements.enums.CommandType;
import me.projectx.settlements.events.*;
import me.projectx.settlements.managers.*;
import me.projectx.settlements.models.Players;
import me.projectx.settlements.runtime.SettlementRuntime;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;


public class Startup {

	public static void runStartup() throws SQLException{
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new BlockEvent(), Main.getInstance());
		pm.registerEvents(new PlayerChat(), Main.getInstance());
		pm.registerEvents(new PlayerDamage(), Main.getInstance());
		pm.registerEvents(new PlayerDeath(), Main.getInstance());
		pm.registerEvents(new PlayerInteract(), Main.getInstance());
		pm.registerEvents(new PlayerJoin(), Main.getInstance());
		pm.registerEvents(new PlayerQuit(), Main.getInstance());
		pm.registerEvents(new PlayerMove(), Main.getInstance());
		pm.registerEvents(new InventoryClick(), Main.getInstance());
		pm.registerEvents(new EntityExplode(), Main.getInstance());

		Main.getInstance().getCommand("s").setExecutor(new CommandSettlementPlayer());
		Main.getInstance().getCommand("sa").setExecutor(new CommandSettlementAdmin());

		CommandType.prepareCommandList();

		DatabaseUtils.setupConnection();
		DatabaseUtils.setupMySQL();

		SettlementManager.getManager().loadSettlmentsFromDB();

		ChunkManager.getManager().loadChunks();

		ChunkManager.getManager().verifyClaims();

		//WarManager.getInstance().loadWarsFromDB();

		for(Player p : Bukkit.getOnlinePlayers()){
			Players pl = PlayerManager.getInstance().addPlayer(p);
			pl.setInt("map", 25);
			MapManager.getInstance().add(p);
			boolean has = false;
			for(ItemStack is : p.getInventory().getContents()){
				if(is!=null&&is.getItemMeta()!=null&&is.getItemMeta().getDisplayName()!=null){
					if(is.getItemMeta().getDisplayName().equals(ChatColor.RED + "Dab " + ChatColor.BLUE + "Maps")){
						p.getInventory().remove(is);
						has = true;
					}
				}
			}
			if(has)MapManager.getInstance().givePlayerMap(p);
		}

		SettlementRuntime.getRuntime().scheduleSorting(1); //Every 1 minute for now

		EconomyManager.getManager().scheduleTaxCollection(); 
	}
}
