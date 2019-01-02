package humine.com.commands.voteban;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import humine.com.main.StaffMain;

public class VoteBanCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length >= 1 && (args[0].equalsIgnoreCase("yes") || args[0].equalsIgnoreCase("no"))) {
				if(!StaffMain.getInstance().getVoteBan().getParticipants().containsKey(player)) {
					if(StaffMain.getInstance().getVoteBan().isInProgress()) {
						if(args[0].equalsIgnoreCase("yes"))
							StaffMain.getInstance().getVoteBan().addParticipant(player, true);
						else
							StaffMain.getInstance().getVoteBan().addParticipant(player, false);
						
						player.sendMessage("Merci d'avoir voté !");
						return true;
					}
					else
						player.sendMessage("aucun vote est en cours !");
				}
				else
					player.sendMessage("vous avez deja voté !");
			}
		}
		
		return false;
	}
}
