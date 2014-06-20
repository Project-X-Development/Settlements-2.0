package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.MapManager;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener{

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		ItemStack item = event.getPlayer().getItemInHand();
		if(item!=null && item.getType().equals(Material.MAP) && item.getDurability()==0){
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR)||event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				MapManager.getInstance().decrement(event.getPlayer());
			}else if(event.getAction().equals(Action.LEFT_CLICK_AIR)||event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				MapManager.getInstance().increment(event.getPlayer());
			}
		}
	}
}
