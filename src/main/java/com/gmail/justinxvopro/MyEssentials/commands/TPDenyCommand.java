package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justinxvopro.MyEssentials.managers.TeleportManager;
import com.gmail.justinxvopro.MyEssentials.managers.TeleportManager.TeleportRequestInfo;

public class TPDenyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player)) {
			return false;
		}

		Player p = (Player) s;
		TeleportRequestInfo info = TeleportManager.getRequest(p);

		if (info == null) {
			p.sendMessage("You have no request!");
		} else {
			info.REQUESTOR.sendMessage(p.getName() + " has denied your request.");
			p.sendMessage("Request from " + info.REQUESTOR.getName() + " denied.");
		}

		return true;
	}

}
