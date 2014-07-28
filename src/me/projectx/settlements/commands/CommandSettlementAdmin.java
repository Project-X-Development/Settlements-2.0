package me.projectx.settlements.commands;

import java.sql.SQLException;

import me.projectx.settlements.enums.ClaimType;
import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.CommandModel;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSettlementAdmin extends CommandModel{

	public CommandSettlementAdmin() {
		super("settlements.admin", "/sa");
	}

	@Override
	public boolean onCmd(CommandSender sender, String cml, String[] args) throws SQLException {
		if (cml.equalsIgnoreCase("sa")){
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
					default:
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGUMENT.getMsg());
						break;	
				}
			}
		}
		return true;
	}
}
