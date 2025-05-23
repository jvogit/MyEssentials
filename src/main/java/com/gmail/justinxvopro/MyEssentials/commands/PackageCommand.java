package com.gmail.justinxvopro.MyEssentials.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.gmail.justinxvopro.MyEssentials.nms.DeliveryVillager;

public class PackageCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (args.length >= 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target != null) {
				s.sendMessage("Sent to " + target.getName());
				target.sendMessage("You have a delivery coming from " + s.getName());
				this.spawnDeliveryVillage(findSuitableLocation(target.getLocation(), 10, 20, 5), target);
			} else {
				s.sendMessage("Player offline!");
			}
		} else if (s instanceof Player p) {
			s.sendMessage("You have a delivery coming!");
			this.spawnDeliveryVillage(findSuitableLocation(p.getLocation(), 10, 20, 5), p);
		}

		return true;
	}

	private Location findSuitableLocation(Location origin, double minRadius, double maxRadius, double yRadius) {
		List<Location> cands = new ArrayList<>();
		cands.add(origin);

		for (double i = origin.getBlockX() - maxRadius; i <= origin.getBlockX() + maxRadius; i++) {
			for (double j = origin.getBlockY() - yRadius; j <= origin.getBlockY() + yRadius; j++) {
				for (double k = origin.getBlockZ() - maxRadius; k <= origin.getBlockZ() + maxRadius; k++) {
					Location loc = new Location(origin.getWorld(), i, j, k);
					if (loc.distanceSquared(origin) < minRadius*minRadius) continue;
					if (loc.getBlock().isSolid() && loc.add(0, 1, 0).getBlock().isPassable() && loc.add(0, 2, 0).getBlock().isPassable())
						cands.add(loc);
				}
			}
		}

		return cands.get(ThreadLocalRandom.current().nextInt(cands.size()));
	}

	private void spawnDeliveryVillage(Location at, Player to) {
		var level = ((CraftWorld) at.getWorld()).getHandle();
		DeliveryVillager b = new DeliveryVillager(level);
		b.setPos(at.getBlockX(), at.getBlockY(), at.getBlockZ());
		level.addFreshEntity(b, SpawnReason.CUSTOM);
		b.deliverTo(((CraftPlayer) to).getHandle());
		to.sendMessage("Your food is coming!");
	}

}
