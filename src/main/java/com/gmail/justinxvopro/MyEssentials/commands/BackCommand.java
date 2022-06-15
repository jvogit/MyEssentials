package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.justinxvopro.MyEssentials.managers.TeleportManager;

public class BackCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player)) {
			return false;
		}

		Player p = (Player) s;
		Location loc = TeleportManager.getLastLocation(p);

		if (loc == null) {
			p.sendMessage("You have no last location!");
		} else {
			p.teleport(loc, TeleportCause.COMMAND);
		}

		return true;
	}

}
