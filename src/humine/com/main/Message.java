package humine.com.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public abstract class Message {

	private static String messageQuit = "";
	private static String messageJoin = "";
	
	public static void initiliaze(File folder) throws IOException {
		File file = new File(folder, "message.yml");
		
		if(!file.exists()) {
			file.createNewFile();
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if(!config.contains("quit"))
			config.set("quit", "{PLAYER} a quitter la partie");
		
		if(!config.contains("join"))
			config.set("join", "{PLAYER} a rejoint la partie");
		
		config.save(file);
		
		messageQuit = config.getString("quit");
		messageJoin = config.getString("join");
			
	}
	
	public static String getMessageQuit() {
		return messageQuit;
	}
	
	public static String getMessageJoin() {
		return messageJoin;
	}
}
