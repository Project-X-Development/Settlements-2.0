package me.projectx.Settlements.Managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.projectx.Settlements.Models.Players;
import me.projectx.Settlements.Utils.MessageType;

public class PlayerManager {
	
	private static PlayerManager pm = new PlayerManager();
	private ArrayList<String> allianceChat = new ArrayList<String>();
	private ArrayList<String> settlementChat = new ArrayList<String>();
	
	public static PlayerManager getInstance(){
		return pm;
	}

	private List<Players> playerobjects = new ArrayList<Players>();
	
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