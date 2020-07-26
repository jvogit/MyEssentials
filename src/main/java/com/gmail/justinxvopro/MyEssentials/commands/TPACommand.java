package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justinxvopro.MyEssentials.managers.TeleportManager;

public class TPACommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player)) {
			return false;
		}

		Player p = (Player) s;

		if (args.length == 0) {
			p.sendMessage("Must be player!");
			return false;
		}

		Player target = Bukkit.getPlayer(args[0]);

		if (target == null) {
			p.sendMessage("Player is offline!");
			return false;
		}

		TeleportManager.sendRequest(p, target, false);

		return true;
	}

}
