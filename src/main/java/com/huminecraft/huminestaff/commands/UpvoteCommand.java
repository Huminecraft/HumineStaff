package com.huminecraft.huminestaff.commands;

import com.huminecraft.huminestaff.main.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.URL;
import java.io.*;


public class UpvoteCommand implements CommandExecutor {


    public static int reward = FileManager.getUpvoteConfig("reward");
    public static int delay = FileManager.getUpvoteConfig("delay");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String ip = getIP(player);
            if (isUpvoted(delay, ip)){
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "store pixel " + player.getName() + " " + reward );
            }
        }

        return false;
    }

    private String getIP(Player player){
        return player.getPlayer().getAddress().getHostString();
    }

    private boolean isUpvoted(int minutes, String ip){
        try
        {
            URL url = new URL("https://www.serveurs-minecraft.org/api/is_valid_vote.php?id=48763&ip=" + ip + "&duration=" + minutes);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = in.readLine();
            in.close();
            return str.equals("1");
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        return true;
    }
}