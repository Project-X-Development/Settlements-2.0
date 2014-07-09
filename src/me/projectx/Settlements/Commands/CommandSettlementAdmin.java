package me.projectx.Settlements.Commands;

import java.sql.SQLException;

import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.CommandModel;
import me.projectx.Settlements.Utils.ClaimType;
import me.projectx.Settlements.Utils.MessageType;

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
							ChunkManager.getInstance().claimSpecialChunk((Player)sender, ClaimType.valueOf(args[1].toUpperCase()));
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
							if (ChunkManager.getInstance().unclaimChunk(p.getLocation().getChunk().getX(), p.getLocation().getChunk().getZ())) 
								p.sendMessage(MessageType.CHUNK_UNCLAIM_SUCCESS.getMsg());
							else 
								p.sendMessage(MessageType.CHUNK_UNCLAIM_FAIL.getMsg());
						}else{
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS + "Try /sa unclaim");
						}
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
