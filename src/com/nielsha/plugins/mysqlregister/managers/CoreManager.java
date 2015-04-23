package com.nielsha.plugins.mysqlregister.managers;

import java.security.MessageDigest;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.nielsha.plugin.mysqlregister.Core;

public class CoreManager {

	// Debug Test
	public static void main(String[] args){
		Core.console(hash("test", "MD5"));
	}

	public static void registerListenenrs(Listener... l){
		for(Listener listeners : l){
			Bukkit.getPluginManager().registerEvents(listeners, new Core());
		}
	}

	public static String hash(String password, String hashName) {
		try {
			MessageDigest md = MessageDigest.getInstance(hashName);
			byte[] array = md.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
