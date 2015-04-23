package com.nielsha.plugins.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nielsha.plugins.Core;
import com.nielsha.plugins.managers.CoreManager;
import com.nielsha.plugins.managers.MessageManager;
import com.nielsha.plugins.managers.MysqlManager;

public class RegisterCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			return true;
		}
		Player p = (Player) sender;

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
			
			if(!RegisterCommand.validate(args[0]))
				return true;

			PreparedStatement pre = MysqlManager.getConnection().prepareStatement("INSERT INTO `users` (Username, Password, UUID, Email) VALUES (?, ?, ?, ?)");
			pre.setString(1, p.getName());
			pre.setString(2, CoreManager.hash(args[0], new Core().getConfig().getString("Hash")));
			pre.setString(3, p.getUniqueId().toString());
			pre.setString(4, args[0]);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
		return matcher.find();
	}

}
