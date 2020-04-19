package com.huminecraft.huminestaff.commands.voteban;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.huminecraft.huminestaff.main.StaffMain;

public class VoteBanCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			StaffMain.sendMessage(sender, "Tu dois etre un joueur");
			return false;
		}
		
		Player player = (Player) sender;
		
		if(args.length < 1) {
			StaffMain.sendMessage(sender, "Argument insuffisant");
			StaffMain.sendMessage(sender, "/voteban <yes|no>");
			return false;
		}
		
		if(StaffMain.getVoteBan() == null || !StaffMain.getVoteBan().isInProgress()) {
			StaffMain.sendMessage(player, "Aucun vote en cours !");
			return false;
		}
		
		if(StaffMain.getVoteBan().getParticipants().containsKey(player)) {
			StaffMain.sendMessage(player, "Vous avez déjà voté(e) !");
			return false;
		}
		
		if(args[0].equalsIgnoreCase("yes")) {
			StaffMain.getVoteBan().addParticipant(player, true);
			StaffMain.sendMessage(player, "Merci d'avoir voté !");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("no")) {
			StaffMain.getVoteBan().addParticipant(player, false);
			StaffMain.sendMessage(player, "Merci d'avoir voté !");
			return true;
		}

		StaffMain.sendMessage(sender, "vote invalide");
		StaffMain.sendMessage(sender, "/voteban <yes|no>");
		return false;
	}
}
