package me.projectx.Settlements.API;

import java.util.ArrayList;

public class SettlementManager {
	
	private ArrayList<Settlement> settlements = new ArrayList<Settlement>();
	
	public Settlement getPlayerSettlement(String name){
		for (Settlement s : settlements){
			if (s.isCitizen(name))
				return s;
		}
		return null;
	}
	
	public boolean settlementExists(String name){
		for (Settlement s : settlements){
			if (s.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
}
