package humine.com.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;

import humine.com.main.StaffMain;

public class PlayerSleepEvent implements Listener {

	@EventHandler
	public void onSleep(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		
		if(event.getBedEnterResult() == BedEnterResult.OK) {
			if(!StaffMain.getInstance().getPlayerInBed().contains(player.getName()))
				StaffMain.getInstance().getPlayerInBed().add(player.getName());
			
			String chiffre = StaffMain.getInstance().getPlayerInBed().size() + "/" + StaffMain.getInstance().getServer().getOnlinePlayers().size();
			StaffMain.sendMessage(player, "Nombre de joueur dans leur lit: " + chiffre);

			if((Bukkit.getOnlinePlayers().size() / 2) <= StaffMain.getInstance().getPlayerInBed().size()) {
					player.getWorld().setTime(0);
			}
			else
				StaffMain.sendMessage(player, "il faut la moitié des joueurs pour revenir au jour");
		}
		else
			StaffMain.sendMessage(player, "Vous ne pouvez pas encore dormir");
		
	}
	
	@EventHandler
	public void onExitSleep(PlayerBedLeaveEvent event) {
		StaffMain.getInstance().getPlayerInBed().remove(event.getPlayer().getName());
		StaffMain.sendMessage(event.getPlayer(), "Vous êtes sortis du lit");
	}
}
