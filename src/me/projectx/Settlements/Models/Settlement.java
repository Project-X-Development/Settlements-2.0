package me.projectx.Settlements.Models;

import java.util.ArrayList;

import me.projectx.Settlements.Managers.SettlementManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Settlement {

	private long id;
	private int power;
	private double balance;
	private String leader, name, desc /*tag*/;
	private final ArrayList<String> officers = new ArrayList<String>();
	private final ArrayList<String> citizens = new ArrayList<String>();

	public Settlement(String name){
		this.name = name;
		SettlementManager.getManager();
		this.id = SettlementManager.getSettlements().size() + 1;
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
	 * Get the id of a settlement
	 * 
	 * @return The id of the settlement
	 */
	public long getId(){
		return this.id;
	}

	/**
	 * Set the ID of the settlement
	 * 
	 * @param id : The ID to give the Settlement
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * Get the balance of the Settlement
	 * 
	 * @return The balance
	 */
	public double getBalance(){
		return this.balance;
	}

	/**
	 * Set the balance of the settlement
	 * 
	 * @param balance : The value to set the balance to
	 */
	public void setBalance(double balance){
		this.balance = balance;
	}

	/**
	 * Get the power of the Settlement
	 * 
	 * @return The power of the Settlement
	 */
	public int getPower(){
		return this.power;
	}

	/**
	 * Set the power of the Settlement
	 * 
	 * @param value : The value to set it to
	 */
	public void setPower(int value){
		this.power = value;
	}

	/**
	 * Set the name for the Settlement
	 * 
	 * @param name : The name for the settlement
	 */
	public void setName(String name){
		this.name = name;
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
	 * Get the description of the Settlement
	 * 
	 * @return The description of the Settlement
	 */
	public String getDescription(){
		return this.desc;
	}

	/**
	 * Set the description of the Settlement
	 * 
	 * @param description : The description of the Settlement
	 */
	public void setDescription(String description){
		this.desc = description;
	}

	/**
	 * Get the tag of the Settlement
	 * 
	 * @return The tag of the Settlement
	 */
	/*public String getTag(){
		return this.tag;
	}*/

	/**
	 * Set the tag of the Settlement
	 * 
	 * @param tag : The tag of the Settlement
	 */
	/*public boolean setTag(String tag){
		if (tag.length() <= 4){
			this.tag = tag;
			return true;
		}
		else{
			return false;
		}
	}*/

	/**
	 * Grant citizenship to a player
	 * 
	 * @param name : The name of the player
	 */
	public void giveCitizenship(String name){
		if (!isCitizen(name)) {
			citizens.add(name);
		}
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
			if (!isOfficer(name)) {
				officers.add(name);
			}
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
	 * Determine if a player is a member of the settlement
	 * 
	 * @param name : The name of the player to check
	 * @return True if the player is a citizen, officer, or leader
	 */
	public boolean hasMember(String name){
		return isCitizen(name) || isOfficer(name) || isLeader(name);
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

	/**
	 * Get how many members are in the Settlement
	 * 
	 * @return How many members are in the Settlement
	 */
	public int memberSize(){
		return (citizens.size() + officers.size() + 1); // +1 is to include the leader
	}

	/**
	 * Send a message to all the players in the Settlement
	 * 
	 * @param message : The message to send to the Settlement members
	 */
	public void sendSettlementMessage(String message){
		for (Player p : Bukkit.getOnlinePlayers()){
			if (hasMember(p.getName())) {
				p.sendMessage(message);
			}
		}
	}

	/**
	 * Deletes settlement object
	 * 
	 * @throws Throwable 
	 */
	public void delteteSettlement() throws Throwable{
		this.finalize();
	}
}