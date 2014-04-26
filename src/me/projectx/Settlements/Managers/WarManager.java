package me.projectx.Settlements.Managers;

import java.util.HashMap;
import java.util.Map;

import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Models.War;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarManager {

	private static WarManager instance;

	private Map<Settlement, Settlement> requests = new HashMap<Settlement, Settlement>();

	public static WarManager getInstance(){
		return instance;
	}

	public War getWar(long id){
		for (War w : War.instances){
			if (w.getId() == id){
				return w;
			}
		}
		return null;
	}

	public War getWar(Settlement set){
		for (War w : War.instances){
			if (w.getStarter() == set || w.getAccepter() == set){
				return w;
			}
		}
		return null;
	}

	public boolean createWar(Settlement setA, Settlement setB, CommandSender sender){
		if (setA.isLeader(((Player) sender).getName())){
			if (!setA.isInWar()){
				if (!setB.isInWar()){
					int powerA = setA.getPower();
					int powerB = setB.getPower();
					if (Math.abs(powerA - powerB) > (powerA/10)){
						sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + setB.getName() + " has been sent a request of war!");
						return true;
					}
					else{
						sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "Your power is not close enough to start a war!");
						return false;
					}
				}
				else{
					sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + setB.getName() + " is already in war!");
					return false;
				}

			}
			else{
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "You are already in war!");
				return false;
			}
		}
		else{
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "You must be an owner to declare war!");
			return false;
		}
	}

	public void sendRequest(Settlement sender, Settlement set){
		OfflinePlayer p = Bukkit.getOfflinePlayer(set.getLeader());
		if (p.isOnline()){
			((Player) p).sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GOLD + sender.getName() + " has requsted a war! Do '/s war accept' to accept or '/s war deny' to deny.");
			requests.put(sender, set);
		}
		else{
			requests.put(sender, set);
		}
	}

	public void acceptRequest(Settlement set){
		if (requests.containsValue(set)){
			for (Settlement temp : requests.keySet()){
				if (requests.get(temp) == set){
					set.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.GOLD + "You are now at war with " + temp.getName() + "!");
					temp.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.GOLD + set.getName() + " is now at war with your settlement!");
					new War(temp, set);
					requests.remove(temp);
					break;
				}
			}
		}
	}

	public void denyRequest(Settlement set){
		if (requests.containsValue(set)){
			for (Settlement temp : requests.keySet()){
				if (requests.get(temp) == set){
					temp.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.GOLD + set.getName() + " has denied your request of war!");
					requests.remove(temp);
					break;
				}
			}
		}
	}
}
