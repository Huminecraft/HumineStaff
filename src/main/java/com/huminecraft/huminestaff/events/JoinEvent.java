package com.huminecraft.huminestaff.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.huminecraft.huminestaff.main.Message;
import com.huminecraft.huminestaff.main.StaffMain;

public class JoinEvent implements Listener{

	@EventHandler
	public void onJoinMessage(PlayerJoinEvent event) {
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
		StaffMain.getTabList().updateFooter();
		event.getPlayer().setPlayerListHeaderFooter(StaffMain.getTabList().getHeader(), StaffMain.getTabList().getFooter());
	}
	
	@EventHandler
	public void onJoinUser(PlayerJoinEvent event) {
		if(!StaffMain.getUserManager().containsUser(event.getPlayer()))
			StaffMain.getUserManager().addUser(event.getPlayer());
		StaffMain.getTabList().updateFooter();
		StaffMain.getUserManager().getUser(event.getPlayer()).refreshDisplay();
	}
}
