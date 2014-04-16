package me.projectx.Settlements.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.projectx.Settlements.Main;
import me.projectx.Settlements.MySQL.MySQL;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class DatabaseUtils {
	public static Connection con;

	public static void setupConnection(){
		//Get database values from config
		Plugin pl = Main.getInstance();
		FileConfiguration fc = pl.getConfig();

		String host = fc.getString("db.host");
		String port = fc.getString("db.port");
		String db = fc.getString("db.dbname");
		String user = fc.getString("db.user");
		String pass = fc.getString("db.pass");

		//Create SQL object for database
		MySQL mysql = new MySQL(pl, host, port, db, user, pass);
		mysql.openConnection();
		
		if (mysql.checkConnection()){
			//Connection successful....store in variable
			con = mysql.getConnection();
		}
		else{
			//Could not connect...throw error!
			pl.getLogger().severe(ChatColor.RED + "Could not connect to database!");
		}
	}
	
	public static void setupMySQL(){
		
	}

	public static Connection getConnection(){
		return con;
	}

	//Query database using SQL Syntax
	public static ResultSet query(String query) throws SQLException{
		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result;
	}
}
