package me.projectx.Settlements.Utils;

import java.util.Collections;
import java.util.UUID;

import me.projectx.Settlements.Main;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.Settlement;

import org.bukkit.Bukkit;

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
	public void sortMembers(Settlement s){
		Collections.sort(s.getCitizens());
		Collections.sort(s.getOfficers());
		
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
		Collections.sort(SettlementManager.getManager().settlements, Settlement.comparator);
		
		//Debug
		for (Settlement s : SettlementManager.getManager().settlements)
			System.out.println(s.getName());
	}
	
	/**
	 * Schedule sorting of Settlements & their members 
	 * 
	 * @param minutes : How often sorting should be done
	 */
	@SuppressWarnings("deprecation")
	public void scheduleSorting(int minutes){ //just incase we want to do it automatically...
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable(){
			@Override
			public void run() {
				sortSettlements();
				for (Settlement s : SettlementManager.getManager().settlements)
					sortMembers(s);
			}
			
		}, 0, 1200 * minutes);
	}
}
