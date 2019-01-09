package humine.com.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public abstract class FileManager {

	private static File folder;
	
	public static void makeDeFaultConfiguration(File folder) {
		File file = new File(folder, "Inventory");
		if(!file.exists())
			file.mkdirs();
		
		file = new File(folder, "EnderChest");
		if(!file.exists())
			file.mkdirs();
		
		file = new File(folder, "Group");
		if(!file.exists())
			file.mkdirs();
		
		FileManager.folder = folder;
	}
	
	public static void savePlayerInventory(Player player) throws IOException {
		File playerFile = new File(folder, "/Inventory/"+player.getName()+".yml");
		if(!playerFile.exists())
			playerFile.createNewFile();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
		for(short i = 0; i < player.getInventory().getSize(); i++) {
			if(player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() != Material.AIR) {
				config.set("Inventory."+i, player.getInventory().getItem(i));
			}
		}
		
		config.save(playerFile);
	}
	
	public static void savePlayerInventory(String name, Inventory inventory) throws IOException {
		File playerFile = new File(folder, "/Inventory/"+name+".yml");
		if(!playerFile.exists())
			playerFile.createNewFile();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
		for(short i = 0; i < inventory.getSize(); i++) {
			if(inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR) {
				config.set("Inventory."+i, inventory.getItem(i));
			}
		}
		
		config.save(playerFile);
	}
	
	public static Inventory getPlayerInventory(String name) {
		File playerFile = new File(folder, "/Inventory/"+name+".yml");
		if(playerFile.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
			Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, "Inventaire de " + name);
			for(String key : config.getConfigurationSection("Inventory").getKeys(false)) {
				int slot = Integer.parseInt(key);
				inv.setItem(slot, config.getItemStack("Inventory."+key));
			}
			
			return inv;
		}
		
		return null;
	}
	
	
	public static void saveEnderChestInventory(Player player) throws IOException {
		File playerFile = new File(folder, "/EnderChest/"+player.getName()+".yml");
		if(!playerFile.exists())
			playerFile.createNewFile();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
		for(short i = 0; i < player.getEnderChest().getSize(); i++) {
			if(player.getEnderChest().getItem(i) != null && player.getEnderChest().getItem(i).getType() != Material.AIR) {
				config.set("Inventory."+i, player.getEnderChest().getItem(i));
			}
		}
		
		config.save(playerFile);
	}
	
	public static void saveEnderChestInventory(String name, Inventory inventory) throws IOException {
		File playerFile = new File(folder, "/EnderChest/"+name+".yml");
		if(!playerFile.exists())
			playerFile.createNewFile();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
		for(short i = 0; i < inventory.getSize(); i++) {
			if(inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR) {
				config.set("Inventory."+i, inventory.getItem(i));
			}
		}
		
		config.save(playerFile);
	}
	
	public static Inventory getEnderChestInventory(String name) {
		File playerFile = new File(folder, "/Inventory/"+name+".yml");
		if(playerFile.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
			Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, "EnderChest de " + name);
			for(String key : config.getConfigurationSection("Inventory").getKeys(false)) {
				int slot = Integer.parseInt(key);
				inv.setItem(slot, config.getItemStack("Inventory."+key));
			}
			
			return inv;
		}
		
		return null;
	}
	
}
