package humine.com.events;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import humine.com.main.FileManager;

public class SavePlayerInventoryEvent implements Listener{

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		try {
			FileManager.savePlayerInventory(event.getPlayer());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
