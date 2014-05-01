package me.projectx.Settlements.Exceptions;

import me.projectx.Settlements.Utils.MessageType;

import org.bukkit.entity.Player;

public class NotInSettlementException extends Exception{

	private static final long serialVersionUID = -5077049051887568812L;
	
	public NotInSettlementException(Player p){
		p.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
	}
}
