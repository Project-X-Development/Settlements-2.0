package me.projectx.settlements.commands;

import java.sql.SQLException;

import me.projectx.settlements.enums.ClaimType;
import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.EconomyManager;
import me.projectx.settlements.managers.PlayerManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.CommandModel;
import me.projectx.settlements.models.Settlement;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSettlementAdmin extends CommandModel{

	public CommandSettlementAdmin() {
		super("settlements.admin", "/sa");
	}

	@Override
	public boolean onCmd(CommandSender sender, String cml, String[] args) throws SQLException {
		if (args.length > 0){
			switch(args[0]){
				case "claim":
					if (args.length == 2)
						ChunkManager.getManager().claim((Player)sender, ClaimType.valueOf(args[1].toUpperCase()));
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS + "Try /sa claim <ClaimType>");
					break;
				case "delete":
					if (args.length == 2)
						SettlementManager.getManager().deleteSettlement(sender, args[1]);
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS + "Try /sa delete <Settlement>");
					break;
				case "unclaim":
					if (args.length == 1){
						Player p = (Player) sender;
						Location l = p.getLocation();
						ChunkManager.getManager().unclaim(p, l.getChunk().getX(), l.getChunk().getZ(), l.getWorld(), true);
					}else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS + "Try /sa unclaim");
					break;
				case "autoclaim":
					if (args.length == 2)
							ChunkManager.getManager().setAutoClaiming((Player)sender, ClaimType.valueOf(args[1].toUpperCase()));
					else
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /sa autoclaim <ClaimType>");
					break;
				case "deposit":
					if (args.length == 3){
						Settlement s = SettlementManager.getManager().getSettlement(args[1]);
						EconomyManager.getManager().depositIntoSettlement((Player)sender, s, Double.valueOf(args[2]));
						sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY 
								+ "Deposited " + ChatColor.AQUA + "$" + args[2] + ChatColor.GRAY + " into " + ChatColor.AQUA + s.getName());
					}else{
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /sa deposit <Settlement> <amount>");
					}
					break;
				case "withdraw":
					if (args.length == 3){
						Settlement s = SettlementManager.getManager().getSettlement(args[1]);
						EconomyManager.getManager().withdrawFromSettlement(s, Double.valueOf(args[2]));
						sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY 
								+ "Withdrew " + ChatColor.AQUA + "$" + args[2] + ChatColor.GRAY + " from " + ChatColor.AQUA + s.getName());
					}else{
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /sa withdraw <Settlement> <amount>");
					}
					break;
				case "override":
					if (args.length == 1){
						PlayerManager.getInstance().setAdminOverride((Player)sender);
					}else{
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /sa override");
					}
					break;
				default:
					sender.sendMessage(MessageType.COMMAND_INVALID_ARGUMENT.getMsg());
					break;	
			}
		}
		return true;
	}
}
