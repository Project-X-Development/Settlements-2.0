package me.projectx.Settlements.Utils;

import java.util.Collections;
import java.util.UUID;

import me.projectx.Settlements.Main;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.Settlement;

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
				Collections.sort(s.getCitizens());
				Collections.sort(s.getOfficers());
			}
		}.runTaskAsynchronously(Main.getInstance());
		//debug
		for (UUID id : s.getCitizens())
			System.out.println("Citizen: " + id);
		for (UUID id : s.getOfficers())
			System.out.println("Officer: " + id);
	}
	
	/**
	 * Sort all Settlements
	 */
	public void sortSettlements(){
		new BukkitRunnable(){
			public void run(){
				Collections.sort(SettlementManager.getManager().settlements, Settlement.comparator);
			}
		}.runTaskAsynchronously(Main.getInstance());
		
		//Debug
		for (Settlement s : SettlementManager.getManager().settlements)
			System.out.println(s.getName());
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
				for (Settlement s : SettlementManager.getManager().settlements)
					sortMembers(s);
			}
			
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 1200 * minutes);
	}
}
