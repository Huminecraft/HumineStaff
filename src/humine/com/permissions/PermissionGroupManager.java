package humine.com.permissions;

import java.util.ArrayList;

public class PermissionGroupManager
{
	private ArrayList<PermissionGroup> groups;
	
	public PermissionGroupManager()
	{
		this.groups = new ArrayList<PermissionGroup>();
	}
	
	public void addPermissionGroup(PermissionGroup group) {
		this.groups.add(group);
	}
	
	public void removePermissionGroup(PermissionGroup group) {
		this.groups.remove(group);
	}
	
	public boolean containsPermissionGroup(PermissionGroup group) {
		return this.groups.contains(group);
	}
	
	public PermissionGroup getPermissionGroup(String group){
		for(PermissionGroup g : this.groups) {
			if(g.getName().equals(group))
				return g;
		}
		
		return null;
	}
	
	public void setDefaultPermissionGroup(PermissionGroup group) {
		if(this.groups.contains(group)) {
			for(PermissionGroup g : this.groups)
				g.setDefault(false);
			
			group.setDefault(true);
		}
	}
	
	public PermissionGroup getDefaultPermissionGroup(PermissionGroup group) {
		if(this.groups.contains(group)) {
			for(PermissionGroup g : this.groups) {
				if(g.isDefault())
					return g;
			}
		}
		
		return null;
	}
	
	public ArrayList<PermissionGroup> getPermissionGroups() {
		return this.groups;
	}

	public void setPermissionGroups(ArrayList<PermissionGroup> groups)
	{
		this.groups = groups;
	}
	
	public boolean isEmpty() {
		return this.groups.isEmpty();
	}
}
