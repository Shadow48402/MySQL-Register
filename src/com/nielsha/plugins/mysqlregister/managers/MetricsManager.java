package com.nielsha.plugins.mysqlregister.managers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.nielsha.plugins.mysqlregister.Core;

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
						String ip = Bukkit.getServer().getIp();
						String port = String.valueOf(Bukkit.getServer().getPort());
						String online = String.valueOf(Bukkit.getServer().getOnlinePlayers().length);
						
						Core.console("Sending HTTP POST");
						String urlParameters  = "ip=" + ip;
						urlParameters		  += "&port=" + port;
						urlParameters		  += "&online=" + online;
						byte[] postData       = urlParameters.getBytes( Charset.forName( "UTF-8" ));
						int    postDataLength = postData.length;
						String request        = "http://data.nielsha.com/index.php";
						URL url;
						url = new URL( request );
						HttpURLConnection cox = (HttpURLConnection) url.openConnection();           
						cox.setDoOutput( true );
						cox.setDoInput ( true );
						cox.setInstanceFollowRedirects( false );
						cox.setRequestMethod( "POST" );
						cox.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
						cox.setRequestProperty( "charset", "utf-8");
						cox.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
						cox.setUseCaches( false );
						try( DataOutputStream wr = new DataOutputStream( cox.getOutputStream())) {
							wr.write( postData );
						} catch (IOException e) {
							e.printStackTrace();
						}
						Core.console("Sent HTTP POST");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			, 1L, 1200L);
		}
	}

}
