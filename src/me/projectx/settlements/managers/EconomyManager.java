package me.projectx.settlements.managers;

import java.sql.SQLException;

import me.projectx.Economy.Managers.AccountManager;
import me.projectx.Economy.Models.Account;
import me.projectx.settlements.Main;
import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.models.Settlement;
import me.projectx.settlements.utils.DatabaseUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EconomyManager {
	
	private static EconomyManager em = new EconomyManager();
	private int memberMinCount = 3;
	private int chunkMinCount = 10;
	public int taxMinutes = 20;
	
	public static EconomyManager getManager(){
		return em;
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
		Account a = AccountManager.getManager().getAccount(player);
		if (a.getBalance() >= amount){
			s.setBalance(s.getBalance() + amount);
			AccountManager.getManager().withdraw(player, amount);
			s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + player.getName()
					+ ChatColor.GRAY + " deposited " + ChatColor.AQUA + "$" 
					+ amount + ChatColor.GRAY + " into your Settlement's account!");
			try {
				DatabaseUtils.queryOut("UPDATE settlements SET balance=" + s.getBalance() + " WHERE id=" + s.getId() + ";");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}else{
			player.sendMessage(MessageType.SETTLEMENT_BALANCE_NOT_ENOUGH.getMsg());
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
			if (s.hasOnlineMember()){
				int claimCount = ChunkManager.getManager().getClaims(s).size();
				double cost = (s.getCitizens().size() + 1 >= memberMinCount && claimCount >= chunkMinCount) ? claimCount * 7 : 0;
				withdrawFromSettlement(s, cost);
				s.sendSettlementMessage(MessageType.SETTLEMENT_TAX.getMsg().replace("<cost>", Double.valueOf(cost).toString()));
			}
		}
	}
	
	public void calculateAllianceBonus(){
		
	}

	/**
	 * Schedule Settlement tax collection
	 */
	public synchronized void scheduleTaxCollection(){ //synchronized to avoid CMEs
		new BukkitRunnable(){
			public void run(){
				taxSettlements();
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 1200 * taxMinutes);
	}
} 
