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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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
				if (args[0].equalsIgnoreCase("list")) {
					SettlementManager.getManager().listSettlements((Player)sender);
				}
				if (args[0].equalsIgnoreCase("desc")){
					StringBuilder str = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						str.append(args[i] + " ");
					}
					SettlementManager.getManager().setDescription(sender, str.toString());
				}

				if (args[0].equalsIgnoreCase("claim")){
					if (sender instanceof Player){
						if (args.length == 1){
							Player p = (Player) sender;
							ChunkManager.getInstance().claimChunk(p, ClaimType.NORMAL);
						}
					} else {
						sender.sendMessage(MessageType.NOT_PLAYER.getMsg());
					}
				}

				if (args[0].equalsIgnoreCase("map")){
					if (sender instanceof Player){
						Player p = (Player) sender;
						MapManager.getInstance().remove(p);
						ItemStack item = new ItemStack(Material.MAP, 1, (short)0);
						MapView m = Bukkit.getServer().getMap(item.getDurability());
						for(org.bukkit.map.MapRenderer r : m.getRenderers()){
							m.removeRenderer(r);
						}
						m.addRenderer(MapManager.getInstance().getRenderMap());
						p.getInventory().addItem(item);
						//ChunkManager.getInstance().printMap(p);
					} else {
						sender.sendMessage(MessageType.NOT_PLAYER.getMsg());
					}
				}

				if (args[0].equalsIgnoreCase("members")){
					if (args.length == 1) {
						Settlement s  = SettlementManager.getManager().getPlayerSettlement(sender.getName());
						if (s != null){
							SettlementManager.getManager().displayMembers((Player)sender, s.getName());
						}else{
							sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
						}
					} else if (args.length == 2) {
						SettlementManager.getManager().displayMembers((Player)sender, args[1]);
					}
				}

				if (args[0].equalsIgnoreCase("kick")){
					if (args.length == 2) {
						SettlementManager.getManager().kickPlayer(sender, args[1]);
					}
				}

				if (args[0].equalsIgnoreCase("power")){//temporary
					SettlementManager.getManager().calculatePower(SettlementManager.getManager().getPlayerSettlement(sender.getName()));
					sender.sendMessage("Your settlement power: " + SettlementManager.getManager().getPlayerSettlement(sender.getName()).getPower());
				}

				if (args[0].equalsIgnoreCase("unclaim")){
					if (sender instanceof Player){
						Player p = (Player) sender;
						if (ChunkManager.getInstance().unclaimChunk(p.getName(), p.getLocation().getChunk().getX(), p.getLocation().getChunk().getZ())) {
							p.sendMessage(MessageType.CHUNK_UNCLAIM_SUCCESS.getMsg());
						} else {
							p.sendMessage(MessageType.CHUNK_UNCLAIM_FAIL.getMsg());
						}
					}
				}
				
				if (args[0].equalsIgnoreCase("autoclaim")){
					ChunkManager.getInstance().setAutoClaiming((Player)sender);
				}
				
				if (args[0].equalsIgnoreCase("chat")){
					Player p = (Player) sender;
					if (args.length == 1){
						PlayerManager.getInstance().setSettlementChatting(p);
					}else if (args.length == 2){
						PlayerManager.getInstance().setAllianceChatting(p);
					}
				}
				
				if (args[0].equalsIgnoreCase("ally")){
					if (args.length == 2){
						SettlementManager.getManager().allySettlement(SettlementManager.getManager().getPlayerSettlement(sender.getName()), args[1]);
					}
				}
			} else {
				CommandType.printList(sender);
			}
		}
		return true;
	}
}
