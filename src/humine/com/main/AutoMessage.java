package humine.com.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.aypi.utils.Timer;
import com.aypi.utils.inter.TimerFinishListener;

public class AutoMessage {

	private ArrayList<String> messages;
	private int delay;
	private boolean loop;
	
	public AutoMessage() {
		this.messages = new ArrayList<String>();
		this.delay = 15;
		this.loop = true;
	}
	
	public void startLoop() {
		this.loop = true;
		loopMessage(0);
	}
	
	public void stopLoop() {
		this.loop = false;
	}
	
	public void addMessage(String message) {
		this.messages.add(message);
	}
	
	public void removeMessage(int index) {
		this.messages.remove(index);
	}
	
	private void loopMessage(int number) {
		Timer timer = new Timer(StaffMain.getInstance(), this.delay, new TimerFinishListener() {
			
			@Override
			public void execute() {
				if(loop) {
					if(!messages.isEmpty())
						Bukkit.broadcastMessage(ChatColor.AQUA + messages.get(number));
					
					if((number+1) < messages.size())
						loopMessage((number+1));
					else
						loopMessage(0);
				}
			}
		});
		
		timer.start();
	}

	public ArrayList<String> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public void save(File folder) throws IOException {
		File file = new File(folder, "AutoMessage.yml");
		if(!file.exists())
			file.createNewFile();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		config.set("Delay", getDelay());
		config.set("Loop", isLoop());
		
		for(int i = 0; i < getMessages().size(); i++) {
			config.set("Messages."+i, getMessages().get(i));
		}
		
		config.save(file);
	}
	
	public void getOnFile(File folder) {
		File file = new File(folder, "AutoMessage.yml");
		if(file.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			if(config.contains("Delay"))
				setDelay(config.getInt("Delay"));
			if(config.contains("Loop"))
				setLoop(config.getBoolean("Loop"));
			
			if(config.contains("Messages")) {
				ArrayList<String> list = new ArrayList<String>();
				for(String key : config.getConfigurationSection("Messages").getKeys(false)) {
					list.add(config.getString("Messages."+key));
				}
				setMessages(list);
			}
			
		}
		
	}
}
