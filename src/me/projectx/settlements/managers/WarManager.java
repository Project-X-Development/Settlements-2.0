package me.projectx.settlements.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import me.projectx.Economy.Utils.DatabaseUtils;
import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.models.Settlement;
import me.projectx.settlements.models.War;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarManager {

	private static WarManager instance;

	private final Map<Settlement, Settlement> requests = new HashMap<Settlement, Settlement>();
	private PreparedStatement selectall, delete, add;

	private WarManager(){
		try{
			selectall = DatabaseUtils.getConnection().prepareStatement("SELECT * FROM wars;");
			delete = DatabaseUtils.getConnection().prepareStatement("DELETE FROM wars WHERE setA=? AND setB=?;");
			add = DatabaseUtils.getConnection().prepareStatement("INSERT INTO wars(setA, setB) VALUES (?,?);");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static WarManager getInstance() {
		return instance;
	}

	public void loadWarsFromDB() throws SQLException{
		ResultSet res = DatabaseUtils.queryIn(selectall);
		while (res.next()){
			String setAstr = res.getString("setA");
			String setBstr = res.getString("setB");

			Settlement setA = SettlementManager.getManager().getSettlement(setAstr);
			Settlement setB = SettlementManager.getManager().getSettlement(setBstr);

			new War(setA, setB);

		}
	}

	public War getWar(long id) {
		for (War w : War.instances) {
			if (w.getId() == id) {
				return w;
			}
		}
		return null;
	}

	public War getWar(Settlement set) {
		for (War w : War.instances) {
			if (w.getStarter() == set || w.getAccepter() == set) {
				return w;
			}
		}
		return null;
	}

	public boolean createWar(Settlement setA, Settlement setB,
			CommandSender sender) {
		if (setA.isLeader(((Player) sender).getUniqueId())) {
			if (!setA.isInWar()) {
				if (!setB.isInWar()) {
					int powerA = setA.getPower();
					int powerB = setB.getPower();
					if (Math.abs(powerA - powerB) > (powerA / 10)) {
						sender.sendMessage(MessageType.PREFIX.getMsg()
								+ ChatColor.RED + setB.getName()
								+ " has been sent a request of war!");
						return true;
					} else {
						sender.sendMessage(MessageType.PREFIX.getMsg()
								+ ChatColor.RED
								+ "Your power is not close enough to start a war!");
						return false;
					}
				} else {
					sender.sendMessage(MessageType.PREFIX.getMsg()
							+ ChatColor.RED + setB.getName()
							+ " is already in war!");
					return false;
				}

			} else {
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED
						+ "You are already in war!");
				return false;
			}
		} else {
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED
					+ "You must be an owner to declare war!");
			return false;
		}
	}

	//Hippie: I just want peace man ☮
	public void endWar(War w) throws SQLException {
		w.getStarter().sendSettlementMessage(ChatColor.GREEN + "The war with " + w.getAccepter().getName() + " has ended! ☮");
		w.getAccepter().sendSettlementMessage(ChatColor.GREEN + "The war with " + w.getStarter().getName() + " has ended! ☮");
		War.instances.remove(w);
		delete.setString(1, w.getStarter().getName());
		delete.setString(2, w.getAccepter().getName());
		DatabaseUtils.queryOut(delete);
	}

	public void sendRequest(Settlement sender, Settlement set) {
		OfflinePlayer p = Bukkit.getOfflinePlayer(set.getLeader());
		if (p.isOnline()) {
			((Player) p)
			.sendMessage(MessageType.PREFIX.getMsg()
					+ ChatColor.GOLD
					+ sender.getName()
					+ " has requsted a war! Do '/s war accept' to accept or '/s war deny' to deny.");
			requests.put(sender, set);
		} else {
			requests.put(sender, set);
		}
	}

	public void acceptRequest(Settlement set) throws SQLException {
		if (requests.containsValue(set)) {
			for (Settlement temp : requests.keySet()) {
				if (requests.get(temp) == set) {
					set.sendSettlementMessage(MessageType.PREFIX.getMsg()
							+ ChatColor.GOLD + "You are now at war with "
							+ temp.getName() + "!");
					temp.sendSettlementMessage(MessageType.PREFIX.getMsg()
							+ ChatColor.GOLD + set.getName()
							+ " is now at war with your settlement!");
					new War(temp, set);
					requests.remove(temp);
					add.setString(1, temp.getName());
					add.setString(2, set.getName());
					DatabaseUtils.queryOut(add);
					break;
				}
			}
		}
	}

	public void denyRequest(Settlement set) {
		if (requests.containsValue(set)) {
			for (Settlement temp : requests.keySet()) {
				if (requests.get(temp) == set) {
					temp.sendSettlementMessage(MessageType.PREFIX.getMsg()
							+ ChatColor.GOLD + set.getName()
							+ " has denied your request of war!");
					requests.remove(temp);
					break;
				}
			}
		}
	}

	public Map<Settlement, Settlement> getRequests() {
		return requests;
	}

	public boolean hasRequest(Settlement s) {
		return requests.containsValue(s);
	}

	public Settlement getOtherSettlement (War w, Settlement set){
		Settlement starter = w.getStarter();
		Settlement accepter = w.getAccepter();

		if (starter == set){
			return accepter;
		}

		else  if (accepter == set){
			return starter;
		}

		return null;
	}
}
