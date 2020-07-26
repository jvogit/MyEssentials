package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class HomeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player)) {
			return false;
		}

		Player p = (Player) s;
		Location loc = p.getBedSpawnLocation();

		if (loc == null) {
			p.sendMessage("You do not have a bed location!");
		} else {
			p.sendMessage("Teleporting you to bed location.");
			p.teleport(loc, TeleportCause.COMMAND);
		}

		return true;
	}

}
