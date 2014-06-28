package me.projectx.Settlements.Commands;

import java.sql.SQLException;
import java.util.UUID;

import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Managers.MapManager;
import me.projectx.Settlements.Managers.PlayerManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.CommandModel;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.ClaimType;
import me.projectx.Settlements.Utils.CommandType;
import me.projectx.Settlements.Utils.DatabaseUtils;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

public class CommandSettlementPlayer extends CommandModel {

	public CommandSettlementPlayer() {
		super("settlement.player", "/s");
	}

	@Override
	public boolean onCmd(CommandSender sender, String cml, String[] args) throws SQLException {
		if (cml.equalsIgnoreCase("s")){
			if (args.length > 0){
				if (args[0].equalsIgnoreCase("create")) {
					if (args.length == 2)
						SettlementManager.getManager().createSettlement(args[1], sender);
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s create <name>");
				}
				
				if (args[0].equalsIgnoreCase("delete")) {
					if (args.length == 1)
						SettlementManager.getManager().deleteSettlement(sender);
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s delete");
				}
				
				if (args[0].equalsIgnoreCase("invite")) {
					if (args.length == 2)
						SettlementManager.getManager().inviteCitizen(args[1], sender);
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s invite <player>");
				}
				
				if (args[0].equalsIgnoreCase("accept")) {
					if (args.length == 1)
						SettlementManager.getManager().acceptInvite(sender.getName());
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s accept");
				}
				
				if (args[0].equalsIgnoreCase("decline")) {
					if (args.length == 1)
						SettlementManager.getManager().declineInvite(sender.getName());
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s decline");
				}
				
				if (args[0].equalsIgnoreCase("leave")) {
					if (args.length == 1)
						SettlementManager.getManager().leaveSettlement(sender.getName());
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s leave");
				}
				
				if (args[0].equalsIgnoreCase("list")) {
					if (args.length == 1)
						SettlementManager.getManager().listSettlements((Player)sender);
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s list");
				}
				
				if (args[0].equalsIgnoreCase("desc")){
					if (args.length > 1){
						StringBuilder str = new StringBuilder();
						for (int i = 1; i < args.length; i++) 
							str.append(args[i] + " ");
						SettlementManager.getManager().setDescription(sender, str.toString());
					}else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s desc <Settlement description>");
				}

				if (args[0].equalsIgnoreCase("claim")){
					if (args.length == 1)
						ChunkManager.getInstance().claimChunk((Player)sender, ClaimType.NORMAL);
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s claim");
				}

				if (args[0].equalsIgnoreCase("map")){
					if (args.length == 1)
						ChunkManager.getInstance().issueMap((Player)sender);
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s map");
				}

				if (args[0].equalsIgnoreCase("members")){
					if (args.length == 1) {
						Settlement s  = SettlementManager.getManager().getPlayerSettlement(sender.getName());
						if (s != null)
							SettlementManager.getManager().displayMembers((Player)sender, s.getName());
						else
							sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
					} 
					else if (args.length == 2) 
						SettlementManager.getManager().displayMembers((Player)sender, args[1]);
					if (args.length > 2)
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s members [Settlement]");
				}

				if (args[0].equalsIgnoreCase("kick")){
					if (args.length == 2) 
						SettlementManager.getManager().kickPlayer(sender, args[1]);
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s kick <player>");
				}

				/*
				if (args[0].equalsIgnoreCase("power")){//temporary
					SettlementManager.getManager().updateInfluence(SettlementManager.getManager().getPlayerSettlement(sender.getName()));
					sender.sendMessage("Your settlement power: " + SettlementManager.getManager().getPlayerSettlement(sender.getName()).getPower());
				}*/

				if (args[0].equalsIgnoreCase("unclaim")){
					if (sender instanceof Player){
						if (args.length == 1){
							Player p = (Player) sender;
							if (ChunkManager.getInstance().unclaimChunk(p.getName(), 
									p.getLocation().getChunk().getX(), p.getLocation().getChunk().getZ())) 
								p.sendMessage(MessageType.CHUNK_UNCLAIM_SUCCESS.getMsg());
							else 
								p.sendMessage(MessageType.CHUNK_UNCLAIM_FAIL.getMsg());
						}else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s unclaim");
					}else
						sender.sendMessage(MessageType.NOT_PLAYER.getMsg());
				}
				
				if (args[0].equalsIgnoreCase("autoclaim")){ //needs work atm
					ChunkManager.getInstance().setAutoClaiming((Player)sender);
				}
				
				if (args[0].equalsIgnoreCase("chat")){
					if (args.length == 1){
						Player p = (Player) sender;
						if (args.length == 1){
							PlayerManager.getInstance().setSettlementChatting(p);
						}else if (args.length == 2){
							PlayerManager.getInstance().setAllianceChatting(p);
						}
					}else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s chat");
				}
				
				if (args[0].equalsIgnoreCase("ally")){
					if (args.length == 2){
						SettlementManager sm = SettlementManager.getManager();
						sm.allySettlement(sm.getPlayerSettlement(sender.getName()), args[1]);
					}else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s ally <Settlement>");
				}
			} else if (sender instanceof Player){
				CommandType.displayCommands((Player)sender);
			} else{
				CommandType.printList(sender);
			}
		}
		return true;
	}
}
