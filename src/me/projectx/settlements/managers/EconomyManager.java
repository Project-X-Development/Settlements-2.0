package me.projectx.settlements.managers;

import java.sql.SQLException;

import me.projectx.Economy.Managers.AccountManager;
import me.projectx.settlements.Main;
import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.models.Settlement;
import me.projectx.settlements.utils.DatabaseUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EconomyManager {
	
	private static EconomyManager em = new EconomyManager();
	public int taxMinutes = 20;
	
	public static EconomyManager getManager(){
		return em;
	}
	
	/**
	 * Deposit a given amount into a Settlement's account
	 *
	 * @param s : The Settlement
	 * @param amount : The amount to deposit
	 * @throws SQLException
	 */
	public void depositIntoSettlement(Settlement s, double amount){
		s.setBalance(s.getBalance() + amount);
		try {
			DatabaseUtils.queryOut("UPDATE settlements SET balance=" + s.getBalance() + " WHERE id=" + s.getId() + ";");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deposit a given amount into a Settlement's account from a player's balance
	 * 
	 * @param player : The player who is giving the money
	 * @param s : The Settlement
	 * @param amount : The amount to deposit
	 * @throws SQLException
	 */
	public void depositIntoSettlement(Player player, Settlement s, double amount){
		AccountManager.getManager().withdraw(player, amount);
		s.setBalance(s.getBalance() + amount);
		try {
			DatabaseUtils.queryOut("UPDATE settlements SET balance=" + s.getBalance() + " WHERE id=" + s.getId() + ";");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Withdraw a given amount from a Settlement's balance
	 * 
	 * @param s : The Settlement
	 * @param amount : The amount to withdraw
	 * @throws SQLException
	 */
	public void withdrawFromSettlement(Settlement s, double amount){
		s.setBalance(s.getBalance() - amount);
		try {
			DatabaseUtils.queryOut("UPDATE settlements SET balance=" + s.getBalance() + " WHERE id=" + s.getId() + ";");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tax all Settlements for their claimed land
	 */
	public void taxSettlements(){
		for (Settlement s : SettlementManager.getManager().settlements){
			int claimCount = ChunkManager.getManager().getClaims(s).size();
			double cost = 0;
			if (claimCount > 0)
				cost = (claimCount + 1) * 7;
			withdrawFromSettlement(s, cost);
			s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + 
					"Your Settlement has been charged " + ChatColor.AQUA + "$" + cost + ChatColor.GRAY + " in land taxes");
		}
	}

	public synchronized void scheduleTaxCollection(){
		new BukkitRunnable(){
			public void run(){
				taxSettlements();
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 1200 * taxMinutes, 1200 * taxMinutes);
	}
	
	public void setTaxSchedule(int minutes){
		this.taxMinutes = minutes;
	}
} 
