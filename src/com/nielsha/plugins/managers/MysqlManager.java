package com.nielsha.plugins.managers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nielsha.plugins.Core;
import com.nielsha.plugins.mysql.MySQL;

public class MysqlManager {
	static Connection c = null;
	
	public static void setup() throws SQLException{
		if(!new File(new Core().getDataFolder().getAbsolutePath(), "database.yml").exists()){
			File f = new File(new Core().getDataFolder().getAbsolutePath(), "database.yml");
			FileConfiguration c = YamlConfiguration.loadConfiguration(f);
			c.set("Host", "HostName");
			c.set("Port", "Port");
			c.set("Database", "Database");
			c.set("Username", "User");
			c.set("Password", "Password");
			try {
				c.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		MySQL mysql = new MySQL(
				new Core(), 
				getInfo("Host"), 
				getInfo("Port"), 
				getInfo("Database"), 
				getInfo("Username"),
				getInfo("Password"));
		try {
			c = mysql.openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if(c.isClosed()) {
			Core.console("MySQL-Register couldn't connect with the database!");
			Bukkit.getPluginManager().disablePlugin(new Core());
			return;
		}
		Statement s = c.createStatement();
		s.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
				+ "id INT NOT NULL AUTO_INCREMENT,"
				+ "Username VARCHAR(200),"
				+ "Password VARCHAR(200),"
				+ "UUID VARCHAR(36),"
				+ "Email VARCHAR(200),"
				+ "PRIMARY KEY (id) )");
	}
	
	public static String getInfo(String s){
		File f = new File(new Core().getDataFolder().getAbsolutePath(), "database.yml");
		FileConfiguration c = YamlConfiguration.loadConfiguration(f);
		return c.getString(s);
	}
	
	public static Connection getConnection(){
		return c;
	}

}
