package com.huminecraft.huminestaff.events;

import java.io.IOException;

import com.huminecraft.huminestaff.main.FileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

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
