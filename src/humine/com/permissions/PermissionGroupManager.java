package humine.com.permissions;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class PermissionGroupManager
{
	private ArrayList<PermissionGroup> groups;

	public PermissionGroupManager()
	{
		this.groups = new ArrayList<PermissionGroup>();
	}

	public void addPermissionGroup(PermissionGroup group)
	{
		this.groups.add(group);
	}

	public void removePermissionGroup(PermissionGroup group)
	{
		this.groups.remove(group);
	}

	public boolean containsPermissionGroup(PermissionGroup group)
	{
		return this.groups.contains(group);
	}

	public boolean containsPlayer(Player player)
	{
		for (PermissionGroup group : this.groups)
		{
			if (group.containsPlayer(player))
				return true;
		}

		return false;
	}

	public boolean containsPlayer(String player)
	{
		for (PermissionGroup group : this.groups)
		{
			if (group.containsPlayer(player))
				return true;
		}

		return false;
	}

	public void addPlayerToDefault(Player player)
	{
		for (PermissionGroup group : this.groups)
		{
			if (group.isDefault())
				group.addPlayer(player);
		}
	}

	public PermissionGroup getPermissionGroup(String group)
	{
		for (PermissionGroup g : this.groups)
		{
			if (g.getName().equals(group))
				return g;
		}

		return null;
	}

	public boolean containsDefaultPermissionGroup()
	{
		for (PermissionGroup g : this.groups)
		{
			if (g.isDefault())
				return true;
		}

		return false;
	}

	public void setDefaultPermissionGroup(PermissionGroup group)
	{
		if (this.groups.contains(group))
		{
			for (PermissionGroup g : this.groups)
				g.setDefault(false);

			group.setDefault(true);
		}
	}

	public PermissionGroup getDefaultPermissionGroup()
	{
		for (PermissionGroup g : this.groups)
		{
			if (g.isDefault())
				return g;
		}

		return null;
	}

	public ArrayList<PermissionGroup> getPermissionGroups()
	{
		return this.groups;
	}

	public void setPermissionGroups(ArrayList<PermissionGroup> groups)
	{
		this.groups = groups;
	}

	public boolean isEmpty()
	{
		return this.groups.isEmpty();
	}

	public void calculatePermission(Player player)
	{
		for (PermissionGroup group : this.groups)
		{
			if (group.containsPlayer(player))
			{
				group.addPlayer(player);
			}
		}
	}

	public void addInheritPermission(PermissionGroup group, String permission)
	{
		for (PermissionGroup g : this.groups)
		{
			if (g.containsInherit(group))
			{
				g.addPermission(permission);
			}
		}
	}

	public void removeInheritPermission(PermissionGroup group, String permission)
	{
		for (PermissionGroup g : this.groups)
		{
			if (g.containsInherit(group))
			{
				g.removePermission(permission);
			}
		}
	}

	public void addInheritGroup(PermissionGroup group)
	{
		for (PermissionGroup g : this.groups)
		{
			if (!g.containsInherit(group))
			{
				g.addInherit(group);
			}
		}
	}

	public void removeInheritGroup(PermissionGroup group)
	{
		for (PermissionGroup g : this.groups)
		{
			if (g.containsInherit(group))
			{
				g.removeInherit(group);
			}
		}
	}
}
