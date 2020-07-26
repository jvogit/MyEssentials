package com.gmail.justinxvopro.MyEssentials.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gmail.justinxvopro.MyEssentials.Core;

public class PassiveCommand implements CommandExecutor, Listener {
	private Set<UUID> passive = new HashSet<>();

	public PassiveCommand(Core core) {
		core.getServer().getPluginManager().registerEvents(this, core);
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (!(arg0 instanceof Player)) {
			return false;
		}

		Player p = (Player) arg0;
		if (passive.contains(p.getUniqueId())) {
			passive.remove(p.getUniqueId());
			p.sendMessage("You are no longer on passive mode!");
		} else {
			passive.add(p.getUniqueId());
			p.sendMessage("You are on passive mode!");
		}

		return true;
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			if (passive.contains(event.getDamager().getUniqueId())) {
				event.setCancelled(true);
				event.getDamager().sendMessage("You are in passive mode!");
			}
			if (passive.contains(event.getEntity().getUniqueId())) {
				event.setCancelled(true);
				event.getDamager().sendMessage(event.getEntity().getName() + " is in passive mode!");
			}
		} else if (event.getDamager() instanceof Projectile) {
			if (((Projectile) event.getDamager()).getShooter() instanceof Player
					&& event.getEntity() instanceof Player) {
				Player shooter = (Player) ((Projectile) event.getDamager()).getShooter();
				if (passive.contains(shooter.getUniqueId())) {
					event.setCancelled(true);
					shooter.sendMessage("You are in passive mode!");
				}
				if (passive.contains(event.getEntity().getUniqueId())) {
					event.setCancelled(true);
					event.getEntity().setFireTicks(0);
					shooter.sendMessage(event.getEntity().getName() + " is in passive mode!");
				}
			}
		}
	}

}
