package com.huminecraft.huminestaff.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.huminecraft.huminestaff.main.Message;
import com.huminecraft.huminestaff.main.StaffMain;

public class QuitEvent implements Listener{

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(!StaffMain.getInstance().getVanished().contains(event.getPlayer())) {
			String message = Message.getMessageQuit();
			message = message.replace("{PLAYER}", event.getPlayer().getName());
			event.setQuitMessage(message);
		}
		else {
			event.setQuitMessage("");
		}
		
	}
}
