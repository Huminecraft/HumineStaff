package humine.com.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import humine.com.main.FileManager;
import humine.com.main.StaffMain;

public class OpenInventoryCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length >= 1) {
				Player target = Bukkit.getPlayer(args[0]);
				
				if(target != null)
					player.openInventory(target.getInventory());
				else
				{
					Inventory inv = FileManager.getPlayerInventory(args[0]);
					
					if(inv != null)
						player.openInventory(inv);
					else
						StaffMain.sendMessage(player, "Joueur inexistant !");
				}
				
				return true;
			}
			StaffMain.sendMessage(player, "Pas assez d'argument");
		}
		else
			StaffMain.sendMessage(sender, "Seulement les joueurs peuvent l'executer");
		
		return false;
	}

}
