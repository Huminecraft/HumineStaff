package com.huminecraft.huminestaff.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class FilterBlockInEnderChestEvent implements Listener{

	@EventHandler
	public void onPose(InventoryClickEvent event) {
		if(event.getInventory().getType() == InventoryType.ENDER_CHEST) {
			if(event.getCurrentItem() != null) {
				if(isOnBlackList(event.getCurrentItem()))
					event.setCancelled(true);
			}
		}
	}
	
	private boolean isOnBlackList(ItemStack item) {
		switch (item.getType()) {
		case DRAGON_EGG:
		case DIAMOND_BLOCK:
		case SHULKER_BOX:
		case BLACK_SHULKER_BOX:
		case BLUE_SHULKER_BOX:
		case BROWN_SHULKER_BOX:
		case CYAN_SHULKER_BOX:
		case GRAY_SHULKER_BOX:
		case GREEN_SHULKER_BOX:
		case MAGENTA_SHULKER_BOX:
		case LIGHT_BLUE_SHULKER_BOX:
		case YELLOW_SHULKER_BOX:
		case WHITE_SHULKER_BOX:
		case RED_SHULKER_BOX:
		case PURPLE_SHULKER_BOX:
		case PINK_SHULKER_BOX:
		case ORANGE_SHULKER_BOX:
		case LIME_SHULKER_BOX:
		case LIGHT_GRAY_SHULKER_BOX:
		case ELYTRA:
		case TOTEM_OF_UNDYING:
			return true;

		default:
			return false;
		}
	}
}
