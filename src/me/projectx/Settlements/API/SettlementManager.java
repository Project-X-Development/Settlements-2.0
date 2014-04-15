package me.projectx.Settlements.API;

import java.util.ArrayList;

public class SettlementManager {
	
	private ArrayList<Settlement> settlements = new ArrayList<Settlement>();
	private SettlementManager sm = new SettlementManager();
	
	/**
	 * Get an instance of the SettlementManager class
	 * 
	 * @return SettlementManager class instance
	 */
	public SettlementManager getManager(){
		return sm;
	}
	
	/**
	 * Get the settlement of a player
	 * 
	 * @param name : The name of the player to get the settlement for
	 * @return The player's settlement
	 */
	public Settlement getPlayerSettlement(String name){
		for (Settlement s : settlements){
			if (s.isCitizen(name) || s.isOfficer(name) || s.isLeader(name))
				return s;
		}
		return null;
	}
	
	/**
	 * Determine if a settlement exists
	 * 
	 * @param name : The name of the settlement to check
	 * @return True if the settlement exists
	 */
	public boolean settlementExists(String name){
		for (Settlement s : settlements){
			if (s.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	/**
	 * Get a settlement by name
	 * 
	 * @param name : The name of the settlement to get
	 * @return The designated settlement
	 */
	public Settlement getSettlement(String name){
		for (Settlement s : settlements){
			if (s.getName().equalsIgnoreCase(name))
				return s;
		}	
		return null;
	}
	
	/**
	 * Determine if a player is a citezen of a settlement
	 * 
	 * @param name : The name of the player to check
	 * @return True if the player is a citizen of a settlement
	 */
	public boolean isCitizenOfSettlement(String name){
		for (Settlement s : settlements){
			if (s.getCitizens().contains(name))
				return true;
		}
		return false;
	} 
	
	/**
	 * Delete a settlement
	 * 
	 * TODO
	 * Add stuff to remove from database and what-not
	 * 
	 * @param name : The name of the settlement to delete
	 */
	public void removeSettlement(String name){
		if (settlementExists(name))
			settlements.remove(name);
	}
}
