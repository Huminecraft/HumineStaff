package humine.com.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class FilterBlockInEnderChestEvent implements Listener{

	@EventHandler
	public void onPose(InventoryMoveItemEvent event) {
		if(event.getDestination().getType() == InventoryType.ENDER_CHEST) {
			if(isOnBlackList(event.getItem()))
				event.setCancelled(true);
		}
	}
	
	private boolean isOnBlackList(ItemStack item) {
		switch (item.getType()) {
		case DRAGON_EGG:
		case DIAMOND_BLOCK:
		case SHULKER_BOX:
		case ELYTRA:
		case TOTEM_OF_UNDYING:
			return true;

		default:
			return false;
		}
	}
}
