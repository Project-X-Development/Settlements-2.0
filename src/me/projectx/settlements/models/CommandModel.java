package me.projectx.settlements.models;

import java.sql.SQLException;

import me.projectx.settlements.enums.MessageType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class CommandModel implements CommandExecutor{

	private String perms, usage;
	
	public CommandModel(String permission, String usage){
		this.perms = permission;
		this.usage = usage;
	}
	
	public abstract boolean onCmd(CommandSender sender, String cml, String[] args) throws SQLException;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender.hasPermission(perms))){
			sender.sendMessage(MessageType.NO_PERM.getMsg());
			return false;
		}
		
		try {
			if (!(onCmd(sender, commandLabel, args))){
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Correct usage: " + ChatColor.AQUA + usage);
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}