package me.projectx.settlements.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent e){
		String title = e.getInventory().getTitle();
		if (title.contains(ChatColor.BLUE + "Members of "))
			e.setCancelled(true);
		else if (title.equals(ChatColor.BLUE + "All Current Settlements:"))
			e.setCancelled(true);
		else if (title.equals(ChatColor.DARK_RED + "Settlement Commands"))
			e.setCancelled(true);
		else if (title.equals(ChatColor.DARK_PURPLE + "Your Settlement's Allies"))
			e.setCancelled(true);
	}	
}
