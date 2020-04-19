package com.huminecraft.huminestaff.main;

import com.huminecraft.huminestaff.commands.*;
import com.huminecraft.huminestaff.commands.voteban.OpenVoteBanCommand;
import com.huminecraft.huminestaff.commands.voteban.VoteBanCommand;
import com.huminecraft.huminestaff.events.*;
import com.huminecraft.huminestaff.events.permissions.PermissionJoinEvent;
import com.huminecraft.huminestaff.permissions.PermissionGroup;
import com.huminecraft.huminestaff.permissions.PermissionGroupManager;
import com.huminecraft.huminestaff.utils.TabList;
import com.huminecraft.huminestaff.utils.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StaffMain extends JavaPlugin implements Listener, CommandExecutor {

    private static StaffMain instance;
    private static VoteBan voteBan;
    private static AutoMessage autoMessage;
    private static TabList tabList;
    private static PermissionGroupManager permissionGroupManager;
    private static UserManager userManager;
    private ArrayList<Player> vanished;
    private List<String> PlayerInBed;

    private File voteBanFolder;
    private File permissionGroupFolder;
    private File prefixFile;
    private File userManagerFolder;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        initializeFiles();
        initializeVariables();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isSleeping())
                this.PlayerInBed.add(player.getName());
        }
        autoMessage.startLoop();

        initiliazePermission();
        initiliazeEvents();
        initializeCommands();
        TPS.startTPSManager();
    }

    private void initializeVariables() {
        instance = this;
        this.vanished = new ArrayList<Player>();
        try {
            autoMessage = AutoMessage.getOnFile(getDataFolder());
        } catch (ClassNotFoundException | IOException e1) {
            e1.printStackTrace();
        }
        tabList = new TabList();
        if (autoMessage == null) autoMessage = new AutoMessage();
        voteBan = null;
        try {
            permissionGroupManager = PermissionGroupManager.load(this.permissionGroupFolder);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        if (permissionGroupManager == null) permissionGroupManager = new PermissionGroupManager();

        this.PlayerInBed = new ArrayList<String>();
        try {
            userManager = UserManager.load(this.userManagerFolder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (userManager == null) userManager = new UserManager();
    }

    private void initializeFiles() {
        FileManager.makeDeFaultConfiguration(this.getDataFolder());
        this.voteBanFolder = new File(getDataFolder(), "VoteBanLogs");
        this.permissionGroupFolder = new File(this.getDataFolder(), "Group");
        this.prefixFile = new File(this.getDataFolder(), "prefix.yml");
        this.userManagerFolder = new File(getDataFolder(), "Users");
        this.userManagerFolder.mkdirs();

        try {
            Message.initiliaze(this.getDataFolder());
            this.prefixFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            AutoMessage.save(autoMessage, this.getDataFolder());
            UserManager.save(userManager, this.userManagerFolder);
            FileManager.saveTPSConfig(TPS.enabled);
            FileManager.saveUpvoteConfig(UpvoteCommand.delay, UpvoteCommand.reward);
            PermissionGroupManager.save(permissionGroupManager, this.permissionGroupFolder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initiliazePermission() {
//		FileConfiguration config = YamlConfiguration.loadConfiguration(prefixFile);
//
//		String name = "";
//		for(File file : permissionGroupFolder.listFiles()) {
//			name = file.getName();
//			name = name.substring(0, name.length() - 4);
//			PermissionGroup group = null;
//			try {
//				group = PermissionGroup.getSave(file);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//			if(group == null) group = new PermissionGroup(this, "");
//
//			if(config.contains(group.getName()))
//				group.setPrefix(config.getString(group.getName()));
//
//			permissionGroupManager.addPermissionGroup(group);
//		}

        if (!permissionGroupManager.containsDefaultPermissionGroup()) {
            PermissionGroup group = new PermissionGroup("user", true);
            for (Player player : Bukkit.getOnlinePlayers())
                group.addPlayer(player);

            permissionGroupManager.addPermissionGroup(group);

        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (StaffMain.getPermissionGroupManager().containsPlayer(player)) {
                StaffMain.getPermissionGroupManager().calculatePermission(player);
            } else if (StaffMain.getPermissionGroupManager().containsDefaultPermissionGroup() && !StaffMain.getPermissionGroupManager().getDefaultPermissionGroup().containsPlayer(player))
                StaffMain.getPermissionGroupManager().addPlayerToDefault(player);
        }
    }

    private void initiliazeEvents() {
        this.getServer().getPluginManager().registerEvents(new SaveEnderChestEvent(), this);
        this.getServer().getPluginManager().registerEvents(new SavePlayerInventoryEvent(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new QuitEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BreakDiamondBlockEvent(), this);
        this.getServer().getPluginManager().registerEvents(new FilterBlockInEnderChestEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PermissionJoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerSleepEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PrefixChatEvent(), this);
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

    public static PermissionGroupManager getPermissionGroupManager() {
        return permissionGroupManager;
    }

    public void setPermissionGroupManager(PermissionGroupManager pgm) {
        permissionGroupManager = pgm;
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

    public static TabList getTabList() {
        return tabList;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public File getUserManagerFolder() {
        return userManagerFolder;
    }

}
