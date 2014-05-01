package me.projectx.Settlements.Exceptions;

import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.entity.Player;

public class ChunkAlreadyClaimedException extends Exception {	
	
	private static final long serialVersionUID = 4279049471809474352L;

	public ChunkAlreadyClaimedException(Player p){
		p.sendMessage(MessageType.CHUNK_CLAIM_OWNED.getMsg());
	}
}
