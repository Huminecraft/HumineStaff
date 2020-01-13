package humine.com.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import humine.com.main.StaffMain;

public class LocateCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 1) {
			StaffMain.sendMessage(sender, "/locate <name>");
			return false;
		}
		
		Player target = serverContains(args[0]);
		
		if(target == null) {
			StaffMain.sendMessage(sender, "Joueur introuvable");
			return false;
		}
		
		StaffMain.sendMessage(sender, "Position de " + target.getName() + ": " + target.getLocation().getX() + " | " + target.getLocation().getY() + " | " + target.getLocation().getZ());
		
		return true;
	}
	
	private Player serverContains(String message) {
		for(Player player : StaffMain.getInstance().getServer().getOnlinePlayers()) {
			if(player.getName().equals(message)) return player;
		}
		return null;
	}
}
