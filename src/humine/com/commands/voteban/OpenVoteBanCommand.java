package humine.com.commands.voteban;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import humine.com.main.StaffMain;
import humine.com.main.VoteBan;

public class OpenVoteBanCommand implements CommandExecutor{

	private static final int MIN_PLAYER = 1;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			StaffMain.sendMessage(sender, "Tu dois etre un joueur");
			return false;
		}
		
		if(args.length < 2) {
			StaffMain.sendMessage(sender, "Argument insuffisant");
			StaffMain.sendMessage(sender, "/openvoteban <player> <reason>");
			return false;
		}
		
		if(StaffMain.getVoteBan() != null && StaffMain.getVoteBan().isInProgress()) {
			StaffMain.sendMessage(sender, "un vote est deja en cours !");
			return false;
		}
		
		int onlinePlayer = StaffMain.getInstance().getServer().getOnlinePlayers().size();
		if(onlinePlayer < MIN_PLAYER) {
			StaffMain.sendMessage(sender, "Pas assez de joueur connectés ! il faut être " + MIN_PLAYER + " minimum(s)");
			return false;
		}
		
		Player target = StaffMain.getInstance().getServer().getPlayer(args[0]);
		if(target == null) {
			StaffMain.sendMessage(sender, "Joueur introuvable");
			return false;
		}
		
		String reason = args[1];
		for(byte i = 2; i < args.length; i++) {
			reason += " " + args[i];
		}
		
		VoteBan voteBan = new VoteBan((Player) sender, target, reason);
		StaffMain.setVoteBan(voteBan);
		voteBan.openVote();
		return true;
	}
}
