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
	public static MySQL mysql;

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
		mysql = new MySQL(pl, host, port, db, user, pass);
		
		if (mysql.checkConnection()){
			//Connection successful....store in variable
			con = mysql.openConnection();
		}
		else{
			//Could not connect...throw error!
			pl.getLogger().severe(ChatColor.RED + "Could not connect to database!");
		}
	}
	
	public static void setupMySQL() throws SQLException{
		queryOut("CREATE TABLE IF NOT EXISTS settlement"
				+ "("
				+ "id BIGINT,"
				+ "name varchar(255),"
				+ "leader varchar(255),"
				+ "description varchar(255),"
				+ "citizens LONGBLOB,"
				+ "officers LONGBLOB"
				+ ");");
		
		queryOut("CREATE TABLE IF NOT EXISTS cache"
				+ "("
				+ "name varchar(255),"
				+ "id varchar(255)"
				+ ");");
	}
	
	public static void closeConnection(){
		mysql.closeConnection();
	}

	public static Connection getConnection(){
		return con;
	}

	//Query database using SQL Syntax
	public static ResultSet queryIn(String query) throws SQLException{
		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result;
	}
	
	//Query database using SQL Syntax
	public static void queryOut(String query) throws SQLException{
		Statement statement = con.createStatement();
		statement.executeUpdate(query);
	}
}
