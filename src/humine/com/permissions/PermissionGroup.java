package humine.com.permissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import humine.com.main.StaffMain;

public class PermissionGroup
{

	private String									name;
	private boolean									defaut;
	private Plugin									plugin;

	private HashMap<String, PermissionAttachment>	permissionsPlayer;
	private ArrayList<String>						permissionsList;
	private ArrayList<String>						groupInherit;

	public PermissionGroup(Plugin plugin, String name)
	{
		this.name = name;
		this.plugin = plugin;
		this.defaut = false;
		this.permissionsPlayer = new HashMap<String, PermissionAttachment>();
		this.permissionsList = new ArrayList<String>();
		this.groupInherit = new ArrayList<String>();
	}

	public PermissionGroup(Plugin plugin, String name, boolean defaut)
	{
		this.name = name;
		this.plugin = plugin;
		this.defaut = defaut;
		this.permissionsPlayer = new HashMap<String, PermissionAttachment>();
		this.permissionsList = new ArrayList<String>();
		this.groupInherit = new ArrayList<String>();
	}

	public void addPlayer(Player player)
	{
		try
		{
			PermissionAttachment attachment = player.addAttachment(plugin);

			for (String perm : this.permissionsList)
				attachment.setPermission(perm, true);

			this.permissionsPlayer.put(player.getName(), attachment);
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	public void removePlayer(Player player)
	{
		try
		{
			PermissionAttachment attachment = player.addAttachment(plugin);

			for (String perm : this.permissionsList)
				attachment.setPermission(perm, false);

			this.permissionsPlayer.remove(player.getName());
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	public void addPermission(String permission)
	{
		if (!this.permissionsList.contains(permission))
		{
			for (Entry<String, PermissionAttachment> perm : this.permissionsPlayer.entrySet())
			{
				perm.getValue().setPermission(permission, true);
			}
			this.permissionsList.add(permission);
		}
	}

	public void removePermission(String permission)
	{
		if (this.permissionsList.contains(permission)) {
			for (Entry<String, PermissionAttachment> perm : this.permissionsPlayer.entrySet())
			{
				perm.getValue().setPermission(permission, false);
			}
			this.permissionsList.remove(permission);
		}
	}

	public void addInherit(PermissionGroup permissionGroup)
	{
		this.groupInherit.add(permissionGroup.getName());

		for (String permission : permissionGroup.getPermissionsList())
		{
			if (!this.permissionsList.contains(permission))
			{
				this.permissionsList.add(permission);
				for (Entry<String, PermissionAttachment> p : this.permissionsPlayer.entrySet())
				{
					p.getValue().setPermission(permission, true);
				}
			}

		}
	}

	public void removeInherit(PermissionGroup permissionGroup)
	{
		this.groupInherit.remove(permissionGroup.getName());

		for (String permission : permissionGroup.getPermissionsList())
		{
			this.permissionsList.remove(permission);
			for (Entry<String, PermissionAttachment> p : this.permissionsPlayer.entrySet())
			{
				p.getValue().setPermission(permission, false);
			}
		}
		
		for(PermissionGroup group : StaffMain.getInstance().getPermissionGroupManager().getPermissionGroups()) {
			if(this.groupInherit.contains(group.getName())) {
				for(String perm : group.getPermissionsList())
					addPermission(perm);
			}
		}
	}

	public boolean containsInherit(PermissionGroup permissionGroup)
	{
		return this.groupInherit.contains(permissionGroup.getName());
	}

	public boolean containsInherit(String permissionGroup)
	{
		return this.groupInherit.contains(permissionGroup);
	}

	public boolean containsPermission(String permission)
	{
		return this.permissionsList.contains(permission);
	}

	public boolean containsPlayer(Player player)
	{
		return this.permissionsPlayer.containsKey(player.getName());
	}

	public boolean containsPlayer(String player)
	{
		return this.permissionsPlayer.containsKey(player);
	}

	public void save(File file) throws IOException
	{
		if (!file.exists())
			file.createNewFile();

		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.createSection(this.name);
		config.set(this.name + ".Default", this.defaut);

		List<String> players = new ArrayList<String>();
		for (Entry<String, PermissionAttachment> p : this.permissionsPlayer.entrySet())
			players.add(p.getKey());

		config.set(this.name + ".Users", players);
		config.set(this.name + ".Inherits", this.groupInherit);
		config.set(this.name + ".Permissions", this.permissionsList);

		config.save(file);
	}

	public void getSave(File file)
	{
		if (file.exists())
		{
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);

			this.defaut = config.getBoolean(this.name + ".Default");

			List<String> playerList = config.getStringList(this.name + ".Users");
			for (Player player : Bukkit.getOnlinePlayers())
			{
				if (playerList.contains(player.getName()))
				{
					addPlayer(player);
					playerList.remove(player.getName());
				}
			}

			if (!this.permissionsPlayer.isEmpty())
			{
				Object[] attach = this.permissionsPlayer.values().toArray();
				for (String player : playerList)
				{
					this.permissionsPlayer.put(player, (PermissionAttachment) attach[0]);
				}
				attach = null;
			}

			this.groupInherit = (ArrayList<String>) config.getStringList(this.name + ".Inherits");
			this.permissionsList = (ArrayList<String>) config.getStringList(this.name + ".Permissions");
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isDefault()
	{
		return defaut;
	}

	public void setDefault(boolean defaut)
	{
		this.defaut = defaut;
	}

	public Plugin getPlugin()
	{
		return plugin;
	}

	public void setPlugin(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public HashMap<String, PermissionAttachment> getPermissionsPlayer()
	{
		return permissionsPlayer;
	}

	public void setPermissionsPlayer(HashMap<String, PermissionAttachment> permissionsPlayer)
	{
		this.permissionsPlayer = permissionsPlayer;
	}

	public ArrayList<String> getPermissionsList()
	{
		return permissionsList;
	}

	public void setPermissionsList(ArrayList<String> permissionsList)
	{
		this.permissionsList = permissionsList;
	}

	public ArrayList<String> getPlayers()
	{
		ArrayList<String> players = new ArrayList<String>();
		for (Entry<String, PermissionAttachment> p : this.permissionsPlayer.entrySet())
			players.add(p.getKey());

		return players;
	}

	public ArrayList<String> getInherits()
	{
		return groupInherit;
	}

	public void setInherits(ArrayList<String> groupInherit)
	{
		this.groupInherit = groupInherit;
	}

	public void showAll(CommandSender sender)
	{
		showDefault(sender);
		showPermission(sender);
		showInherit(sender);
		showPlayer(sender);
	}

	public void showPlayer(CommandSender sender)
	{
		String message = "";
		for (String player : getPlayers())
			message += ChatColor.GOLD + player + "§r, ";
		if (!message.equals("") && message.length() >= 2)
			message = message.substring(0, message.length() - 2);
		sender.sendMessage(message);
	}

	public void showInherit(CommandSender sender)
	{
		String message = "";
		for (String g : getInherits())
			message += ChatColor.GREEN + g + "§r, ";
		if (!message.equals("") && message.length() >= 2)
			message = message.substring(0, message.length() - 2);
		sender.sendMessage("Heritage: " + message);

	}

	public void showPermission(CommandSender sender)
	{
		for (String perm : getPermissionsList())
			sender.sendMessage("- " + ChatColor.GOLD + perm);
	}

	public void showDefault(CommandSender sender)
	{
		if (isDefault())
			sender.sendMessage("Default: " + ChatColor.GREEN + "true");
		else
			sender.sendMessage("Default: " + ChatColor.RED + "false");
	}
}
