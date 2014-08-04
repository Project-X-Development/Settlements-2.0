package me.projectx.settlements.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.UUID;

import me.projectx.settlements.managers.SettlementManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Settlement {

	private long id;
	private int power;
	private double balance;
	private boolean delete;
	private String name, desc;
	private UUID owner, queuedLeader;
	private Location home;
	private final ArrayList<Long> allies = new ArrayList<Long>();
	private final ArrayList<UUID> officers = new ArrayList<UUID>();
	private final ArrayList<UUID> citizens = new ArrayList<UUID>();

	public Settlement(String name) {
		this.name = name;
		this.id = new Random().nextLong();
	}

	/**
	 * Get the name of a settlement
	 * 
	 * @return The name of the settlement
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the id of a settlement
	 * 
	 * @return The id of the settlement
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Set the ID of the settlement
	 * 
	 * @param id
	 *            : The ID to give the Settlement
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get the balance of the Settlement
	 * 
	 * @return The balance
	 */
	public double getBalance() {
		return this.balance;
	}

	/**
	 * Set the balance of the settlement
	 * 
	 * @param balance
	 *            : The value to set the balance to
	 */
	public void setBalance(double balance) {
		//TODO Power calculations
		this.balance = balance;
	}

	/**
	 * Get the power of the Settlement
	 * 
	 * @return The power of the Settlement
	 */
	public int getPower() {
		return this.power;
	}

	/**
	 * Set the power of the Settlement
	 * 
	 * @param value
	 *            : The value to set it to
	 */
	public void setPower(int value) {
		this.power = value;
	}

	/**
	 * Set the name for the Settlement
	 * 
	 * @param name
	 *            : The name for the settlement
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the leader of the settlement
	 * 
	 * @return The UUID of the leader leader of the settlement
	 */
	public UUID getLeader() {
		return this.owner;
	}

	/**
	 * Set the leader of the settlement
	 * 
	 * @param uuid
	 *            : The UUID of the new leader
	 */
	public void setLeader(UUID uuid) {
		this.owner = uuid;
	}

	/**
	 * Set the leader of the Settlement
	 * 
	 * @param uuid
	 *            : The string that will be converted to UUID
	 */
	public void setLeader(String uuid) {
		this.owner = UUID.fromString(uuid);
	}

	/**
	 * Get the description of the Settlement
	 * 
	 * @return The description of the Settlement
	 */
	public String getDescription() {
		return this.desc;
	}

	/**
	 * Set the description of the Settlement
	 * 
	 * @param description
	 *            : The description of the Settlement
	 */
	public void setDescription(String description) {
		this.desc = description;
	}

	/**
	 * Grant citizenship to a player
	 * 
	 * @param uuid
	 *            : The UUID of the player to grant citizenship
	 */
	public void giveCitizenship(UUID uuid) {
		if (!hasMember(uuid)) {
			citizens.add(uuid);
		}
	}

	/**
	 * Grant citizenship to a player
	 * 
	 * @param uuid
	 *            : The string that will be converted to a UUID
	 */
	public void giveCitizenship(String uuid) {
		if (uuid != null) {
			UUID id = UUID.fromString(uuid);
			if (!hasMember(id)) {
				citizens.add(id);
			}
		}
	}

	/**
	 * Revoke citizenship from a player
	 * 
	 * @param uuid
	 *            : The UUID of the player who will lose citizenship
	 */
	public void revokeCitizenship(UUID uuid) {
		if (isCitizen(uuid)) {
			citizens.remove(uuid);
		} else if (isOfficer(uuid)) {
			officers.remove(uuid);
		}
		setPower(getPower() - 1);
	}

	/**
	 * Set a player as an officer of the settlement
	 * 
	 * @param uuid
	 *            : The UUID of the player who will become an officer
	 */
	public void setOfficer(UUID uuid) {
		if (hasMember(uuid)) {
			if (!isOfficer(uuid)) {
				officers.add(uuid);
				citizens.remove(uuid);
			}
		}
	}

	/**
	 * Set a player as an officer of the Settlement
	 * 
	 * @param uuid
	 *            : The string that will be converted to UUID.
	 */
	public void setOfficer(String uuid) {
		if (uuid != null) {
			UUID id = UUID.fromString(uuid);
			if (hasMember(id)) {
				if (!isOfficer(id)) {
					officers.add(id);
					citizens.remove(id);
				}
			}
		}
	}

	/**
	 * Determine if a player is a citizen of the settlement
	 * 
	 * @param uuid
	 *            : The UUID of the player to check
	 * @return True if the player is a citizen
	 */
	public boolean isCitizen(UUID uuid) {
		return citizens.contains(uuid);
	}

	/**
	 * Determine if a player is an officer in the settlement
	 * 
	 * @param uuid
	 *            : The UUID of the player to check
	 * @return True if the player is an officer
	 */
	public boolean isOfficer(UUID uuid) {
		return officers.contains(uuid);
	}

	/**
	 * Determine if a player is the leader of the settlement
	 * 
	 * @param uuid
	 *            : The UUID of the player to check
	 * @return True if the player is the leader of the settlement
	 */
	public boolean isLeader(UUID uuid) {
		return owner.equals(uuid);
	}

	/**
	 * Determine if a player is a member of the settlement
	 * 
	 * @param uuid
	 *            : The UUID of the player to check
	 * @return True if the player is a citizen, officer, or leader
	 */
	public boolean hasMember(UUID uuid) {
		return isCitizen(uuid) || isOfficer(uuid) || isLeader(uuid);
	}

	/**
	 * Get the citizens of the settlement
	 * 
	 * @return The UUIDs of the citizens of the settlement
	 */
	public ArrayList<UUID> getCitizens() {
		return citizens;
	}

	/**
	 * Get the officers of the settlement
	 * 
	 * @return The UUIDs of the officers of the settlement
	 */
	public ArrayList<UUID> getOfficers() {
		return officers;
	}

	/**
	 * Get how many members are in the Settlement
	 * 
	 * @return How many members are in the Settlement
	 */
	public int memberSize() {
		return (citizens.size() + officers.size() + 1);
	}

	/**
	 * Send a message to all the players in the Settlement
	 * 
	 * @param message
	 *            : The message to send to the Settlement members
	 */
	public void sendSettlementMessage(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (hasMember(p.getUniqueId())) {
				p.sendMessage(message);
			}
		}
	}

	/**
	 * Send a message to all members in this settlement's alliance
	 * 
	 * @param message
	 *            : The message to send to the alliance members
	 */
	public void sendAllianceMessage(String message) {
		// Mental note: This could probably be more efficient
		for (Long s : allies) {
			Settlement set = SettlementManager.getManager().getSettlement(s);
			if (set != null) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (set.hasMember(p.getUniqueId())) {
						p.sendMessage(message);
					}
				}
			}
		}
	}

	/**
	 * Determine if the settlement is currently at war
	 * 
	 * @return True if the Settlement is at war
	 */
	public boolean isInWar() {
		for (War w : War.instances) {
			if (w.getStarter() == this || w.getAccepter() == this) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine if the Settlement is allied with a designated Settlement
	 * 
	 * @param s
	 *            : The Settlement to check
	 * @return True if the Settlement is allied with the other Settlement
	 */
	public boolean hasAlly(Settlement s) {
		return allies.contains(s.getId());
	}

	/**
	 * Get all Settlements in this Settlement's alliance
	 * 
	 * @return All Settlements in this Settlement's alliance
	 */
	public ArrayList<Settlement> getAlliedSettlements() {
		ArrayList<Settlement> list = new ArrayList<Settlement>();
		for (Settlement s : SettlementManager.getManager().settlements){
			if (allies.contains(s.getId())){
				list.add(s);
			}
		}
		return list;
	}
	
	public ArrayList<Long> getAllies(){
		return allies;
	}

	/**
	 * Add an ally to the Settlement
	 * 
	 * @param s
	 *            : The Settlement to add
	 * @return True if the ally was successfully added
	 */
	public void addAlly(Settlement s) {
		if (!hasAlly(s)) {
			allies.add(s.getId());
			s.addAlly(this);
		}
	}

	/**
	 * Remove one of the Settlement's allies
	 * 
	 * @param s
	 *            : The Settlement who will no longer be in the alliance
	 * @return True if the ally was successfully removed
	 */
	public void removeAlly(Settlement s) {
		if (hasAlly(s)) {
			allies.remove(s.getId());
			s.removeAlly(this);
		}
	}

	/**
	 * Get a member from the Settlement. This method is used for iteration only!
	 * 
	 * @param i
	 *            - The integer value of the player to get
	 * @return The player based on the integer specified
	 */
	public UUID getPlayer(int i) {
		UUID uuid = null;
		if (i < citizens.size()) {
			uuid = citizens.get(i);
		} else if (i - citizens.size() < officers.size()) {
			uuid = officers.get(i - citizens.size());
		} else {
			uuid = owner;
		}
		return uuid;
	}

	/**
	 * Get the rank of a player
	 * <p>
	 * Ex. "Citizen", "Officer", "Leader"
	 * 
	 * @param p
	 *            : The player
	 * @return The rank of the player
	 */
	public String getRank(OfflinePlayer p) {
		UUID id = p.getUniqueId();
		if (isCitizen(id)) {
			return "Citizen";
		} else if (isOfficer(id)) {
			return "Officer";
		} else if (isLeader(id)) {
			return "Leader";
		}
		return null;
	}

	public static Comparator<Settlement> comparator = new Comparator<Settlement>() {
		@Override
		public int compare(Settlement s1, Settlement s2) {
			String n1 = s1.getName().toUpperCase();
			String n2 = s2.getName().toUpperCase();

			return n1.compareTo(n2);
		}
	};

	/**
	 * Get the location of the Settlement's home
	 * 
	 * @return The Settlement's home location
	 */
	public Location getHome() {
		return home;
	}

	/**
	 * Set the Settlement's home location
	 * 
	 * @param location
	 *            : The location to set
	 */
	public void setHome(Location location) {
		this.home = location;
	}

	/**
	 * Determine if this Settlement has a home set
	 * 
	 * @return True if there is a home set, false if not
	 */
	public boolean hasHome() {
		return home != null;
	}

	/**
	 * Get the UUID of the player that is queued to be the next leader of the Settlement
	 * 
	 * @return The UUID of the queued leader
	 */
	public UUID getQueuedLeader(){
		return queuedLeader;
	}

	/**
	 * Set the queued leader for the Settlement
	 * 
	 * @param uuid : The UUID of the queued leader
	 */
	public void setQueuedLeader(UUID uuid){
		this.queuedLeader = uuid;
	}

	/**
	 * Determine if the Settlement should be deleted
	 * 
	 * @return True if should be deleted, false otherwise
	 */
	public boolean delete(){
		return delete;
	}

	/**
	 * Set whether or not the Settlement should be deleted
	 * 
	 * @param delete True to delete, false to not delete
	 */
	public void setToDelete(boolean delete){
		this.delete = delete;
	}
}