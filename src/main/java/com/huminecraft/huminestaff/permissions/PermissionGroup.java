package com.huminecraft.huminestaff.permissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.huminecraft.huminestaff.main.StaffMain;
import com.huminecraft.huminestaff.utils.User;

public class PermissionGroup {

	private String name;
	private String prefix;
	private boolean defaut;

	private Map<String, PermissionAttachment> permissionsPlayer;
	
	private List<String> permissionsList;
	private List<String> groupInherit;

	public PermissionGroup(String name) {
		this(name, false);
	}

	public PermissionGroup(String name, boolean defaut) {
		this.name = name;
		this.prefix = "";
		this.defaut = defaut;
		this.permissionsPlayer = new HashMap<String, PermissionAttachment>();
		this.permissionsList = new ArrayList<String>();
		this.groupInherit = new ArrayList<String>();

		File file = new File(StaffMain.getInstance().getDataFolder(), "prefix.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set(this.name, this.prefix);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPlayer(Player player) {
		try {
			PermissionAttachment attachment = player.addAttachment(StaffMain.getInstance());

			for (String perm : this.permissionsList)
				attachment.setPermission(perm, true);

			this.permissionsPlayer.put(player.getName(), attachment);

			User user = StaffMain.getUserManager().getUser(player);
			if (user != null)
				user.addGroup(this);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void removePlayer(Player player) {
		try {
			PermissionAttachment attachment = player.addAttachment(StaffMain.getInstance());

			for (String perm : this.permissionsList)
				attachment.setPermission(perm, false);

			this.permissionsPlayer.remove(player.getName());
			
			User user = StaffMain.getUserManager().getUser(player);
			if (user != null)
				user.removeGroup(this);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void addPermission(String permission) {
		if(this.permissionsList.contains(permission))
			return;
		
		for (Entry<String, PermissionAttachment> perm : this.permissionsPlayer.entrySet()) {
			perm.getValue().setPermission(permission, true);
		}
	}

	public void removePermission(String permission) {
		if(!this.permissionsList.contains(permission))
			return;
		
		for (Entry<String, PermissionAttachment> perm : this.permissionsPlayer.entrySet()) {
			perm.getValue().setPermission(permission, false);
		}
		this.permissionsList.remove(permission);
	}

	public void addInherit(PermissionGroup permissionGroup) {
		if(this.groupInherit.contains(permissionGroup.getName()))
			return;
		
		this.groupInherit.add(permissionGroup.getName());

		for (String permission : permissionGroup.getPermissionsList()) {
			addPermission(permission);
		}
	}

	public void removeInherit(PermissionGroup permissionGroup) {
		if(!this.groupInherit.contains(permissionGroup.getName()))
			return;
		
		this.groupInherit.remove(permissionGroup.getName());

		for (String permission : permissionGroup.getPermissionsList()) {
			removePermission(permission);
		}

		for (PermissionGroup group : StaffMain.getPermissionGroupManager().getPermissionGroups()) {
			if (this.groupInherit.contains(group.getName())) {
				for (String perm : group.getPermissionsList())
					addPermission(perm);
			}
		}
	}

	public boolean containsInherit(PermissionGroup permissionGroup) {
		return this.groupInherit.contains(permissionGroup.getName());
	}

	public boolean containsInherit(String permissionGroupName) {
		return this.groupInherit.contains(permissionGroupName);
	}

	public boolean containsPermission(String permission) {
		return this.permissionsList.contains(permission);
	}

	public boolean containsPlayer(Player player) {
		return this.permissionsPlayer.containsKey(player.getName());
	}

	public boolean containsPlayer(String playerName) {
		return this.permissionsPlayer.containsKey(playerName);
	}

	public void refreshPermissionPlayer(Player player) {
		PermissionAttachment attachment = player.addAttachment(StaffMain.getInstance());
		for(String perm : this.permissionsList)
			attachment.setPermission(perm, true);
		this.permissionsPlayer.put(player.getName(), attachment);
	}
	
	public static void save(PermissionGroup group, File file) throws IOException
	{
		if (!file.exists())
			file.createNewFile();

		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.set("Name", group.name);
		config.set("Default", group.defaut);

		List<String> players = new ArrayList<String>();
		for (String p : group.permissionsPlayer.keySet())
			players.add(p);

		config.set("Users", players);
		config.set("Inherits", group.groupInherit);
		config.set("Permissions", group.permissionsList);

		config.save(file);
	}
	
	public static PermissionGroup getSave(File file) throws FileNotFoundException
	{
		if(!file.exists())
			throw new FileNotFoundException("HumineStaff : PermissionGroup not found : " + file.getName());
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		PermissionGroup g = new PermissionGroup("");
		
		g.name = config.getString("Name");
		g.defaut = config.getBoolean("Default");

		g.groupInherit = config.getStringList("Inherits");
		g.permissionsList = config.getStringList("Permissions");
		
		List<String> playerList = config.getStringList("Users");
		for(String player : playerList)
			g.permissionsPlayer.put(player, null);
			
		for(Player player : StaffMain.getInstance().getServer().getOnlinePlayers()) {
			if(g.containsPlayer(player))
				g.refreshPermissionPlayer(player);
		}
		
		return g;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefault() {
		return defaut;
	}

	public void setDefault(boolean defaut) {
		this.defaut = defaut;
	}

	public Map<String, PermissionAttachment> getPermissionsPlayer() {
		return permissionsPlayer;
	}

	private List<String> getPermissionsList() {
		return permissionsList;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ArrayList<String> getPlayers() {
		ArrayList<String> players = new ArrayList<String>();
		for (Entry<String, PermissionAttachment> p : this.permissionsPlayer.entrySet())
			players.add(p.getKey());

		return players;
	}

	private List<String> getInherits() {
		return groupInherit;
	}

	public void showAll(CommandSender sender) {
		showDefault(sender);
		showPermission(sender);
		showInherit(sender);
		showPlayer(sender);
	}

	public void showPlayer(CommandSender sender) {
		List<String> players = getPlayers();
		if(players.isEmpty())
			return;
		
		String message = "";
		for (String player : players) message += ChatColor.GOLD + player + "§r, ";
		sender.sendMessage("Joueur: " + message);
	}

	public void showInherit(CommandSender sender) {
		List<String> inherits = getInherits();
		if(inherits.isEmpty())
			return;

		String message = "";
		for (String g : getInherits()) message += ChatColor.GREEN + g + "§r, ";
		sender.sendMessage("Heritage: " + message);

	}

	public void showPermission(CommandSender sender) {
		sender.sendMessage("Permissions:");
		for (String perm : getPermissionsList())
			sender.sendMessage("- " + ChatColor.GOLD + perm);
	}

	public void showDefault(CommandSender sender) {
		if (isDefault())
			sender.sendMessage("Defaut: " + ChatColor.GREEN + "true");
		else
			sender.sendMessage("Defaut: " + ChatColor.RED + "false");
	}
}
