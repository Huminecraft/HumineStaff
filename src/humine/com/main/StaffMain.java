package humine.com.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import humine.com.commands.*;
import humine.com.commands.permissions.PermissionCommand;
import humine.com.commands.voteban.OpenVoteBanCommand;
import humine.com.commands.voteban.VoteBanCommand;
import humine.com.events.*;
import humine.com.events.permissions.PermissionJoinEvent;
import humine.com.permissions.PermissionGroup;
import humine.com.permissions.PermissionGroupManager;

public class StaffMain extends JavaPlugin{

	private static StaffMain instance;
	
	private ArrayList<Player> vanished;
	private AutoMessage autoMessage;
	private VoteBan voteBan;
	private PermissionGroupManager permissionGroupManager;
	
	@Override
	public void onEnable() {
		instance = this;
		this.vanished = new ArrayList<Player>();
		this.autoMessage = new AutoMessage();
		this.voteBan = new VoteBan();
		this.permissionGroupManager = new PermissionGroupManager();
		
		this.saveDefaultConfig();
		FileManager.makeDeFaultConfiguration(this.getDataFolder());
		try {
			Message.initiliaze(this.getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.autoMessage.getOnFile(this.getDataFolder());
		
		initiliazePermission();
		initiliazeEvents();
		initializeCommands();
	}
	
	@Override
	public void onDisable() {
		try {
			this.autoMessage.save(this.getDataFolder());
			
			for(PermissionGroup group : this.permissionGroupManager.getPermissionGroups()) {
				File file = new File(this.getDataFolder(), "Group/"+group.getName()+".yml");
				group.save(file);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initiliazePermission()
	{
		File folder = new File(this.getDataFolder(), "Group");
		File prefixFile = new File(this.getDataFolder(), "prefix.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(prefixFile);
		
		String name = "";
		for(File file : folder.listFiles()) {
			name = file.getName();
			name = name.substring(0, name.length() - 4);
			PermissionGroup group = new PermissionGroup(this, name);
			group.getSave(file);
			
			if(config.contains(group.getName()))
				group.setPrefix(config.getString(group.getName()));
			
			this.permissionGroupManager.addPermissionGroup(group);
		}
		
		if(!this.permissionGroupManager.containsDefaultPermissionGroup()) {
			PermissionGroup group = new PermissionGroup(this, "user");
			group.setDefault(true);
			for(Player player : Bukkit.getOnlinePlayers())
				group.addPlayer(player);
			
			this.permissionGroupManager.addPermissionGroup(group);
			
		}
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(StaffMain.getInstance().getPermissionGroupManager().containsPlayer(player)) {
				StaffMain.getInstance().getPermissionGroupManager().calculatePermission(player);
			}
			else if(StaffMain.getInstance().getPermissionGroupManager().containsDefaultPermissionGroup() && !StaffMain.getInstance().getPermissionGroupManager().getDefaultPermissionGroup().containsPlayer(player))
				StaffMain.getInstance().getPermissionGroupManager().addPlayerToDefault(player);
		}
	}
	
	private void initiliazeEvents() {
		this.getServer().getPluginManager().registerEvents(new SaveEnderChestEvent(), this);
		this.getServer().getPluginManager().registerEvents(new SavePlayerInventoryEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MessageJoinEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MessageQuitEvent(), this);
		this.getServer().getPluginManager().registerEvents(new BreakDiamondBlockEvent(), this);
		this.getServer().getPluginManager().registerEvents(new FilterBlockInEnderChestEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PermissionJoinEvent(), this);
	}
	
	private void initializeCommands() {
		this.getCommand("invsee").setExecutor(new OpenInventoryCommand());
		this.getCommand("endsee").setExecutor(new OpenEnderChestCommand());
		this.getCommand("annonce").setExecutor(new AnnonceCommand());
		this.getCommand("vanish").setExecutor(new VanishCommand());
		this.getCommand("voteban").setExecutor(new OpenVoteBanCommand());
		this.getCommand("voteban").setExecutor(new VoteBanCommand());
		this.getCommand("automessage").setExecutor(new AutoMessageCommand());
		this.getCommand("lien").setExecutor(new OpenLienCommand());
		this.getCommand("permission").setExecutor(new PermissionCommand());
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

	public AutoMessage getAutoMessage() {
		return autoMessage;
	}

	public VoteBan getVoteBan() {
		return voteBan;
	}

	public void setVoteBan(VoteBan voteBan) {
		this.voteBan = voteBan;
	}

	public PermissionGroupManager getPermissionGroupManager()
	{
		return permissionGroupManager;
	}

	public void setPermissionGroupManager(PermissionGroupManager permissionGroupManager)
	{
		this.permissionGroupManager = permissionGroupManager;
	}

}
