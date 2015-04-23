package com.nielsha.plugins.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nielsha.plugins.Core;

public class MessageManager {
	public File f;
	public FileConfiguration c;

	public void setup() {
		f = new File(new Core().getDataFolder().getAbsolutePath(), "message.yml");
		c = YamlConfiguration.loadConfiguration(f);
		c.options().copyDefaults(true);
		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getMessage(String s){
		if(new MessageManager().c.contains(s))
			return new MessageManager().c.getString(s);
		else {
			Core.console( "Can't find message '" + s + "'" );
			return "NULL";
		}
	}
}
