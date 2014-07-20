package me.projectx.Settlements.Commands;

import java.sql.SQLException;

import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Managers.PlayerManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.CommandModel;
import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Utils.PlayerUtils;
import me.projectx.Settlements.enums.ClaimType;
import me.projectx.Settlements.enums.CommandType;
import me.projectx.Settlements.enums.MessageType;

import org.bukkit.Location;
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
				switch(args[0]){
					case "create":
						if (args.length == 2)
							SettlementManager.getManager().createSettlement(args[1], sender);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s create <name>");
						break;
					case "delete":
						if (args.length == 1)
							SettlementManager.getManager().deleteSettlement(sender);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s delete");
						break;
					case "invite":
						if (args.length == 2)
							SettlementManager.getManager().inviteCitizen(args[1], sender);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s invite <player>");
						break;
					case "accept":
						if (args.length == 1)
							SettlementManager.getManager().acceptInvite(sender.getName());
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s accept");
						break;
					case "decline":
						if (args.length == 1)
							SettlementManager.getManager().declineInvite(sender.getName());
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s decline");
						break;
					case "leave":
						if (args.length == 1)
							SettlementManager.getManager().leaveSettlement(sender.getName());
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s leave");
						break;
					case "list":
						if (args.length == 1)
							SettlementManager.getManager().listSettlements((Player)sender);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s list");
						break;
					case "desc":
						if (args.length > 1){
							StringBuilder str = new StringBuilder();
							for (int i = 1; i < args.length; i++) 
								str.append(args[i] + " ");
							SettlementManager.getManager().setDescription(sender, str.toString());
						}else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s desc <Settlement description>");
						break;
					case "claim":
						if (args.length == 1)
							ChunkManager.getManager().claim((Player)sender, ClaimType.NORMAL);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s claim");
						break;
					case "map":
						if (args.length == 1)
							ChunkManager.getManager().issueMap((Player)sender);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s map");
						break;
					case "members":
						if (args.length == 1) {
							Settlement s  = SettlementManager.getManager().getPlayerSettlement(sender.getName());
							if (s != null)
								SettlementManager.getManager().displayMembers((Player)sender, s.getName());
							else
								sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
						} else if (args.length == 2) 
							SettlementManager.getManager().displayMembers((Player)sender, args[1]);
						if (args.length > 2)
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s members [Settlement]");
						break;
					case "kick":
						if (args.length == 2) 
							SettlementManager.getManager().kickPlayer(sender, args[1]);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s kick <player>");
						break;
					case "unclaim":
						if (args.length == 1){
							Player p = (Player) sender;
							Location l = p.getLocation();
							ChunkManager.getManager().unclaim(p, l.getChunk().getX(), l.getChunk().getZ(), l.getWorld(), false);
						}else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s unclaim");
						break;
					case "autoclaim":
						if (args.length == 1)
							ChunkManager.getManager().setAutoClaiming((Player)sender, ClaimType.NORMAL);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s autoclaim");
						break;
					case "chat": //needs some work
						if (args.length == 1){
							Player p = (Player) sender;
							if (args.length == 1)
								PlayerManager.getInstance().setSettlementChatting(p);
							else if (args.length == 2)
								PlayerManager.getInstance().setAllianceChatting(p);
						}else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s chat");
						break;
					case "ally":
						if (args.length == 2){
							SettlementManager sm = SettlementManager.getManager();
							sm.allySettlement(sm.getPlayerSettlement(sender.getName()), args[1]);
						}else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s ally <Settlement>");
						break;
					case "sethome":
						if (args.length == 1) 
							SettlementManager.getManager().setHome((Player)sender);
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s sethome");
						break;
					case "home":
						if (args.length == 1)
							SettlementManager.getManager().teleportToHome((Player)sender);		
						else
							sender.sendMessage(MessageType.COMMAND_INVALID_ARGS.getMsg() + "Try /s home");
						break;
					case "id":
						Player p = (Player)sender;
						p.sendMessage(PlayerUtils.getNameFromUUID(p.getUniqueId()));
						break;
					case "testclaim":
						if (args.length == 1)
							ChunkManager.getManager().claim((Player)sender, ClaimType.NORMAL);
						else if (args.length == 2)
							ChunkManager.getManager().claim((Player)sender, ClaimType.SAFEZONE);
						break;
					default:
						sender.sendMessage(MessageType.COMMAND_INVALID_ARGUMENT.getMsg() + " Type /s for help");
						break;
				}
			} else if (sender instanceof Player)
				CommandType.displayCommands((Player)sender);
			else 
				CommandType.printList(sender);
		}
		return true;
	}
}