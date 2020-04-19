package com.huminecraft.huminestaff.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class UserManager {

	private Map<UUID, User> users;
	
	public UserManager() {
		this.users = new HashMap<>();
	}
	
	public void addUser(User user) {
		this.users.put(user.getId(), user);
	}
	
	public void addUser(Player player) {
		User u = new User(player);
		this.users.put(u.getId(), u);
	}
	
	public void removeUser(User user) {
		this.users.remove(user.getId());
	}
	
	public void removeUser(Player player) {
		this.users.remove(player.getUniqueId());
	}
	
	public User getUser(Player player) {
		return this.users.get(player.getUniqueId());
	}
	
	public boolean containsUser(Player player) {
		return getUser(player) != null;
	}
	
	public static void save(UserManager um, File folder) throws IOException {
		folder.mkdirs();
		File f;
		for(User u : um.users.values()) {
			f = new File(folder, u.getId() + ".yml");
			User.save(u, f);
		}
	}
	
	public static UserManager load(File folder) throws FileNotFoundException {
		if(!folder.exists())
			throw new FileNotFoundException("HumineStaff : Dossier inexistant : UserManager :" + folder.getName());
		
		UserManager userManager = new UserManager();
		for(File f : folder.listFiles()) {
			User u = User.load(f);
			userManager.addUser(u);
		}
		return userManager;
	}
}
