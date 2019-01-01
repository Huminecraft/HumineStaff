package humine.com.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import humine.com.main.Message;
import humine.com.main.StaffMain;

public class VanishCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			if(!StaffMain.getInstance().getVanished().contains(player)) {
				vanish(player);
			}
			else {
				unvanish(player);
			}
			
		}
		
		return false;
	}
	
	private void vanish(Player player)
	{
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.hidePlayer(StaffMain.getInstance(), player);
		}
		
		StaffMain.getInstance().getVanished().add(player);
		
		String message = Message.getMessageQuit();
		message = message.replace("{PLAYER}", player.getName());
		Bukkit.broadcastMessage(message);
	}
	
	private void unvanish(Player player)
	{
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(StaffMain.getInstance(), player);
		}
		
		StaffMain.getInstance().getVanished().remove(player);
		
		String message = Message.getMessageJoin();
		message = message.replace("{PLAYER}", player.getName());
		Bukkit.broadcastMessage(message);
	}
}
