package com.huminecraft.huminestaff.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.huminecraft.huminestaff.main.StaffMain;

public class SOSCommand implements CommandExecutor {

	private static final String PERMISSION = "huminestaff.admin.sos";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			StaffMain.sendMessage(sender, "Vous devez être un joueur");
			return false;
		}
		
		if(args.length == 0) {
			StaffMain.sendMessage(sender, "/sos <message>");
			return false;
		}
		
		String message = args[0];
		for(int i = 1; i < args.length; i++) message += " " + args[i];
		
		for(Player player : StaffMain.getInstance().getServer().getOnlinePlayers()) {
			if(player.hasPermission(PERMISSION) || player.isOp())
				sendMessage(player, message);
		}
		
		return true;
	}
	
	private void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.RED+"===["+ChatColor.GOLD+"SOS"+ChatColor.RED+"]===");
		player.sendMessage(player.getName() + " : " + ChatColor.AQUA + message);
		player.sendMessage(ChatColor.RED+"===========");
	}
}
