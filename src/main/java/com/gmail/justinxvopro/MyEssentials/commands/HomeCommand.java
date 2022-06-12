package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.justinxvopro.MyEssentials.Core;
import com.gmail.justinxvopro.MyEssentials.managers.HomeManager;

public class HomeCommand implements CommandExecutor {
	private HomeManager manager;
	
	public HomeCommand(Core core) {
		manager = new HomeManager(core);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player)) {
			return false;
		}

		Player p = (Player) s;
		Location loc = p.getBedSpawnLocation() != null ? p.getBedSpawnLocation() : manager.getHomeLocation(p.getUniqueId());

		if (loc == null) {
			p.sendMessage("You do not have a home location!");
		} else {
			p.sendMessage("Teleporting you to home location.");
			p.teleport(loc, TeleportCause.COMMAND);
		}

		return true;
	}

}
