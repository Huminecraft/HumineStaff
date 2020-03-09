package humine.fr.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import humine.com.main.StaffMain;
import humine.com.permissions.PermissionGroup;

public class User {

	private String name;
	private UUID id;
	private String prefix;
	private List<PermissionGroup> groups;
	
	private User() {}
	
	public User(Player player) {
		this.name = player.getName();
		this.id = player.getUniqueId();
		this.prefix = "";
		this.groups = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public List<PermissionGroup> getGroups() {
		return groups;
	}
	
	public void addGroup(PermissionGroup group) {
		if(!this.groups.contains(group))
			this.groups.add(group);
		this.prefix = (!group.getPrefix().isEmpty()) ? group.getPrefix() : this.prefix;
	}
	
	public void removeGroup(PermissionGroup group) {
		this.groups.remove(group);
		this.prefix = (!this.groups.isEmpty()) ? this.groups.get(0).getPrefix() : "";
	}
	
	public void refreshDisplay() {
		Player player = StaffMain.getInstance().getServer().getPlayer(this.name);
		if(player != null) {
			StaffMain.getTabList().sendPacket(player);
			if(!this.prefix.isEmpty()) {
				player.setCustomNameVisible(true);
				player.setCustomName(this.prefix + " " + player.getName());
				player.setDisplayName(this.prefix + " " + player.getName());
				player.setPlayerListName(this.prefix + " " + player.getName());
			}
		}
	}
	
	public static void save(User user, File file) throws IOException {
		if(!file.exists())
			file.createNewFile();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("Nom", user.name);
		config.set("Uuid", user.id.toString());
		config.set("Prefix", user.prefix);
		List<String> groups = new ArrayList<>();
		for(PermissionGroup g : user.groups) groups.add(g.getName());
		config.set("Groups", groups);
		config.save(file);
	}
	
	public static User load(File file) throws FileNotFoundException {
		if(!file.exists()) {
			throw new FileNotFoundException("HumineStaff : User File not find : " + file.getName());
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		User user = new User();
		user.name = config.getString("Nom");
		user.id = UUID.fromString(config.getString("Uuid"));
		user.prefix = config.getString("Prefix");
		List<String> groupsName = config.getStringList("Groups");
		List<PermissionGroup> groups = new ArrayList<>();
		for(PermissionGroup g : StaffMain.getPermissionGroupManager().getPermissionGroups()) {
			if(groupsName.contains(g.getName()))
				groups.add(g);
		}
		user.groups = groups;
		return user;
	}
}
