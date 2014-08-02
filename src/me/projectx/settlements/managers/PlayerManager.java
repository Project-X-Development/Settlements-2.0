package me.projectx.settlements.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.models.Players;

public class PlayerManager {
	
	private static PlayerManager pm = new PlayerManager();
	private List<String> allianceChat = new ArrayList<String>();
	private List<String> settlementChat = new ArrayList<String>();
	private List<Players> playerobjects = new ArrayList<Players>();
	
	public static PlayerManager getInstance(){
		return pm;
	}
	
	public Players addPlayer(Player player){
		Players pl = new Players(player);
		this.playerobjects.add(pl);
		return pl;
	}
	
	public Players getPlayer(Player player){
		for(Players p : playerobjects){
			if(p.getString("uuid").equals(player.getUniqueId().toString())){
				return p; 
			}
		}
		return addPlayer(player);
	}
	
	public void removePlayer(Player player){
		this.playerobjects.remove(getPlayer(player));
	}
	
	public void setAllianceChatting(Player player){
		if (allianceChat.contains(player.getName())) {
			allianceChat.remove(player.getName());
			player.sendMessage(MessageType.ALLIANCE_CHAT_END.getMsg());
		}else{
			allianceChat.add(player.getName());
			player.sendMessage(MessageType.ALLIANCE_CHAT_START.getMsg());
		}
	}
	
	public boolean isAllianceChatting(Player player){
		return allianceChat.contains(player.getName());
	}
	
	public void setSettlementChatting(Player player){
		if (settlementChat.contains(player.getName())){
			settlementChat.remove(player.getName());
			player.sendMessage(MessageType.SETTLEMENT_CHAT_END.getMsg());
		}else{
			settlementChat.add(player.getName());
			player.sendMessage(MessageType.SETTLEMENT_CHAT_START.getMsg());
		}
	}
	
	public boolean isSettlementChatting(Player player){
		return settlementChat.contains(player.getName());
	}
}
