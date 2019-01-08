package humine.com.commands.permissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import humine.com.main.StaffMain;
import humine.com.permissions.PermissionGroup;

public class PermissionCommand implements CommandExecutor
{

	/*
	 * /permission add permission <player> <permission> 
	 * /permission add permission <group> <permission> 
	 * /permission add player <group> <player>
	 * 
	 * /permission remove permission <player> <permission> 
	 * /permission remove permission <group> <permission> 
	 * /permission remove player <group> <player>
	 * 
	 * /permission create group <group> [default] 
	 * /permission delete group <group>
	 * 
	 * /permission inherit <group> <group> 
	 * /permission disinherit <group> <group>
	 * 
	 * /permission list [group] [player|permission|inherit|default]
	 * 
	 * /permission help
	 */

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (args.length >= 2)
		{
			if (args[0].equalsIgnoreCase("add"))
			{
				if (args[1].equalsIgnoreCase("permission"))
				{
					if (args.length >= 4)
					{
						addPermission(sender, args);
						return true;
					}
					else
						StaffMain.sendMessage(sender, "/permission add permission <player|group> <permission>");

				}

				if (args[1].equalsIgnoreCase("player"))
				{
					if (args.length >= 4)
					{
						addPlayerToGroup(sender, args);
						return true;
					}
					else
						StaffMain.sendMessage(sender, "/permission add player <group> <player>");

				}
			}

			if (args[0].equalsIgnoreCase("remove"))
			{
				if (args[1].equalsIgnoreCase("permission"))
				{
					if (args.length >= 4)
					{
						removePermission(sender, args);
						return true;
					}
					else
						StaffMain.sendMessage(sender, "/permission remove permission <player|group> <permission>");
				}

				if (args[1].equalsIgnoreCase("player"))
				{
					if (args.length >= 4)
					{
						removePlayerTogroup(sender, args);
						return true;
					}
					else
						StaffMain.sendMessage(sender, "/permission remove player <group> <player>");
				}
			}

			if (args[0].equalsIgnoreCase("create"))
			{
				if (args[1].equalsIgnoreCase("group"))
				{
					if (args.length >= 3)
					{
						createGroup(sender, args);
						return true;
					}
					else
						StaffMain.sendMessage(sender, "/permission create group <group> [default]");
				}
			}

			if (args[0].equalsIgnoreCase("delete"))
			{
				if (args[1].equalsIgnoreCase("group"))
				{
					if (args.length >= 3)
					{
						deleteGroup(sender, args);
						return true;
					}
					else
						StaffMain.sendMessage(sender, "/permission delete group <group>");
				}
			}

			if (args[0].equalsIgnoreCase("inherit"))
			{
				if (args.length >= 3)
				{
					inherit(sender, args);
					return true;
				}
				else
					StaffMain.sendMessage(sender, "/permission inherit <groupReceiver> <group>");
			}
			
			if (args[0].equalsIgnoreCase("disinherit"))
			{
				if (args.length >= 3)
				{
					disinherit(sender, args);
					return true;
				}
				else
					StaffMain.sendMessage(sender, "/permission disinherit <groupReceiver> <group>");
			}
			
			if (args[0].equalsIgnoreCase("list"))
			{
				list(sender, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("help"))
			{
				help(sender);
				return true;
			}

		}

		return false;
	}

	private void help(CommandSender sender)
	{
		sender.sendMessage("§3/permission add permission §c<player> <permission>");
		sender.sendMessage("§3/permission add permission §c<group> <permission>");
		sender.sendMessage("§3/permission add player §c<group> <player>");
		sender.sendMessage("§3/permission remove permission §c<player> <permission>");
		sender.sendMessage("§3/permission remove permission §c<group> <permission> ");
		sender.sendMessage("§3/permission remove player §c<group> <player>");
		sender.sendMessage("§3/permission create group §c<group> §6[default]");
		sender.sendMessage("§3/permission delete group §c<group>");
		sender.sendMessage("§3/permission inherit §c<group> <group> ");
		sender.sendMessage("§3/permission disinherit §c<group> <group>");
		sender.sendMessage("§3/permission list §6[group] [player|permission|inherit|default]");
		sender.sendMessage("§3/permission help");
	}

