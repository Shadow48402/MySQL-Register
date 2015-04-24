package com.nielsha.plugins.mysqlregister.commands;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.nielsha.plugins.mysqlregister.Core;
import com.nielsha.plugins.mysqlregister.managers.CoreManager;
import com.nielsha.plugins.mysqlregister.managers.MessageManager;
import com.nielsha.plugins.mysqlregister.managers.MysqlManager;

public class RegisterCommand implements CommandExecutor {
	public Core plugin;
	public RegisterCommand(Core plugin){
		this.plugin = plugin;
	}
	static File f;
	static FileConfiguration c;
	
	public static void setConfig(FileConfiguration config){
		c = config;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			return true;
		}
		Player p = (Player) sender;
		
		if(!p.hasPermission("MysqlRegister.register")){
			p.sendMessage(MessageManager.getMessage("NO_PERMISSION"));
			return true;
		}

		if(args.length != 3){
			p.sendMessage(MessageManager.getMessage("WRONG_ARGUMENTS"));
			return true;
		}

		try {
			Statement s = MysqlManager.getConnection().createStatement();
			ResultSet set = s.executeQuery("SELECT * FROM users WHERE uuid='" + p.getUniqueId().toString() + "'");
			if(set.first()) {
				p.sendMessage(MessageManager.getMessage("ALREADY_REGISTERED"));
				return true;
			}

			if(!args[1].equalsIgnoreCase(args[2])){
				p.sendMessage(MessageManager.getMessage("PASSWORDS_NOT_EQUAL"));
				return true;
			}
			
			if(!RegisterCommand.validate(args[0])){
				p.sendMessage(MessageManager.getMessage("INVALID_EMAIL"));
				return true;
			}

			if(c.getString("Hash") == null)
				Core.console( "HASH NAME IS NOT FOUND" );
			PreparedStatement pre = MysqlManager.getConnection().prepareStatement("INSERT INTO `" 
				+ MysqlManager.getInfo(plugin, "Options.table-name") 
				+ "` (" + MysqlManager.getInfo(plugin, "Options.user-column") 
				+ ", "
				+ MysqlManager.getInfo(plugin, "Options.password-column") 
				+ ", "
				+ MysqlManager.getInfo(plugin, "Options.uuid-column") 
				+ ", "
				+ MysqlManager.getInfo(plugin, "Options.email-column") 
				+ ") VALUES (?, ?, ?, ?)");
			pre.setString(1, p.getName());
			pre.setString(2, CoreManager.hash(args[1], c.getString("Hash")));
			pre.setString(3, p.getUniqueId().toString());
			pre.setString(4, args[0]);
			pre.execute();
			
			p.sendMessage(MessageManager.getMessage("REGISTER_SUCCESS"));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	public static boolean validate(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
		return matcher.find();
	}

}
