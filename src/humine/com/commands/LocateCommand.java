package humine.com.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import humine.com.main.StaffMain;
import main.MainWelcome;

public class LocateCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 1) {
			StaffMain.sendMessage(sender, "/where <name>");
			return false;
		}
		
		Player target = serverContains(args[0]);
		
		if(target == null) {
			StaffMain.sendMessage(sender, "Joueur introuvable");
			return false;
		}
		
		Location targetSpawn = null;
		if(StaffMain.getInstance().getServer().getPluginManager().isPluginEnabled("HumineWelcome"))
			targetSpawn = MainWelcome.getInstance().loadNewbieLocation(target);
		else
			StaffMain.sendMessage(sender, "Impossible de trouver HumineWelcome");
		
		StaffMain.sendMessage(sender, target.getName() + ":");
		StaffMain.sendMessage(sender, "Position Actuelle : " + positionToString(target.getLocation()));
		StaffMain.sendMessage(sender, "Spawn Defini : " + ((targetSpawn != null) ? positionToString(targetSpawn) : "aucun"));
		StaffMain.sendMessage(sender, "Spawn Lit : " + ((target.getBedSpawnLocation() != null) ? positionToString(target.getBedSpawnLocation()) : "aucun"));
		return true;
	}
	
	private String positionToString(Location loc) {
		return loc.getBlockX() + " | " + loc.getBlockY() + " | " + loc.getBlockZ();
	}
	
	private Player serverContains(String message) {
		for(Player player : StaffMain.getInstance().getServer().getOnlinePlayers()) {
			if(player.getName().equals(message)) return player;
		}
		return null;
	}
}
