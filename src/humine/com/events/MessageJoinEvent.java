package humine.com.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import humine.com.main.Message;
import humine.com.main.StaffMain;

public class MessageJoinEvent implements Listener{

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(!StaffMain.getInstance().getVanished().contains(event.getPlayer())) {
			String message = Message.getMessageJoin();
			message = message.replace("{PLAYER}", event.getPlayer().getName());
			event.setJoinMessage(message);
		}
		else
			event.setJoinMessage("");
		
		for(Player player : StaffMain.getInstance().getVanished()) {
			event.getPlayer().hidePlayer(StaffMain.getInstance(), player);
		}
	}
}
