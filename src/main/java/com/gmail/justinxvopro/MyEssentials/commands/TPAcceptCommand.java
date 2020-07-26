package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.justinxvopro.MyEssentials.managers.TeleportManager;
import com.gmail.justinxvopro.MyEssentials.managers.TeleportManager.TeleportRequestInfo;

public class TPAcceptCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player)) {
			return false;
		}

		Player p = (Player) s;
		TeleportRequestInfo requestInfo = TeleportManager.getRequest(p);

		if (requestInfo == null) {
			p.sendMessage("You have no requests!");
			return true;
		}

		if (args.length != 0) {
			String name = args[0];

			if (!requestInfo.REQUESTOR.getName().equalsIgnoreCase(name)) {
				p.sendMessage("No request from " + name);
				return true;
			}
		}

		Player requestor = requestInfo.REQUESTOR;

		if (!requestor.isOnline()) {
			p.sendMessage("Player is not online anymore!");
		}

		if (requestInfo.tpahere) {
			p.sendMessage("Teleporting to " + requestor.getName());
			requestor.sendMessage(p.getName() + " has accepted your teleport request!");
			p.teleport(requestor, TeleportCause.COMMAND);
		} else {
			requestor.sendMessage("Teleporting to " + p.getName());
			p.sendMessage("You accepted " + p.getName() + " request");
			requestor.teleport(p, TeleportCause.COMMAND);
		}

		return true;
	}

}
