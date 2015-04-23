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
		if(!f.exists()){
			c = YamlConfiguration.loadConfiguration(f);
			c.set("WRONG_ARGUMENTS", "&c/register [Email] [Password] [Password Confirmation]");
			c.set("ALREADY_REGISTERED", "&cYou are already registered!");
			c.set("PASSWORDS_NOT_EQUAL", "&cThe two passwords supplied do not match!");
			c.set("REGISTER_NOW", "&aRegister now a account for more features!");
			c.set("REGISTER_SUCCESS", "&aRegistration successful!");
			c.set("INVALID_EMAIL", "&cPlease enter a valid email!");
			
			try {
				c.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			c = YamlConfiguration.loadConfiguration(f);
		}
	}

	public static String getMessage(String s){
		if(c.getString(s) != null) {
			return ChatColor.translateAlternateColorCodes('&', c.getString(s));
		} else {
			Core.console( "Can't find message '" + s + "'" );
			return "NULL";
		}
	}
}
