package humine.com.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.aypi.utils.Timer;
import com.aypi.utils.inter.TimerFinishListener;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class VoteBan {

	public static final float SEUIL_BAN = 75.0f;
	
	private Map<Player, Boolean> participants;
	private int onlinePlayers;
	
	private Player demandeur;
	private Player accuser;
	private String reason;
	private boolean inProgress;
	
	public VoteBan(Player demandeur, Player accuser, String reason) {
		this.participants = new HashMap<Player, Boolean>();
		this.accuser = accuser;
		this.demandeur = demandeur;
		this.reason = reason;
		this.inProgress = false;
		this.onlinePlayers = -1;
	}
	
	public void openVote() {
		this.inProgress = true;
		this.onlinePlayers = StaffMain.getInstance().getServer().getOnlinePlayers().size();
		alertMessage();
		
		Timer timer = new Timer(StaffMain.getInstance(), 30, new TimerFinishListener() {
			
			@Override
			public void execute() {
				inProgress = false;
				float pourcentage = (float) (((float) getNumberOfPersonFor() * 100.0) / (float) onlinePlayers);
				Bukkit.broadcastMessage("Il est l'heure du jugement !");
				Bukkit.broadcastMessage("nombre de connectée au commencement: " + ChatColor.DARK_AQUA + onlinePlayers);
				Bukkit.broadcastMessage("nombre de participant: " + ChatColor.DARK_AQUA + participants.size());
				Bukkit.broadcastMessage("nombre de pour: " + ChatColor.GREEN + getNumberOfPersonFor());
				Bukkit.broadcastMessage("nombre de contre: " + ChatColor.RED + getNumberOfPersonAgainst());
				Bukkit.broadcastMessage("Pourcentage: " + ChatColor.GOLD + pourcentage + "%");
				judgement(pourcentage);
				saveLog();
				StaffMain.setVoteBan(null);
			}
		});
		
		timer.start();
	}
	
	private void saveLog() {
		try {
			VoteBan.saveLog(this, StaffMain.getInstance().getVoteBanFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void alertMessage() {
		StaffMain.getInstance().getServer().broadcastMessage(ChatColor.GOLD + "Un voteBan est ouvert !");
		StaffMain.getInstance().getServer().broadcastMessage(ChatColor.GOLD + this.demandeur.getName() + " demande le banissement de " + this.accuser.getName() + " pour la raison suivante:");
		StaffMain.getInstance().getServer().broadcastMessage(ChatColor.RED + this.reason);
		
		StaffMain.getInstance().getServer().broadcastMessage(ChatColor.GOLD + "Vous avez 30 secondes pour voter en cliquant sur l'un des boutons suivants !");
		
		TextComponent accept = new TextComponent("Accepter");
		accept.setBold(true); accept.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/voteban yes")); accept.setColor(net.md_5.bungee.api.ChatColor.GREEN);
		TextComponent refuse = new TextComponent(" Refuser");
		refuse.setBold(true); refuse.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/voteban no")); refuse.setColor(net.md_5.bungee.api.ChatColor.RED);
		accept.addExtra(refuse);
		for(Player player : StaffMain.getInstance().getServer().getOnlinePlayers())
			player.spigot().sendMessage(accept);
	}
	
	private void judgement(float pourcentage) {
		if(pourcentage >= SEUIL_BAN) {
			Bukkit.getBanList(Type.NAME).addBan(this.accuser.getName(), this.reason, null, "VoteBan");
			this.accuser.kickPlayer(this.reason);
			Bukkit.broadcastMessage("Le joueur a etait banni du serveur par le peuple");
		}
		else
		{
			Bukkit.broadcastMessage("Le joueur n'a pas etait banni du serveur par le peuple");
		}
	}
	
	public int getNumberOfPersonFor() {
		int number = 0;
		for(boolean choose : this.participants.values()) {
			if(choose)
				number += 1;
		}
		
		return number;
	}
	
	public int getNumberOfPersonAgainst() {
		int number = 0;
		for(boolean choose : this.participants.values()) {
			if(!choose)
				number += 1;
		}
		
		return number;
	}
	
	public void addParticipant(Player player, boolean choose) {
		this.participants.put(player, choose);
	}

	public Map<Player, Boolean> getParticipants() {
		return participants;
	}

	public void setParticipants(HashMap<Player, Boolean> participants) {
		this.participants = participants;
	}

	public Player getAccuser() {
		return accuser;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}
	
	public Player getDemandeur() {
		return demandeur;
	}
	
	public int getOnlinePlayers() {
		return onlinePlayers;
	}
	
	public static void saveLog(VoteBan voteBan, File folder) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("YYYY_MM_DD-HH_mm_ss");
		Date date = new Date();
		String stringDate = format.format(date);
		File file = new File(folder, stringDate + ".yml");
		file.delete();
		file.createNewFile();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		format.applyPattern("DD MMMM YYYY : HH'h' mm'min' ss's'");
		
		config.set("Date_du_vote", format.format(date));
		config.set("Demandeur", voteBan.getDemandeur().getName());
		config.set("Accuse", voteBan.getAccuser().getName());
		config.set("Raison", voteBan.getReason());
		config.set("Nombre_de_connecte", voteBan.getOnlinePlayers());
		config.set("Nombre_de_votant", voteBan.getParticipants().size());
		config.set("Nombre_de_pour", voteBan.getNumberOfPersonFor());
		config.set("Nombre_de_contre", voteBan.getNumberOfPersonAgainst());
		
		float pourcentage = (float) (((float) voteBan.getNumberOfPersonFor() * 100.0) / (float) voteBan.getOnlinePlayers());
		config.set("Pourcentage", pourcentage);
		config.set("Accuse_banni", (pourcentage >= VoteBan.SEUIL_BAN));
		
		List<String> nameFor = new ArrayList<String>();
		List<String> nameAgainst = new ArrayList<String>();
		for(Entry<Player, Boolean> entry : voteBan.getParticipants().entrySet()) {
			if(entry.getValue())
				nameFor.add(entry.getKey().getName());
			else
				nameAgainst.add(entry.getKey().getName());
		}
		
		config.set("Votant_pour", nameFor);
		config.set("Votant_contre", nameAgainst);
		
		config.save(file);
	}
	
}
