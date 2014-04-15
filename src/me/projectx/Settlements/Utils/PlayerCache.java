package me.projectx.Settlements.Utils;

import java.util.HashMap;
import java.util.UUID;

public class PlayerCache {
	private static HashMap<String, UUID> playerMap = new HashMap<String, UUID>();
	
	public static HashMap<String, UUID> getCache(){
		return playerMap;
	}
}
