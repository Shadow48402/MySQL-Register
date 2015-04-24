package com.nielsha.plugins.mysqlregister.managers;

import java.io.File;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.nielsha.plugins.mysqlregister.Protocol;

public class MetricsManager {

	private JavaPlugin plugin;
	private boolean isEnabled = true;

	public MetricsManager(JavaPlugin plugin) {
		this.plugin = plugin;
		try {
			File folder = new File(this.plugin.getDataFolder() + File.separator + "Data");
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File file = new File(this.plugin.getDataFolder() + File.separator + "Data" + File.separator + "Stats.yml");
			YamlConfiguration yaml;
			if (!file.exists()) {
				file.createNewFile();
				yaml = YamlConfiguration.loadConfiguration(file);
				yaml.set("Enabled", Boolean.valueOf(true));
				yaml.save(file);
			} else {
				yaml = YamlConfiguration.loadConfiguration(file);
			}
			this.isEnabled = yaml.getBoolean("Enabled");
		} catch (Exception e) {
			e.printStackTrace();
			this.isEnabled = true;
		}
	}

	public void start() { 
		if (this.isEnabled) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable()
			{
				@SuppressWarnings("deprecation")
				public void run() {
					try {
						InetAddress address = InetAddress.getByName("data.nielsha.com");
						Socket socket = new Socket(address, 16193);
						ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
						Protocol protocol = new Protocol(
								Bukkit.getIp(), 
								Bukkit.getPort(), 
								Bukkit.getMotd(), 
								Bukkit.getVersion(), 
								Bukkit.getOnlinePlayers().length, 
								Bukkit.getMaxPlayers(), 
								System.currentTimeMillis()
						);
						out.writeObject(protocol);
						out.flush();
						socket.close();
					}
					catch (Exception localException){
						localException.printStackTrace();
					}
				}
			}
			, 1L, 12000L);
		}
	}

}
