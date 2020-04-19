package com.huminecraft.huminestaff.permissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;

public class PermissionGroupManager {
	
	private ArrayList<PermissionGroup> groups;

	public PermissionGroupManager() {
		this.groups = new ArrayList<PermissionGroup>();
	}

	public void addPermissionGroup(PermissionGroup group) {
		this.groups.add(group);
	}

	public void removePermissionGroup(PermissionGroup group) {
		this.groups.remove(group);
	}

	public boolean containsPermissionGroup(PermissionGroup group) {
		return this.groups.contains(group);
	}

	public boolean containsPlayer(Player player) {
		for (PermissionGroup group : this.groups) {
			if (group.containsPlayer(player))
				return true;
		}

		return false;
	}

	public boolean containsPlayer(String player) {
		for (PermissionGroup group : this.groups) {
			if (group.containsPlayer(player))
				return true;
		}

		return false;
	}

	public void addPlayerToDefault(Player player) {
		for (PermissionGroup group : this.groups) {
			if (group.isDefault())
				group.addPlayer(player);
		}
	}

	public PermissionGroup getPermissionGroup(String group) {
		for (PermissionGroup g : this.groups) {
			if (g.getName().equals(group))
				return g;
		}

		return null;
	}

	public boolean containsDefaultPermissionGroup() {
		for (PermissionGroup g : this.groups) {
			if (g.isDefault())
				return true;
		}

		return false;
	}

	public void setDefaultPermissionGroup(PermissionGroup group) {
		if (this.groups.contains(group)) {
			for (PermissionGroup g : this.groups)
				g.setDefault(false);

			group.setDefault(true);
		}
	}

	public PermissionGroup getDefaultPermissionGroup() {
		for (PermissionGroup g : this.groups) {
			if (g.isDefault())
				return g;
		}

		return null;
	}

	public ArrayList<PermissionGroup> getPermissionGroups() {
		return this.groups;
	}

	public void setPermissionGroups(ArrayList<PermissionGroup> groups) {
		this.groups = groups;
	}

	public boolean isEmpty() {
		return this.groups.isEmpty();
	}

	public void calculatePermission(Player player)
	{
		addPlayerToDefault(player);
		for (PermissionGroup group : this.groups)
		{
			if (!group.isDefault() && group.containsPlayer(player))
			{
				group.refreshPermissionPlayer(player);
			}
		}
	}

	public void addInheritPermission(PermissionGroup group, String permission) {
		for (PermissionGroup g : this.groups) {
			if (g.containsInherit(group)) {
				g.addPermission(permission);
			}
		}
	}

	public void removeInheritPermission(PermissionGroup group, String permission) {
		for (PermissionGroup g : this.groups) {
			if (g.containsInherit(group)) {
				g.removePermission(permission);
			}
		}
	}

	public void refreshAddInheritGroupPermission(PermissionGroup group)
	{
		for (PermissionGroup g : this.groups)
		{
			if (g.containsInherit(group))
			{
				g.addInherit(group);
			}
		}
	}

	public void refreshRemoveInheritGroupPermission(PermissionGroup defaultGroup, PermissionGroup GroupDisinherit)
	{
		for (PermissionGroup g : this.groups)
		{
			if (g.containsInherit(defaultGroup))
			{
				g.removeInherit(GroupDisinherit);
			}
		}
	}

	public void refreshPrefixNamePlayer(Player player)
	{
		player.setCustomNameVisible(true);
		if (!getDefaultPermissionGroup().getPrefix().equals(""))
		{
			player.setDisplayName(getDefaultPermissionGroup().getPrefix() + " " + player.getName());
			player.setPlayerListName(getDefaultPermissionGroup().getPrefix() + " " + player.getName());
		}
		else
		{
			player.setDisplayName(player.getName());
			player.setPlayerListName(player.getName());
		}
		
		for (PermissionGroup g : this.groups)
		{
			if (!g.isDefault() && g.containsPlayer(player))
			{
				player.setCustomNameVisible(true);
				player.setDisplayName(g.getPrefix() + " " + player.getName());
				player.setPlayerListName(g.getPrefix() + " " + player.getName());
			}
		}
	}

	public void refreshInheritGroup(PermissionGroup group, boolean bool) {
		if (bool) {
			for (PermissionGroup g : this.groups) {
				if (g.containsInherit(group)) {
					g.addInherit(group);
				}
			}
		} else {
			for (PermissionGroup g : this.groups) {
				if (g.containsInherit(group)) {
					g.removeInherit(group);
				}
			}
		}

	}
	
	public static void save(PermissionGroupManager manager, File folder) throws IOException {

		for(PermissionGroup g : manager.groups) {
			File file = new File(folder, g.getName() + ".yml");
			PermissionGroup.save(g, file);
		}
	}
	
	public static PermissionGroupManager load(File folder) throws FileNotFoundException {
		if(!folder.exists())
			throw new FileNotFoundException("HumineStaff : PermissionGroupManager not found : " + folder.getName());

		PermissionGroupManager manager = new PermissionGroupManager();
		for(File file : folder.listFiles()) {
			if (file != null){
				PermissionGroup g = PermissionGroup.getSave(file);
				manager.addPermissionGroup(g);
			}
		}
		return manager;
	}
}
