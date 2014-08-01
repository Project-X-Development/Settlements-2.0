package me.projectx.settlements.runtime;

import java.util.Collections;

import me.projectx.settlements.Main;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.Settlement;

import org.bukkit.scheduler.BukkitRunnable;

public class SettlementRuntime {
	
	private static SettlementRuntime rm = new SettlementRuntime();
	
	public static SettlementRuntime getRuntime(){
		return rm;
	}
	
	/**
	 * Sort the members of a given Settlement
	 * 
	 * @param s : The Settlement
	 */
	public void sortMembers(final Settlement s){
		new BukkitRunnable(){
			public void run(){
				System.out.println("[Settlements] Sorting Settlements members...");
				Collections.sort(s.getCitizens());
				Collections.sort(s.getOfficers());
				System.out.println("[Settlements] Done!");
			}
		}.runTaskAsynchronously(Main.getInstance());

	}
	
	/**
	 * Sort all Settlements
	 */
	public void sortSettlements(){
		new BukkitRunnable(){
			public void run(){
				System.out.println("[Settlements] Sorting Settlements...");
				Collections.sort(SettlementManager.getManager().settlements, Settlement.comparator);
				System.out.println("[Settlements] Done!");
			}
		}.runTaskAsynchronously(Main.getInstance());
	}
	
	/**
	 * Schedule sorting of Settlements & their members 
	 * 
	 * @param minutes : How often sorting should be done
	 */
	public void scheduleSorting(int minutes){ //just incase we want to do it automatically...
		new BukkitRunnable(){
			@Override
			public void run() {
				sortSettlements();
				for (Settlement s : SettlementManager.getManager().settlements){
					sortMembers(s);
				}
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 1200, 1200 * minutes);
	}
}
