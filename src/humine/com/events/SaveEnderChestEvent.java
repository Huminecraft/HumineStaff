package humine.com.events;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import humine.com.main.FileManager;

public class SaveEnderChestEvent implements Listener{

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		try {
			FileManager.saveEnderChestInventory(event.getPlayer());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
