package humine.fr.utils;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_15_R1.PlayerConnection;

public class TabList {

	private String header;
	private String footer;

	private ChatComponentText headerComponent;
	private ChatComponentText footerComponent;

	private PacketPlayOutPlayerListHeaderFooter packet;

	public TabList(String header, String footer) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.header = header;
		this.footer = footer;
		this.headerComponent = new ChatComponentText(header);
		this.footerComponent = new ChatComponentText(footer);
		this.packet = new PacketPlayOutPlayerListHeaderFooter();
		
		Field a = packet.getClass().getDeclaredField("header");
		a.setAccessible(true);
		a.set(packet, this.headerComponent);
		a.setAccessible(false);
		
		Field b = packet.getClass().getDeclaredField("footer");
		b.setAccessible(true);
		b.set(packet, this.footerComponent);
		b.setAccessible(false);
	}

	public TabList() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this("", "");
	}

	public void sendPacket(Player player) {
		CraftPlayer p = (CraftPlayer) player;
		PlayerConnection connection = p.getHandle().playerConnection;
		connection.sendPacket(this.packet);
	}
	
	public String getFooter() {
		return footer;
	}
	
	public String getHeader() {
		return header;
	}
}
