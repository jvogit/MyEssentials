package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StorageCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (!(arg0 instanceof Player)) {
			return false;
		}

		Player p = (Player) arg0;

		p.openInventory(p.getEnderChest());

		return true;
	}

}
