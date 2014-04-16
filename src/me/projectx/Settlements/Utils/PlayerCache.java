package me.projectx.Settlements.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import me.projectx.Settlements.Utils.DatabaseUtils;

public class PlayerCache {
	private static HashMap<String, UUID> playerMap = new HashMap<String, UUID>();

	public static HashMap<String, UUID> getCache(){
		return playerMap;
	}

	//Backsup PlayerName : UUID database - Can bog down the server a bit
	public static void backupCache() throws SQLException{
		for (String name : playerMap.keySet()){
			DatabaseUtils.queryOut("INSERT INTO cache (`name`, `UUID`) VALUES ('" + name + "', '" + playerMap.get(name).toString() + "');");
		}
	}

	//Loads cache from backup database - Can bog down the server a bit
	public static void loadCache() throws SQLException{
		ResultSet result = DatabaseUtils.queryIn("SELECT * FROM cache;");
		HashMap<String, UUID> tempMap = new HashMap<String, UUID>();
		while (result.next()){
			String user = result.getString("name");
			String uuidstring = result.getString("UUID");
			UUID tempId = UUID.fromString(uuidstring);
			tempMap.put(user, tempId);
		}
		playerMap = tempMap;
		tempMap.clear();
	}
}