	private void list(CommandSender sender, String[] args)
	{
		if(args.length >= 3) {
			if(isGroup(args[1])) {
				PermissionGroup group = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[1]);
				if(args[2].equalsIgnoreCase("player")) {
					sender.sendMessage(group.getName() + ":");
					afficherJoueur(sender, group);
				}
				
				if(args[2].equalsIgnoreCase("default")) {
					sender.sendMessage(group.getName() + ":");
					afficherDefault(sender, group);
				}
				
				if(args[2].equalsIgnoreCase("permission")) {
					sender.sendMessage(group.getName() + ":");
					afficherPermission(sender, group);
				}
				
				if(args[2].equalsIgnoreCase("inherit")) {
					sender.sendMessage(group.getName() + ":");
					afficherInherit(sender, group);
				}
			}
			else
				StaffMain.sendMessage(sender, "Groupe introuvable");
		}
		else if(args.length == 2) {
			if(isGroup(args[1])) {
				PermissionGroup group = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[1]);
				sender.sendMessage(group.getName() + ":");
				afficherDefault(sender, group);
				afficherPermission(sender, group);
				afficherInherit(sender, group);
				afficherJoueur(sender, group);
				
				for(String perm : group.getPermissionsList())
					sender.sendMessage("- " + ChatColor.GOLD + perm);
			}
			else
				StaffMain.sendMessage(sender, "Groupe introuvable");
		}
		else {
			String message = "";
			for(PermissionGroup group : StaffMain.getInstance().getPermissionGroupManager().getPermissionGroups())
				message += ChatColor.GOLD + group.getName() + "§r, ";
			
			sender.sendMessage("Groupe(s):");
			sender.sendMessage(message);
		}
	}

	private void afficherJoueur(CommandSender sender, PermissionGroup group)
	{
		String message = "";
		for(String player : group.getPlayers())
			message += ChatColor.GOLD + player + "§r, ";
		
		sender.sendMessage(message);
	}

	private void afficherInherit(CommandSender sender, PermissionGroup group)
	{
		String message = "";
		for(PermissionGroup g : group.getInherits())
			message += ChatColor.GOLD + g.getName() + "§r, ";
	
		sender.sendMessage("Heritage: " + message);
		
	}

	private void afficherPermission(CommandSender sender, PermissionGroup group)
	{
		for(String perm : group.getPermissionsList())
			sender.sendMessage("- " + ChatColor.GOLD + perm);
	}

	private void afficherDefault(CommandSender sender, PermissionGroup group)
	{
		if(group.isDefault())
			sender.sendMessage("Default: " + ChatColor.GREEN + "true");
		else
			sender.sendMessage("Default: " + ChatColor.RED + "false");
	}

	private void disinherit(CommandSender sender, String[] args)
	{
		if(isGroup(args[1])) {
			if(isGroup(args[2])) {
				PermissionGroup group1 = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[1]);
				PermissionGroup group2 = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[2]);
				group1.removeInherit(group2);
				StaffMain.sendMessage(sender, "Maintenant " + args[1] + " n'hérite plus de " + args[2] + " !");
			}
			else
				StaffMain.sendMessage(sender, "Groupe " + args[2] + " introuvable");
		}
		else
			StaffMain.sendMessage(sender, "Groupe " + args[1] + " introuvable");
	}

	private void inherit(CommandSender sender, String[] args)
	{
		if(isGroup(args[1])) {
			if(isGroup(args[2])) {
				PermissionGroup group1 = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[1]);
				PermissionGroup group2 = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[2]);
				group1.addInherit(group2);
				StaffMain.sendMessage(sender, "Maintenant " + args[1] + " hérite de " + args[2] + " !");
			}
			else
				StaffMain.sendMessage(sender, "Groupe " + args[2] + " introuvable");
		}
		else
			StaffMain.sendMessage(sender, "Groupe " + args[1] + " introuvable");
	}

	private void deleteGroup(CommandSender sender, String[] args)
	{
		if(isGroup(args[2])) {
			PermissionGroup group = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[2]);
			StaffMain.getInstance().getPermissionGroupManager().removePermissionGroup(group);
			
			if(group.isDefault()) {
				if(!StaffMain.getInstance().getPermissionGroupManager().isEmpty())
					StaffMain.getInstance().getPermissionGroupManager().setDefaultPermissionGroup(StaffMain.getInstance().getPermissionGroupManager().getPermissionGroups().get(0));
			}
			
			StaffMain.sendMessage(sender, "Groupe " + args[2] + " supprimé !");
		}
		else
			StaffMain.sendMessage(sender, "Groupe introuvable");
	}

	private void createGroup(CommandSender sender, String[] args)
	{
		if(!isGroup(args[2])) {
			PermissionGroup group = new PermissionGroup(StaffMain.getInstance(), args[2]);
			StaffMain.getInstance().getPermissionGroupManager().addPermissionGroup(group);
			
			if(args.length >= 4 && args[3].equalsIgnoreCase("true")) {
				StaffMain.getInstance().getPermissionGroupManager().setDefaultPermissionGroup(group);
			}
			
			StaffMain.sendMessage(sender, "Groupe " + args[2] + " créé !");
		}
		else
			StaffMain.sendMessage(sender, "Groupe déjà existant");
	}

	private void removePlayerTogroup(CommandSender sender, String[] args)
	{
		if(isGroup(args[2])) {
			if(isPlayer(args[3])) {
				Player player = Bukkit.getPlayer(args[2]);
				PermissionGroup group = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[2]);
				group.removePlayer(player);
				StaffMain.sendMessage(sender, args[3] + " supprimé du groupe " + ChatColor.GREEN + args[2]);
			}
			else
				StaffMain.sendMessage(sender, "Joueur introuvable");
		}
		else
			StaffMain.sendMessage(sender, "Groupe introuvable");
	}

	private void removePermission(CommandSender sender, String[] args)
	{
		if(isPlayer(args[2])) {
			Player player = Bukkit.getPlayer(args[2]);
			PermissionAttachment attach = player.addAttachment(StaffMain.getInstance());
			attach.setPermission(args[3], false);
			StaffMain.sendMessage(sender, "Permission supprimé au joueur " + args[2] + ": " + ChatColor.GOLD + args[3]);
		}
		else if(isGroup(args[2])) {
			PermissionGroup group = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[2]);
			group.removePermission(args[3]);
			StaffMain.sendMessage(sender, "Permission supprimé au groupe " + args[2] + ": " + ChatColor.GOLD + args[3]);
		}
		else {
			StaffMain.sendMessage(sender, args[2] + " introuvable");
		}
	}

	private void addPlayerToGroup(CommandSender sender, String[] args)
	{
		if(isGroup(args[2])) {
			if(isPlayer(args[3])) {
				Player player = Bukkit.getPlayer(args[2]);
				PermissionGroup group = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[2]);
				group.addPlayer(player);
				StaffMain.sendMessage(sender, args[3] + " ajouté dans le groupe " + ChatColor.GREEN + args[2]);
			}
			else
				StaffMain.sendMessage(sender, "Joueur introuvable");
		}
		else
			StaffMain.sendMessage(sender, "Groupe introuvable");
	}

	private void addPermission(CommandSender sender, String[] args)
	{
		if(isPlayer(args[2])) {
			Player player = Bukkit.getPlayer(args[2]);
			PermissionAttachment attach = player.addAttachment(StaffMain.getInstance());
			attach.setPermission(args[3], true);
			StaffMain.sendMessage(sender, "Permission ajoutée au joueur " + args[2] + ": " + ChatColor.GOLD + args[3]);
		}
		else if(isGroup(args[2])) {
			PermissionGroup group = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(args[2]);
			group.addPermission(args[3]);
			StaffMain.sendMessage(sender, "Permission ajoutée au groupe " + args[2] + ": " + ChatColor.GOLD + args[3]);
		}
		else {
			StaffMain.sendMessage(sender, args[2] + " introuvable");
		}
	}

	private boolean isGroup(String group)
	{
		PermissionGroup g = StaffMain.getInstance().getPermissionGroupManager().getPermissionGroup(group);
		if(g != null)
			return true;
		else
			return false;
	}

	private boolean isPlayer(String player)
	{
		Player p = Bukkit.getPlayer(player);
		if(p != null)
			return true;
		else
			return false;
	}

}
