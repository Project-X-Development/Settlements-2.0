package me.projectx.settlements.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.models.Players;

import org.bukkit.entity.Player;

public class PlayerManager {
	
	private static PlayerManager pm = new PlayerManager();
	private List<String> allianceChat = new ArrayList<String>();
	private List<String> settlementChat = new ArrayList<String>();
	private List<Players> playerobjects = new ArrayList<Players>();
	private List<UUID> override = new ArrayList<UUID>();
	
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
		this.settlementChat.remove(player.getName());
		this.allianceChat.remove(player.getName());
		this.override.remove(player.getUniqueId());
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
	
	public void setAdminOverride(Player player){
		UUID id = player.getUniqueId();
		if (override.contains(id)){
			override.remove(id);
			player.sendMessage(MessageType.ADMIN_OVERRIDE_END.getMsg());
		}else{
			override.add(id);
			player.sendMessage(MessageType.ADMIN_OVERRIDE_START.getMsg());
		}
	}
	
	public boolean hasAdminOverride(Player player){
		return override.contains(player.getUniqueId());
	}
}
