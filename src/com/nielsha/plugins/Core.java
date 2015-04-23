package com.nielsha.plugins;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import com.nielsha.plugins.commands.RegisterCommand;
import com.nielsha.plugins.listeners.JoinEvent;
import com.nielsha.plugins.managers.CoreManager;
import com.nielsha.plugins.managers.MessageManager;
import com.nielsha.plugins.managers.MysqlManager;

public class Core extends JavaPlugin {
	
	public void onEnable(){
		// Register events
		CoreManager.registerListenenrs(
				new JoinEvent()
		);
		
		
		try {
			// Setup MySQL
			MysqlManager.setup();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Config defaults
		if(!new File(this.getDataFolder().getAbsolutePath(), "config.yml").exists()){
			this.getConfig().options().copyDefaults(true);
			try {
				this.getConfig().save(new File(this.getDataFolder().getAbsolutePath(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		new MessageManager().setup();
		
		getCommand("register").setExecutor(new RegisterCommand());
	}
	
	public void onDisable(){
		try {
			MysqlManager.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void console(String s){
		System.out.println(s);
	}

}
