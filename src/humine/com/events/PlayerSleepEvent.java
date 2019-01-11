package humine.com.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import humine.com.main.StaffMain;

public class PlayerSleepEvent implements Listener {

	@EventHandler
	public void onSleep(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		StaffMain.getInstance().getPlayerInBed().add(player.getName());
		String chiffre = StaffMain.getInstance().getPlayerInBed().size() + "/" + Bukkit.getOnlinePlayers().size();
		player.sendMessage("Nombre de joueur dans leur lit: " + chiffre);
		
		if((Bukkit.getOnlinePlayers().size() / 2) <= StaffMain.getInstance().getPlayerInBed().size()) {
				player.getWorld().setTime(0);
		}
		
	}
	
	@EventHandler
	public void onExitSleep(PlayerBedLeaveEvent event) {
		StaffMain.getInstance().getPlayerInBed().remove(event.getPlayer().getName());
	}
}
