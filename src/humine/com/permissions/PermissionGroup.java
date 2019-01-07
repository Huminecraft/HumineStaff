package humine.com.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class PermissionGroup {
	
	private String name;
	private boolean defaut;
	private Plugin plugin;
	
	private HashMap<String, PermissionAttachment> permissionsPlayer;
	private ArrayList<String> permissionsList;
	
	public PermissionGroup(Plugin plugin, String name, boolean defaut) {
		this.name = name;
		this.plugin = plugin;
		this.defaut = defaut;
		this.permissionsPlayer = new HashMap<String, PermissionAttachment>();
		this.permissionsList = new ArrayList<String>();
	}
	
	public void addPlayer(Player player) {
		if(!this.permissionsPlayer.containsKey(player.getName())) {
			PermissionAttachment attachment = player.addAttachment(this.plugin);
			for(String perm : this.permissionsList)
				attachment.setPermission(perm, true);
			
			this.permissionsPlayer.put(player.getName(), attachment);
		}
	}
	
	public void removePlayer(Player player) {
		if(this.permissionsPlayer.containsKey(player.getName())) {
			PermissionAttachment attachment = this.permissionsPlayer.get(player.getName());
			for(String perm : this.permissionsList)
				attachment.unsetPermission(perm);
			
			this.permissionsPlayer.remove(player.getName());
		}
	}
	
	public void addPermission(String permission) {
		for(Entry<String, PermissionAttachment> perm : this.permissionsPlayer.entrySet()) {
			perm.getValue().setPermission(permission, true);
		}
		this.permissionsList.add(permission);
	}
	
	public void removePermission(String permission) {
		for(Entry<String, PermissionAttachment> perm : this.permissionsPlayer.entrySet()) {
			perm.getValue().unsetPermission(permission);
		}
		this.permissionsList.remove(permission);
	}
	
	public boolean containsPermission(String permission) {
		return this.permissionsList.contains(permission);
	}
	
	public boolean containsPlayer(Player player) {
		return this.permissionsPlayer.containsKey(player.getName());
	}
	
	public boolean containsPlayer(String player) {
		return this.permissionsPlayer.containsKey(player);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefaut() {
		return defaut;
	}

	public void setDefaut(boolean defaut) {
		this.defaut = defaut;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	public HashMap<String, PermissionAttachment> getPermissionsPlayer() {
		return permissionsPlayer;
	}

	public void setPermissionsPlayer(HashMap<String, PermissionAttachment> permissionsPlayer) {
		this.permissionsPlayer = permissionsPlayer;
	}

	public ArrayList<String> getPermissionsList() {
		return permissionsList;
	}

	public void setPermissionsList(ArrayList<String> permissionsList) {
		this.permissionsList = permissionsList;
	}
}
