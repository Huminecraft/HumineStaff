package com.huminecraft.huminestaff.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.huminecraft.huminestaff.main.TPS;


public class TPSCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 0) {
		    if (args[0].equalsIgnoreCase("enable")) {
			TPS.enabled = true;
		    } else if (args[0].equalsIgnoreCase("disable")) {
			TPS.enabled = false;
		    } else if (args[0].equalsIgnoreCase("status")) {
		    	TPS.getTPS();
			}
		}
		else {
		    sender.sendMessage("Erreur, veuillez préciser si vous souhaiter activer ou désactiver le monitoring");
		    return false;
		}
	return true;
    }
}
