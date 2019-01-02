package humine.com.main;

import java.util.HashMap;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.aypi.utils.Timer;
import com.aypi.utils.inter.TimerFinishListener;

public class VoteBan {

	private HashMap<Player, Boolean> participants;
	private HashMap<Player, String> banished;
	
	private Player accuser;
	private String reason;
	private boolean inProgress;
	
	public VoteBan() {
		this.participants = new HashMap<Player, Boolean>();
		this.banished = new HashMap<Player, String>();
		this.accuser = null;
		this.reason = "";
		this.inProgress = false;
	}
	
	public void openVote(Player accuser, String reason) {
		this.accuser = accuser;
		this.reason = reason;
		this.inProgress = true;
		
		Timer timer = new Timer(StaffMain.getInstance(), 30, new TimerFinishListener() {
			
			@Override
			public void execute() {
				inProgress = false;
				float pourcentage = (float) (((float) getNumberOfPersonFor() * 100.0) / (float) Bukkit.getOnlinePlayers().size());
				Bukkit.broadcastMessage("Il est l'heure du jugement !");
				Bukkit.broadcastMessage("nombre de connectée: " + ChatColor.DARK_AQUA + Bukkit.getOnlinePlayers().size());
				Bukkit.broadcastMessage("nombre de participant: " + ChatColor.DARK_AQUA + participants.size());
				Bukkit.broadcastMessage("nombre de pour: " + ChatColor.GREEN + getNumberOfPersonFor());
				Bukkit.broadcastMessage("nombre de contre: " + ChatColor.RED + getNumberOfPersonAgainst());
				Bukkit.broadcastMessage("Pourcentage: " + ChatColor.GOLD + pourcentage + "%");
				judgement(pourcentage);
			}
		});
		
		timer.start();
	}
	
	private void judgement(float pourcentage) {
		if(pourcentage >= 75.0) {
			Bukkit.getBanList(Type.NAME).addBan(this.accuser.getName(), this.reason, null, "Vote Ban");
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
			if(choose == true)
				number += 1;
		}
		
		return number;
	}
	
	public int getNumberOfPersonAgainst() {
		int number = 0;
		for(boolean choose : this.participants.values()) {
			if(choose == false)
				number += 1;
		}
		
		return number;
	}
	
	public void addParticipant(Player player, boolean choose) {
		this.participants.put(player, choose);
	}

	public HashMap<Player, Boolean> getParticipants() {
		return participants;
	}

	public void setParticipants(HashMap<Player, Boolean> participants) {
		this.participants = participants;
	}

	public HashMap<Player, String> getBanished() {
		return banished;
	}

	public void setBanished(HashMap<Player, String> banished) {
		this.banished = banished;
	}

	public Player getAccuser() {
		return accuser;
	}

	public void setAccuser(Player accuser) {
		this.accuser = accuser;
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
	
}
