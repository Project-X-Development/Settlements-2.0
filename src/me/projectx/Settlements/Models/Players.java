package me.projectx.Settlements.Models;

import org.bukkit.entity.Player;

import me.projectx.Settlements.Utils.Storage;

public class Players extends Storage{

	public Players(Player player){
		setString("uuid", player.getUniqueId().toString());
	}
}
