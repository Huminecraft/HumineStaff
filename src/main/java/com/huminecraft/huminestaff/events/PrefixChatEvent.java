package com.huminecraft.huminestaff.events;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.huminecraft.huminestaff.main.StaffMain;
import com.huminecraft.huminestaff.utils.User;

public class PrefixChatEvent implements Listener{

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		User user = StaffMain.getUserManager().getUser(event.getPlayer());
		if(user != null) {
			event.setFormat(user.getPrefix() + " " + event.getFormat());
		}
	}
}
