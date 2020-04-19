package com.huminecraft.huminestaff.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.ChatColor;

public class AutoMessage implements Serializable {

    private static final long serialVersionUID = 2871950504092625478L;

    private ArrayList<String> messages;

    private int delay;
    private int currentMessage;

    private boolean loop;

    private int task;

    public AutoMessage() {
        this.messages = new ArrayList<String>();
        this.delay = 15;
        this.currentMessage = 0;
        this.loop = false;
        schedule();
    }

    public void startLoop() {
        this.loop = true;
        schedule();
    }

    public void stopLoop() {
        this.loop = false;
    }

    public void refresh() {
        StaffMain.getInstance().getServer().getScheduler().cancelTasks(StaffMain.getInstance());
        schedule();
    }

    private void schedule() {
        this.task = StaffMain.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(StaffMain.getInstance(), new Runnable() {
            public void run() {
                if(!messages.isEmpty() && loop == true) {
                    currentMessage = (currentMessage + 1) % messages.size();
                    sendBroadcastMessage(messages.get(currentMessage));
                }
            }
        }, 0L, 20 * this.delay);

        if(this.task == -1) {
            StaffMain.getInstance().getServer().getLogger().warning("AutoMessage Scheduler == -1");
        }
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void removeMessage(int index) {
        this.messages.remove(index);
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public int getTaskNumber() {
        return task;
    }

    private void sendBroadcastMessage(String str) {
        StaffMain.getInstance().getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + str);
    }

    public static void save(AutoMessage autoMessage, File folder) throws IOException {
        if(autoMessage == null)
            return;

        File file = new File(folder, "AutoMessage.hf");
        file.delete();
        file.createNewFile();

        if(!file.exists()) {
            StaffMain.getInstance().getServer().getLogger().warning("Sauvegarde : Fichier non Existant : AutoMessage.hf");
            return;
        }

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(autoMessage);
        out.flush();
        out.close();
    }

    public static AutoMessage getOnFile(File folder) throws FileNotFoundException, IOException, ClassNotFoundException {
        File file = new File(folder, "AutoMessage.hf");

        if(!file.exists()) {
            StaffMain.getInstance().getServer().getLogger().warning("Chargement : Fichier non Existant : AutoMessage.hf");
            return null;
        }

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        AutoMessage autoMessage = (AutoMessage) in.readObject();
        in.close();
        return autoMessage;
    }
}
