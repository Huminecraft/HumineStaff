package humine.com.permissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class PermissionGroup
{

	private String									name;
	private boolean									defaut;
	private Plugin									plugin;

	private HashMap<String, PermissionAttachment>	permissionsPlayer;
	private ArrayList<String>						permissionsList;
	private ArrayList<PermissionGroup>				groupHeritage;

	public PermissionGroup(Plugin plugin, String name)
	{
		this.name = name;
		this.plugin = plugin;
		this.defaut = false;
		this.permissionsPlayer = new HashMap<String, PermissionAttachment>();
		this.permissionsList = new ArrayList<String>();
		this.groupHeritage = new ArrayList<PermissionGroup>();
	}

	public PermissionGroup(Plugin plugin, String name, boolean defaut)
	{
		this.name = name;
		this.plugin = plugin;
		this.defaut = defaut;
		this.permissionsPlayer = new HashMap<String, PermissionAttachment>();
		this.permissionsList = new ArrayList<String>();
		this.groupHeritage = new ArrayList<PermissionGroup>();
	}

	public void addPlayer(Player player)
	{
		PermissionAttachment attachment = player.addAttachment(this.plugin);
		for (String perm : this.permissionsList)
			attachment.setPermission(perm, true);

		this.permissionsPlayer.put(player.getName(), attachment);
	}

	public void removePlayer(Player player)
	{
		PermissionAttachment attachment = this.permissionsPlayer.get(player.getName());
		for (String perm : this.permissionsList)
			attachment.setPermission(perm, false);

		this.permissionsPlayer.remove(player.getName());
	}

	public void addPermission(String permission)
	{
		for (Entry<String, PermissionAttachment> perm : this.permissionsPlayer.entrySet())
		{
			perm.getValue().setPermission(permission, true);
		}
		this.permissionsList.add(permission);
	}

	public void removePermission(String permission)
	{
		for (Entry<String, PermissionAttachment> perm : this.permissionsPlayer.entrySet())
		{
			perm.getValue().setPermission(permission, false);
		}
		this.permissionsList.remove(permission);
	}

	public void addInherit(PermissionGroup permissionGroup)
	{
		this.groupHeritage.add(permissionGroup);

		for (String permission : permissionGroup.getPermissionsList())
		{
			for (Entry<String, PermissionAttachment> p : this.permissionsPlayer.entrySet())
			{
				p.getValue().setPermission(permission, true);
			}
		}
	}

	public void removeInherit(PermissionGroup permissionGroup)
	{
		this.groupHeritage.remove(permissionGroup);

		for (String permission : permissionGroup.getPermissionsList())
		{
			for (Entry<String, PermissionAttachment> p : this.permissionsPlayer.entrySet())
			{
				p.getValue().setPermission(permission, false);
			}
		}
	}

	public boolean containsInherit(PermissionGroup permissionGroup)
	{
		return this.groupHeritage.contains(permissionGroup);
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
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		config.createSection(this.name);
		config.set(this.name + ".Default", this.defaut);

		List<String> players = new ArrayList<String>();
		for (Entry<String, PermissionAttachment> p : this.permissionsPlayer.entrySet())
			players.add(p.getKey());

		config.set(this.name + ".Users", players);

		List<String> groups = new ArrayList<String>();
		for (PermissionGroup permGroup : this.groupHeritage)
			groups.add(permGroup.getName());

		config.set(this.name + ".Groups", groups);

		List<String> permissions = new ArrayList<String>();
		for (String perm : this.permissionsList)
			permissions.add(perm);

		for (PermissionGroup group : this.groupHeritage)
		{
			for (String perm : group.getPermissionsList())
			{
				permissions.add(perm);
			}
		}
		config.set(this.name + ".Permissions", this.permissionsList);

		config.save(file);
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

	public ArrayList<PermissionGroup> getInherits()
	{
		return groupHeritage;
	}

	public void setInherits(ArrayList<PermissionGroup> groupHeritage)
	{
		this.groupHeritage = groupHeritage;
	}
}
