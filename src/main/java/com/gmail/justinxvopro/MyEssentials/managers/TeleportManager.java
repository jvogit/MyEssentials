package com.gmail.justinxvopro.MyEssentials.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.justinxvopro.MyEssentials.Core;

public class TeleportManager implements Listener {

	private final Core core;
	private static Map<Player, Location> lastLocation = new HashMap<>();
	private static Map<Player, TeleportRequestInfo> request = new HashMap<>();

	public TeleportManager(final Core core) {
		this.core = core;
		this.core.getServer().getPluginManager().registerEvents(this, this.core);
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		TeleportCause cause = event.getCause();
		if (TeleportCause.PLUGIN == cause || TeleportCause.COMMAND == cause) {
			lastLocation.put(event.getPlayer(), event.getFrom());
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		lastLocation.put(event.getEntity(), event.getEntity().getLocation());
	}

	public static Location getLastLocation(Player p) {
		return lastLocation.get(p);
	}

	public static void sendRequest(Player requestor, Player to, boolean tpahere) {
		addRequest(to, new TeleportRequestInfo(requestor, tpahere));
		if (!tpahere) {
			requestor.sendMessage("A request has been sent to " + to.getName());
			to.sendMessage(requestor.getName()
					+ Core.colored(" requests to be in your presence!\nType in &a/tpaccept &for &c/tpdeny"));
		} else {
			requestor.sendMessage("A request has been sent to " + to.getName());
			to.sendMessage(requestor.getName()
					+ Core.colored(" requests your presence!\nType in &a/tpaccept &for &c/tpdeny"));
		}
	}

	public static TeleportRequestInfo getRequest(Player p) {
		return request.remove(p);
	}

	private static void addRequest(Player target, TeleportRequestInfo info) {
		request.put(target, info);
	}

	public static class TeleportRequestInfo {
		public final Player REQUESTOR;
		public final boolean tpahere;

		TeleportRequestInfo(Player target, boolean tpahere) {
			this.REQUESTOR = target;
			this.tpahere = tpahere;
		}
	}

}
