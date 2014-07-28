package me.projectx.settlements.models;

import org.bukkit.entity.Player;

import me.projectx.settlements.utils.Storage;

public class Players extends Storage{

	public Players(Player player){
		setString("uuid", player.getUniqueId().toString());
	}
}
