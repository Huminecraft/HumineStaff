package humine.com.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class OpenLienCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		TextComponent text = new TextComponent("Viens voter ici !");
		text.setColor(ChatColor.GOLD);
		text.setBold(true);
		text.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder("C'est pour le bien de tous").create()));
		text.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, "https://www.google.com/"));
		sender.spigot().sendMessage(text);
		
		return true;
	}

}
