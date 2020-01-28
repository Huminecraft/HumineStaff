package humine.com.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private static VoteBan voteBan;
	private static AutoMessage autoMessage;
	
	private ArrayList<Player> vanished;
	
	private PermissionGroupManager permissionGroupManager;
	private List<String> PlayerInBed;
	
	private File voteBanFolder;

	@Override
	public void onEnable() {
		instance = this;
		
		this.voteBanFolder = new File(getDataFolder(), "VoteBanLogs");
		this.voteBanFolder.mkdirs();
		
		this.vanished = new ArrayList<Player>();
		try {
			autoMessage = AutoMessage.getOnFile(getDataFolder());
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		
		if(autoMessage == null)
			autoMessage = new AutoMessage();
		
		autoMessage.startLoop();
		voteBan = null;
		this.permissionGroupManager = new PermissionGroupManager();
		this.PlayerInBed = new ArrayList<String>();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.isSleeping())
				this.PlayerInBed.add(player.getName());
		}
		
		this.saveDefaultConfig();
		FileManager.makeDeFaultConfiguration(this.getDataFolder());
		try {
			Message.initiliaze(this.getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			autoMessage = AutoMessage.getOnFile(this.getDataFolder());
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		initiliazePermission();
		initiliazeEvents();
		initializeCommands();
		TPS.startTPSManager();
	}
	
	@Override
	public void onDisable() {
		try {
			AutoMessage.save(autoMessage, this.getDataFolder());
			FileManager.saveTPSConfig(TPS.enabled);
			FileManager.saveUpvoteConfig(UpvoteCommand.delay, UpvoteCommand.reward);
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
		this.getServer().getPluginManager().registerEvents(new PlayerSleepEvent(), this);
	}
	
	private void initializeCommands() {
		this.getCommand("invsee").setExecutor(new OpenInventoryCommand());
		this.getCommand("endsee").setExecutor(new OpenEnderChestCommand());
		this.getCommand("annonce").setExecutor(new AnnonceCommand());
		this.getCommand("vanish").setExecutor(new VanishCommand());
		this.getCommand("openvoteban").setExecutor(new OpenVoteBanCommand());
		this.getCommand("voteban").setExecutor(new VoteBanCommand());
		this.getCommand("automessage").setExecutor(new AutoMessageCommand());
		this.getCommand("lien").setExecutor(new OpenLienCommand());
		this.getCommand("permission").setExecutor(new PermissionCommand());
		this.getCommand("tpsmonitor").setExecutor(new TPSCommand());
		this.getCommand("upvote").setExecutor(new UpvoteCommand());
		this.getCommand("where").setExecutor(new LocateCommand());
		this.getCommand("sos").setExecutor(new SOSCommand());
	}
	
	
	public static StaffMain getInstance() {
		return instance;
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.DARK_AQUA + "[Staff] " + ChatColor.RESET + message);
	}
	
	public static void sendBroadCastMessage(String message) {
		Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "[Staff] " + ChatColor.RESET + message);
	}

	public ArrayList<Player> getVanished() {
		return vanished;
	}

	public static AutoMessage getAutoMessage() {
		return autoMessage;
	}

	public static VoteBan getVoteBan() {
		return voteBan;
	}

	public static void setVoteBan(VoteBan vb) {
		voteBan = vb;
	}

	public PermissionGroupManager getPermissionGroupManager()
	{
		return permissionGroupManager;
	}

	public void setPermissionGroupManager(PermissionGroupManager permissionGroupManager)
	{
		this.permissionGroupManager = permissionGroupManager;
	}

	public List<String> getPlayerInBed() {
		return PlayerInBed;
	}

	public void setPlayerInBed(List<String> playerInBed) {
		PlayerInBed = playerInBed;
	}
	
	public File getVoteBanFolder() {
		return voteBanFolder;
	}

}
