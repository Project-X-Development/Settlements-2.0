package me.projectx.Settlements.Models;

import java.util.ArrayList;
import java.util.UUID;

import me.projectx.Settlements.Managers.SettlementManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Settlement {

	private long id;
	private int power;
	private double balance;
	private String name, desc;
	private UUID owner;
	private ArrayList<String> allies = new ArrayList<String>();
	private ArrayList<UUID> officers = new ArrayList<UUID>();
	private ArrayList<UUID> citizens = new ArrayList<UUID>();

	public Settlement(String name){
		this.name = name;
		this.id = SettlementManager.getManager().settlements.size() + 1;
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
	 * @return The UUID of the leader leader of the settlement
	 */
	public UUID getLeader(){
		return this.owner;
	}

	/**
	 * Set the leader of the settlement
	 * 
	 * @param uuid : The UUID of the new leader
	 */
	public void setLeader(UUID uuid){
		this.owner = uuid;
	}
	
	/**
	 * Set the leader of the Settlement
	 * 
	 * @param uuid : The string that will be converted to UUID
	 */
	public void setLeader(String uuid){
		this.owner = UUID.fromString(uuid);
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
	 * Grant citizenship to a player
	 * 
	 * @param uuid : The UUID of the player to grant citizenship
	 */
	public void giveCitizenship(UUID uuid){
		if (!isCitizen(uuid)) {
			citizens.add(uuid);
		}
	}
	
	/**
	 * Grant citizenship to a player
	 * 
	 * @param uuid : The string that will be converted to a UUID
	 */
	public void giveCitizenship(String uuid){
		if (uuid != null){
			UUID id = UUID.fromString(uuid);
			if (!isCitizen(id)){
				citizens.add(id);
			}
		}
	}

	/**
	 * Revoke citizenship from a player
	 * 
	 * @param uuid : The UUID of the player who will lose citizenship
	 */
	public void revokeCitizenship(UUID uuid){
		if (isCitizen(uuid)){
			citizens.remove(uuid);
			officers.remove(uuid);
		}
	}

	/**
	 * Set a player as an officer of the settlement
	 * 
	 * @param uuid : The UUID of the player who will become an officer
	 */
	public void setOfficer(UUID uuid){
		if (isCitizen(uuid)){
			if (!isOfficer(uuid)) {
				officers.add(uuid);
			}
		}
	}
	
	/**
	 * Set a player as an officer of the Settlement
	 * 
	 * @param uuid : The string that will be converted to UUID. 
	 */
	public void setOfficer(String uuid){
		if (uuid != null){
			UUID id = UUID.fromString(uuid);
			if (isCitizen(id)){
				if (!isOfficer(id)){
					officers.add(id);
				}
			}
		}
	}

	/**
	 * Determine if a player is a citizen of the settlement
	 * 
	 * @param uuid : The UUID of the player to check
	 * @return True if the player is a citizen
	 */
	public boolean isCitizen(UUID uuid){
		return citizens.contains(uuid);
	}

	/**
	 * Determine if a player is an officer in the settlement
	 * 
	 * @param uuid : The UUID of the player to check
	 * @return True if the player is an officer
	 */
	public boolean isOfficer(UUID uuid){
		return officers.contains(uuid);
	}

	/**
	 * Determine if a player is the leader of the settlement
	 * 
	 * @param uuid : The UUID of the player to check
	 * @return True if the player is the leader of the settlement
	 */
	public boolean isLeader(UUID uuid){
		return owner.equals(uuid);
	}

	/**
	 * Determine if a player is a member of the settlement
	 * 
	 * @param uuid : The UUID of the player to check
	 * @return True if the player is a citizen, officer, or leader
	 */
	public boolean hasMember(UUID uuid){
		return isCitizen(uuid) || isOfficer(uuid) || isLeader(uuid);
	}

	/**
	 * Get the citizens of the settlement
	 * 
	 * @return The UUIDs of the citizens of the settlement
	 */
	public ArrayList<UUID> getCitizens(){
		return citizens;
	}

	/**
	 * Get the officers of the settlement
	 * 
	 * @return The UUIDs of the officers of the settlement
	 */
	public ArrayList<UUID> getOfficers(){
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
			if (hasMember(p.getUniqueId())) {
				p.sendMessage(message);
			}
		}
	}
	
	/**
	 * Send a message to all members in this settlement's alliance
	 * 
	 * @param message : The message to send to the alliance members
	 */
	public void sendAllianceMessage(String message){
		//Mental note: This could probably be more efficient
		for (String s : allies){ 
			Settlement set = SettlementManager.getManager().getSettlement(s);
			if (set != null){
				for (Player p : Bukkit.getOnlinePlayers()){
					if (set.hasMember(p.getUniqueId()))
						p.sendMessage(message);
				}
			}
		}
	}
	
	/**
	 * Determine if the settlement is currently at war
	 * 
	 * @return True if the Settlement is at war
	 */
	public boolean isInWar(){
		for (War w : War.instances){
			if (w.getStarter() == this || w.getAccepter() == this){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determine if the Settlement is allied with a designated Settlement
	 * 
	 * @param s : The Settlement to check
	 * @return True if the Settlement is allied with the other Settlement
	 */
	public boolean hasAlly(Settlement s){
		return allies.contains(s.getName());
	}
	
	/**
	 * Get one of the allies of the Settlement
	 * 
	 * @param s : The ally to get
	 * @return The allied Settlement
	 */
	public Settlement getAlly(Settlement s){
		return SettlementManager.getManager().getSettlement(s.getName());
	}
	
	/**
	 * Get the names of all the allies of the Settlement
	 * 
	 * @return The names of all the allied Settlements
	 */
	public ArrayList<String> getAllies(){
		return allies;
	}
	
	/**
	 * Get all Settlements in this Settlement's alliance
	 * 
	 * @return All Settlements in this Settlement's alliance
	 */
	public ArrayList<String> getAlliedSettlements(){
		return allies;
	}
	
	/**
	 * Add an ally to the Settlement
	 * 
	 * @param s : The Settlement to add
	 * @return True if the ally was successfully added
	 */
	public boolean addAlly(Settlement s){
		if (!hasAlly(s)){
			allies.add(s.getName());
			s.addAlly(this);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove one of the Settlement's allies
	 * 
	 * @param s : The Settlement who will no longer be in the alliance
	 * @return True if the ally was successfully removed
	 */
	public boolean removeAlly(Settlement s){
		if (hasAlly(s)){
			allies.remove(s.getName());
			s.removeAlly(this);
			return true;
		}
		return false;
	}
	
	/*public Player getPlayer(int i){
		List<UUID> temp = new ArrayList<UUID>();
		temp.addAll(citizens);
		temp.addAll(officers);
		temp.add(owner);
		
		return Bukkit.getPlayer(temp.get(i));
	}*/
	
	/**
	 * Get a member from the Settlement. This method is used for iteration only!
	 * 
	 * @param i - The integer value of the player to get
	 * @return The player based on the integer specified
	 */
	public Player getPlayer(int i){
		UUID uuid = null;
		if (i < citizens.size()) {
			uuid = citizens.get(i);
		}else if (i - citizens.size() < officers.size()) {
			uuid = officers.get(i - citizens.size());
		}else if (i - citizens.size() - officers.size() == 0) {
			uuid = owner;
		}
	 return Bukkit.getPlayer(uuid);
	}
	
	/**
	 * Get the rank of a player
	 * <p>
	 * Ex. "Citizen", "Officer", "Leader"
	 * 
	 * @param p : The player
	 * @return The rank of the player
	 */
	public String getRank(Player p){
		if (isCitizen(p.getUniqueId()))
			return "Citizen";
		else if (isOfficer(p.getUniqueId()))
			return "Officer";
		else if (isLeader(p.getUniqueId()))
			return "Leader";
		return null;
	}
}