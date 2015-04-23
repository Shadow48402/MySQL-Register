package com.nielsha.plugins.mysqlregister.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.nielsha.plugins.mysqlregister.Core;

public class MessageManager {
	static File f;
	static FileConfiguration c;

	public static void setup(Plugin pl) {
		f = new File(pl.getDataFolder().getAbsolutePath(), "message.yml");
		c = YamlConfiguration.loadConfiguration(f);
		c.options().copyDefaults(true);
		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getMessage(String s){
		if(c.contains(s))
			return ChatColor.translateAlternateColorCodes('&', c.getString(s));
		else {
			Core.console( "Can't find message '" + s + "'" );
			return "NULL";
		}
	}
}
