package humine.com.commands.voteban;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import humine.com.main.StaffMain;

public class OpenVoteBanCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length >= 2) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target != null) {
				if(!StaffMain.getInstance().getVoteBan().isInProgress()) {
					if(Bukkit.getOnlinePlayers().size() >= 5) {
						voteban(target, args);
						return true;
					}
					else
						sender.sendMessage("Pas assez de joueur connectés !");
				}
				else
					sender.sendMessage("un vote est deja en cours !");
			}
			else
				sender.sendMessage("Joueur introuvable");
		}
		else
			sender.sendMessage("/voteban <player> <reason>");
		
		return false;
	}

	private void voteban(Player target, String[] args) {
		String message = "";
		for(byte i = 1; i < args.length; i++) {
			message += args[i] + " ";
		}
		
		Bukkit.broadcastMessage(ChatColor.GOLD + "Un vote sur le banissement sur " + target.getName() + " est lancé !");
		Bukkit.broadcastMessage("Raison: " + ChatColor.DARK_AQUA + message);
		Bukkit.broadcastMessage("Êtes-vous d'accord ? §a/voteban yes§r ou §4/voteban no§r pour voter");
		StaffMain.getInstance().getVoteBan().openVote(target, message);
	}
}
