package com.nielsha.plugins.mysqlregister.managers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.nielsha.plugins.mysqlregister.Core;
import com.nielsha.plugins.mysqlregister.mysql.MySQL;

public class MysqlManager {
	static Connection c = null;
	static boolean connected = false;
	public static boolean isConnected(){
		return connected;
	}
	static boolean close = false;
	public static boolean needsClose(){
		return close;
	}
	
	public static void setup(Plugin pl) throws SQLException{
		if(!new File(pl.getDataFolder().getAbsolutePath(), "database.yml").exists()){
			File f = new File(pl.getDataFolder().getAbsolutePath(), "database.yml");
			FileConfiguration c = YamlConfiguration.loadConfiguration(f);
			c.set("Host", "localhost");
			c.set("Port", "3306");
			c.set("Database", "Database");
			c.set("Username", "User");
			c.set("Password", "Password");
			try {
				c.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Core.console("MySQL-Register couldn't connect with the database!");
			Bukkit.getPluginManager().disablePlugin(pl);
			return;
		}
		MySQL mysql = new MySQL(
				pl, 
				getInfo(pl, "Host"), 
				getInfo(pl, "Port"), 
				getInfo(pl, "Database"), 
				getInfo(pl, "Username"),
				getInfo(pl, "Password"));
		try {
			c = mysql.openConnection();
			connected = true;
		} catch (ClassNotFoundException | SQLException e) {
			connected = false;
		}
		if(!isConnected()){
			Core.console("MySQL-Register couldn't connect with the database!");
			Bukkit.getPluginManager().disablePlugin(pl);
			return;
		}
		close = true;
		Statement s = c.createStatement();
		Core.console("Creating table 'users'");
		s.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
				+ "id INT NOT NULL AUTO_INCREMENT,"
				+ "Username VARCHAR(200),"
				+ "Password VARCHAR(200),"
				+ "UUID VARCHAR(36),"
				+ "Email VARCHAR(200),"
				+ "PRIMARY KEY (id) )");
		Core.console("Created table 'users'");
	}
	
	public static String getInfo(Plugin pl, String s){
		File f = new File(pl.getDataFolder().getAbsolutePath(), "database.yml");
		FileConfiguration c = YamlConfiguration.loadConfiguration(f);
		return c.getString(s);
	}
	
	public static Connection getConnection(){
		return c;
	}

}
