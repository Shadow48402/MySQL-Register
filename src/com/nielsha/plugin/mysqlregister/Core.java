package com.nielsha.plugin.mysqlregister;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import com.nielsha.plugins.mysqlregister.commands.RegisterCommand;
import com.nielsha.plugins.mysqlregister.listeners.JoinEvent;
import com.nielsha.plugins.mysqlregister.managers.CoreManager;
import com.nielsha.plugins.mysqlregister.managers.MessageManager;
import com.nielsha.plugins.mysqlregister.managers.MysqlManager;

public class Core extends JavaPlugin {

	public void onEnable(){
		// Register events
		CoreManager.registerListenenrs(
				this,
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

		MessageManager.setup();
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