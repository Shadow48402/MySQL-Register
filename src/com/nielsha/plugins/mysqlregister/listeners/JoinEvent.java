package com.nielsha.plugins.mysqlregister.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.nielsha.plugins.mysqlregister.Core;
import com.nielsha.plugins.mysqlregister.managers.MessageManager;
import com.nielsha.plugins.mysqlregister.managers.MysqlManager;

public class JoinEvent implements Listener{

	public Core plugin;
	public JoinEvent(Core plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		try {
			MysqlManager.CheckConnection();
			Statement s = MysqlManager.getConnection().createStatement();
			ResultSet set = s.executeQuery("SELECT * FROM " 
					+ MysqlManager.getInfo(plugin, "Options.table-name") 
					+ " WHERE " 
					+ MysqlManager.getInfo(plugin, "Options.uuid-column") 
					+ "='" + p.getUniqueId().toString() + "'");
			if(set.first()) {
				return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		p.sendMessage(MessageManager.getMessage("REGISTER_NOW"));
	}

}
