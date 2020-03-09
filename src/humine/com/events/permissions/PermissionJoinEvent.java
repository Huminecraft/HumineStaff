package humine.com.events.permissions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import humine.com.main.StaffMain;

public class PermissionJoinEvent implements Listener
{

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(StaffMain.getPermissionGroupManager().containsPlayer(player)) {
			StaffMain.getPermissionGroupManager().calculatePermission(player);
		}
		else if(StaffMain.getPermissionGroupManager().containsDefaultPermissionGroup() && !StaffMain.getPermissionGroupManager().getDefaultPermissionGroup().containsPlayer(player))
			StaffMain.getPermissionGroupManager().addPlayerToDefault(player);
		
	}
}
