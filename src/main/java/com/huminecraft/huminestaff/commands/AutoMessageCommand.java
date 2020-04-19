package com.huminecraft.huminestaff.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.huminecraft.huminestaff.main.StaffMain;

public class AutoMessageCommand implements CommandExecutor{

	/*
	 *	/automessage add <message>
	 *	/automessage remove <numero>
	 *	/automessage list
	 *	/automessage delay <number>
	 *	/automessage <true|false>
	 *	/automessage help
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("add") && args.length >= 2)
				addMessage(sender, args);
			else if(args[0].equalsIgnoreCase("remove") && args.length >= 2)
				removeMessage(sender, args[1]);
			else if(args[0].equalsIgnoreCase("list"))
				showList(sender);
			else if(args[0].equalsIgnoreCase("delay") && args.length >= 2)
				changeDelay(sender, args[1]);
			else if(args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false"))
				changeLoop(sender, args[0]);
			else if(args[0].equalsIgnoreCase("help"))
				showHelp(sender);
			else if(args[0].equalsIgnoreCase("refresh")) {
				StaffMain.getAutoMessage().refresh();
				StaffMain.sendMessage(sender, "AutoMessage refresh");
			}
			
			return true;
		}
		else {
			StaffMain.sendMessage(sender, "Pas assez d'argument !");
			StaffMain.sendMessage(sender, "/automessage help");
		}
		
		return false;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage("/automessage add §3<message>");
		sender.sendMessage("/automessage remove §3<numero>");
		sender.sendMessage("/automessage list");
		sender.sendMessage("/automessage refresh");
		sender.sendMessage("/automessage delay §3<number>");
		sender.sendMessage("/automessage <true|false>");
		sender.sendMessage("/automessage help");
	}

	private void changeLoop(CommandSender sender, String bool) {
		if(bool.equalsIgnoreCase("true"))
			StaffMain.getAutoMessage().setLoop(true);
		else if(bool.equalsIgnoreCase("false"))
			StaffMain.getAutoMessage().setLoop(false);
		
		StaffMain.sendMessage(sender, "Changement effectué !");
	}

	private void changeDelay(CommandSender sender, String number) {
		if(isNumber(number)) {
			int n = Integer.parseInt(number);
			StaffMain.getAutoMessage().setDelay(n);
			StaffMain.sendMessage(sender, "Delay modifié à " + n + " seconde(s)");
		}
		else
			StaffMain.sendMessage(sender, "chiffre non valide");
	}

	private void showList(CommandSender sender) {
		StaffMain.sendMessage(sender, "==============");
		for(short i = 0; i < StaffMain.getAutoMessage().getMessages().size(); i++) {
			sender.sendMessage(i + ": " + StaffMain.getAutoMessage().getMessages().get(i));
		}
		StaffMain.sendMessage(sender, "==============");
	}

	private void removeMessage(CommandSender sender, String number) {
		if(isNumber(number)) {
			int n = Integer.parseInt(number);
			if(n < StaffMain.getAutoMessage().getMessages().size()) {
				StaffMain.getAutoMessage().removeMessage(n);
				StaffMain.sendMessage(sender, "Message supprimé !");
			}
			else
				StaffMain.sendMessage(sender, "Message inexistant");
		}
		else
			StaffMain.sendMessage(sender, "chiffre non valide");
	}

	private boolean isNumber(String phrase) {
		for(byte i = 0; i < phrase.length(); i++) {
			if(phrase.charAt(i) < '0' || phrase.charAt(i) > '9') {
				return false;
			}
		}
		return true;
	}

	private void addMessage(CommandSender sender, String[] args) {
		String message = "";
		for(byte i = 1; i < args.length; i++)
			message += args[i] + " ";
		StaffMain.getAutoMessage().addMessage(message);
		StaffMain.sendMessage(sender, "Message enregistré !");
	}
}
