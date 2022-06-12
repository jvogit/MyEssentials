package com.gmail.justinxvopro.MyEssentials.commands;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.justinxvopro.MyEssentials.Core;
import com.gmail.justinxvopro.MyEssentials.nms.RandomUtils;

public class HardmodeCommand implements CommandExecutor, Listener {
	private Core core;
	private boolean enabled = false;
	private String[] death_messages = {
		"%s ruined it for everyone.",
		"%s fucked up.",
		"bruh %s",
		"%s killed everyone."
	};
	
	public HardmodeCommand(Core core) {
		this.enabled = core.getConfig().getBoolean("hard-mode");
		this.core = core;
		core.getServer().getPluginManager().registerEvents(this, core);
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		this.enabled = !this.enabled;
		arg0.sendMessage("Hard-mode : " + enabled);
		
		return true;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (enabled) {
			event.setDeathMessage(String.format(RandomUtils.chooseRandomly(death_messages), event.getEntity().getName()));
			
			if (event.getEntity().hasMetadata("to-kill")) {
				String killer = event.getEntity().getMetadata("to-kill").get(0).asString();
				event.setDeathMessage(String.format("%s was killed because of %s", event.getEntity().getName(), killer));
				event.getEntity().removeMetadata("to-kill", core);
				return;
			}
			
			Bukkit.getOnlinePlayers().forEach(p -> { 
				if (!p.equals(event.getEntity())) {
					p.setMetadata("to-kill", new FixedMetadataValue(core, event.getEntity().getName()));
					p.setHealth(0);
				}
			});
		}
	}

}
