package humine.com.events.permissions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import humine.com.main.StaffMain;

public class PermissionJoinCommand implements Listener
{

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(StaffMain.getInstance().getPermissionGroupManager().containsPlayer(player)) {
			StaffMain.getInstance().getPermissionGroupManager().calculatePermission(player);
		}
		
		if(StaffMain.getInstance().getPermissionGroupManager().containsDefaultPermissionGroup() && !StaffMain.getInstance().getPermissionGroupManager().getDefaultPermissionGroup().containsPlayer(player))
			StaffMain.getInstance().getPermissionGroupManager().addPlayerToDefault(player);
	}
}
