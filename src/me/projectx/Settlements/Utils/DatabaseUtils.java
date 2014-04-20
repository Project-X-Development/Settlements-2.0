package me.projectx.Settlements.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.projectx.Settlements.Main;
import me.projectx.Settlements.MySQL.MySQL;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class DatabaseUtils extends Thread{
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

		//Connection successful....store in variable
		System.out.println("[Settlements] Attempting database connection...");
		mysql.openConnection();
		con = mysql.getConnection();
		if (mysql.checkConnection()) {
			System.out.println("[Settlements] Success!");
		}		
	}

	public static void setupMySQL() throws SQLException{
		queryOut("CREATE TABLE IF NOT EXISTS settlements(id BIGINT, name varchar(255), "
				+ "leader varchar(255), description varchar(255), citizens LONGBLOB, officers LONGBLOB, tag VARCHAR(4));");
		queryOut("CREATE TABLE IF NOT EXISTS cache(name varchar(255), id varchar(255));");
		queryOut("CREATE TABLE IF NOT EXISTS chunks(x BIGINT, z BIGINT, player VARCHAR(255), settlement BIGINT, world VARCHAR(255));");
	}

	public static void closeConnection(){
		mysql.closeConnection();
	}

	public static Connection getConnection(){
		return con;
	}

	public static void openConnection(){
		new Thread() {
			@Override
			public void run() {
				mysql.openConnection();
			}
		}.start();
	}

	//Query database using SQL Syntax
	public static ResultSet queryIn(String query) throws SQLException{
		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result;
	}

	public static void queryOut(final String query) throws SQLException{
		new Thread() {
			@Override
			public void run() {
				Statement statement;
				try {
					statement = con.createStatement();
					statement.executeUpdate(query);
				} catch(SQLException e) {
					e.printStackTrace();
				}	
			}
		}.start();
	}
}
