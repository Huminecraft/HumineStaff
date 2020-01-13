package humine.com.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakDiamondBlockEvent implements Listener{

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(event.getBlock().getType() == Material.DIAMOND_ORE) {
			for(Player player : Bukkit.getOnlinePlayers())
			{
				if(player.hasPermission("humine.admin.diamondblock") || player.isOp()) {
					player.sendMessage(ChatColor.ITALIC + "" + ChatColor.AQUA + "Diamant miné ! : " + event.getPlayer().getName() + " : " + event.getBlock().getLocation().getX() + " | " + event.getBlock().getLocation().getY() + " | " + event.getBlock().getLocation().getZ());
				}
			}
		}
	}
}
