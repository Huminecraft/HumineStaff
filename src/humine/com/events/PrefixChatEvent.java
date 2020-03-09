package humine.com.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import humine.com.main.StaffMain;
import humine.fr.utils.User;

public class PrefixChatEvent implements Listener{

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		User user = StaffMain.getUserManager().getUser(event.getPlayer());
		if(user != null) {
			event.setFormat(user.getPrefix() + " " + event.getFormat());
		}
	}
}
