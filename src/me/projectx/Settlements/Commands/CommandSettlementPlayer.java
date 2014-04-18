package me.projectx.Settlements.Commands;

import java.sql.SQLException;

import me.projectx.Settlements.API.SettlementManager;
import me.projectx.Settlements.Land.ChunkManager;
import me.projectx.Settlements.Utils.CommandType;
import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSettlementPlayer extends CommandModel {

	public CommandSettlementPlayer() {
		super("settlement.player", "/s");
	}

	@Override
	public boolean onCmd(CommandSender sender, String cml, String[] args) throws SQLException {
		if (cml.equalsIgnoreCase("s")){
			if (args.length > 0){
				if (args[0].equalsIgnoreCase("create")) {
					SettlementManager.getManager().createSettlement(args[1], sender);
				}
				if (args[0].equalsIgnoreCase("delete")) {
					SettlementManager.getManager().deleteSettlement(sender);
				}
				if (args[0].equalsIgnoreCase("invite")) {
					SettlementManager.getManager().inviteCitizen(args[1], sender);
				}
				if (args[0].equalsIgnoreCase("accept")) {
					SettlementManager.getManager().acceptInvite(sender.getName());
				}
				if (args[0].equalsIgnoreCase("decline")) {
					SettlementManager.getManager().declineInvite(sender.getName());
				}
				if (args[0].equalsIgnoreCase("leave")) {
					SettlementManager.getManager().leaveSettlement(sender.getName());
				}
				if (args[0].equalsIgnoreCase("desc")){
					StringBuilder str = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						str.append(args[i] + " ");
					}
					SettlementManager.getManager().setDescription(sender, str.toString());
				}
				if (args[0].equalsIgnoreCase("claim")){ //temporary
					int status = ChunkManager.getInstance().claimChunk(sender.getName(), ((Player) sender).getLocation().getChunk().getX(), ((Player) sender).getLocation().getChunk().getZ(), ((Player) sender).getLocation().getWorld());
					if (status == 2){
						sender.sendMessage(MessageType.PREFIX.getMsg() + "Claimed this chunk!");
					}
					else if (status == 1){
						sender.sendMessage(MessageType.PREFIX.getMsg() + "Someone has already claimed this land!");
					}
					else if (status == 0){
						sender.sendMessage(MessageType.PREFIX.getMsg() + "You must be part of a settlement to claim land!");
					}
				}

				if (args[0].equalsIgnoreCase("map")){
					if (sender instanceof Player){
						Player p = (Player) sender;
						ChunkManager.getInstance().printMap(p);
					}else
						sender.sendMessage(MessageType.NOT_PLAYER.getMsg());
				}
				
				if (args[0].equalsIgnoreCase("members")){
					if (args.length == 1)
						SettlementManager.getManager().listMembers(sender, SettlementManager.getManager().getPlayerSettlement(sender.getName()));
					else if (args.length == 2)
						SettlementManager.getManager().listMembers(sender, SettlementManager.getManager().getSettlement(args[1]));
				}
				
				if (args[0].equalsIgnoreCase("kick"))
					if (args.length == 2)
						SettlementManager.getManager().kickPlayer(sender, args[1]);

			} else {
				CommandType.printList(sender);
			}
		}
		return true;
	}
}
