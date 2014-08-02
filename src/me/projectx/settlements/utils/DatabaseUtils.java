package me.projectx.settlements.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.projectx.settlements.Main;
import me.projectx.settlements.mysql.MySQL;

import org.bukkit.configuration.file.FileConfiguration;

/*
 * TODO Add prepared statements to prevent SQL injection
 * 
 * I honestly have no idea how to do this....
 * 
 * Someone halp plz
 * 
 */

public class DatabaseUtils {
	private static Connection con;
	private static MySQL mysql;

	public static void setupConnection() {
		FileConfiguration fc = Main.getInstance().getConfig();

		String host = fc.getString("db.host");
		String port = fc.getString("db.port");
		String db = fc.getString("db.dbname");
		String user = fc.getString("db.user");
		String pass = fc.getString("db.pass");

		mysql = new MySQL(Main.getInstance(), host, port, db, user, pass);

		System.out.println("[Settlements] Attempting database connection...");
		mysql.openConnection();
		con = mysql.getConnection();
		if (mysql.checkConnection())
			System.out.println("[Settlements] Success!");
	}

	public static void setupMySQL() throws SQLException {
		queryOut("CREATE TABLE IF NOT EXISTS settlements(id BIGINT, name varchar(255), "
				+ "leader varchar(255), description varchar(255), balance BIGINT);");
		queryOut("CREATE TABLE IF NOT EXISTS citizens(uuid varchar(255), settlement varchar(255), rank varchar(255));");
		queryOut("CREATE TABLE IF NOT EXISTS sethomes(id BIGINT, world VARCHAR(255), x BIGINT, y BIGINT, z BIGINT, yaw BIGINT, pitch BIGINT);");
		queryOut("CREATE TABLE IF NOT EXISTS chunks(x BIGINT, z BIGINT, player VARCHAR(255), settlement BIGINT, world VARCHAR(255), type VARCHAR(255));");
		queryOut("CREATE TABLE IF NOT EXISTS alliances(main BIGINT, ally BIGINT);");
		queryOut("CREATE TABLE IF NOT EXISTS wars(setA VARCHAR(255), setB VARCHAR(255));");
	}

	public static void closeConnection() {
		mysql.closeConnection();
	}

	public static Connection getConnection() {
		return con;
	}

	public static void openConnection() {
		new Thread() {
			@Override
			public void run() {
				mysql.openConnection();
			}
		}.start();
	}

	// Query database using SQL Syntax
	public static ResultSet queryIn(String query) throws SQLException {
		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(query);
		return result;
	}

	public static void queryOut(final String query) throws SQLException {
		 new Thread() {
			@Override
			public void run() {
				Statement statement;
				try {
					statement = con.createStatement();
					statement.executeUpdate(query);
					this.interrupt();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
