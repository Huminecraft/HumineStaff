package humine.com.main;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import humine.com.commands.AnnonceCommand;
import humine.com.commands.OpenEnderChestCommand;
import humine.com.commands.OpenInventoryCommand;
import humine.com.commands.VanishCommand;
import humine.com.events.MessageJoinEvent;
import humine.com.events.MessageQuitEvent;
import humine.com.events.SaveEnderChestEvent;
import humine.com.events.SavePlayerInventoryEvent;

public class StaffMain extends JavaPlugin{

	private static StaffMain instance;
	
	private ArrayList<Player> vanished;
	
	@Override
	public void onEnable() {
		instance = this;
		this.vanished = new ArrayList<Player>();
		
		this.saveDefaultConfig();
		FileManager.makeDeFaultConfiguration(this.getDataFolder());
		try {
			Message.initiliaze(this.getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		initiliazeEvents();
		initializeCommands();
	}
	
	private void initiliazeEvents() {
		this.getServer().getPluginManager().registerEvents(new SaveEnderChestEvent(), this);
		this.getServer().getPluginManager().registerEvents(new SavePlayerInventoryEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MessageJoinEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MessageQuitEvent(), this);
	}
	
	private void initializeCommands() {
		this.getCommand("invsee").setExecutor(new OpenInventoryCommand());
		this.getCommand("endsee").setExecutor(new OpenEnderChestCommand());
		this.getCommand("annonce").setExecutor(new AnnonceCommand());
		this.getCommand("vanish").setExecutor(new VanishCommand());
	}
	
	
	public static StaffMain getInstance() {
		return instance;
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.DARK_AQUA + "[HumineStaff] " + ChatColor.RESET + message);
	}
	
	public static void sendBroadCastMessage(String message) {
		Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "[HumineStaff] " + ChatColor.RESET + message);
	}

	public ArrayList<Player> getVanished() {
		return vanished;
	}
}
