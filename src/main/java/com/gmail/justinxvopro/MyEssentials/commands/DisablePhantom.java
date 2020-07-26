package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.gmail.justinxvopro.MyEssentials.Core;

public class DisablePhantom implements CommandExecutor, Listener {
	private boolean disabled = true;

	public DisablePhantom(Core core) {
		core.getServer().getPluginManager().registerEvents(this, core);
	}

	@EventHandler
	public void onSpawn(EntitySpawnEvent event) {
		if (disabled && event.getEntityType() == EntityType.PHANTOM) {
			event.setCancelled(true);
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] arg3) {
		disabled = !disabled;
		s.sendMessage("Phantom spawning disabled? " + disabled);

		return true;
	}
}
