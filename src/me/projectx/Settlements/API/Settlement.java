package me.projectx.Settlements.API;

import java.util.ArrayList;

public class Settlement {
	
	private String leader, name;
	private ArrayList<String> officers = new ArrayList<String>();
	private ArrayList<String> citizens = new ArrayList<String>();
	
	public Settlement(String name){
		this.name = name;
	}
	
	/**
	 * Get the name of a settlement
	 * 
	 * @return The name of the settlement
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Get the leader of the settlement
	 * 
	 * @return The leader of the settlement
	 */
	public String getLeader(){
		return this.leader;
	}
	
	/**
	 * Set the leader of the settlement
	 * 
	 * @param name : The name of the new leader
	 */
	public void setLeader(String name){
		this.leader = name;
	}
	
	/**
	 * Grant citizenship to a player
	 * 
	 * @param name : The name of the player
	 */
	public void giveCitizenship(String name){
		if (!isCitizen(name))
			citizens.add(name);
	}
	
	/**
	 * Revoke citizenship from a player
	 * 
	 * @param name : The name of the player who will lose citizenship
	 */
	public void revokeCitizenship(String name){
		if (isCitizen(name)){
			citizens.remove(name);
			officers.remove(name);
		}
	}
	
	/**
	 * Set a player as an officer of the settlement
	 * 
	 * @param name : The name of the player who will become an officer
	 */
	public void setOfficer(String name){
		if (isCitizen(name)){
			if (!isOfficer(name))
				officers.add(name);
		}
	}
	
	/**
	 * Determine if a player is a citizen of the settlement
	 * 
	 * @param name : The name of the player to check
	 * @return True if the player is a citizen
	 */
	public boolean isCitizen(String name){
		return citizens.contains(name);
	}
	
	/**
	 * Determine if a player is an officer in the settlement
	 * 
	 * @param name : The name of the player to check
	 * @return True if the player is an officer
	 */
	public boolean isOfficer(String name){
		return officers.contains(name);
	}
	
	/**
	 * Determine if a player is the leader of the settlement
	 * 
	 * @param name : The name of the player to check
	 * @return True if the player is the leader of the settlement
	 */
	public boolean isLeader(String name){
		return name.equalsIgnoreCase(leader);
	}
	
	/**
	 * Get the citizens of the settlement
	 * 
	 * @return The citizens of the settlement
	 */
	public ArrayList<String> getCitizens(){
		return citizens;
	}
	
	/**
	 * Get the officers of the settlement
	 * 
	 * @return The officers of the settlement
	 */
	public ArrayList<String> getOfficers(){
		return officers;
	}
}