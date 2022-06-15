package com.gmail.justinxvopro.MyEssentials.commands;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.gmail.justinxvopro.MyEssentials.nms.CustomEntity;
import com.gmail.justinxvopro.MyEssentials.nms.DeliveryVillager;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackageCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player p)) {
			return false;
		}

		p.sendMessage("Sent!");

		if (args.length >= 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target != null) {
				p.sendMessage("Sent to " + target.getName());
				p = target;
			} else {
				p.sendMessage("Player offline!");
				return false;
			}
		}

		this.spawnDeliveryVillage(findSuitableLocation(p.getLocation(), 5, 10, 5), p);
		
		return true;
	}

	private Location findSuitableLocation(Location origin, double minRadius, double maxRadius, double yRadius) {
		List<Location> cands = new ArrayList<>();
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

		Collections.shuffle(cands);

		return cands.get(0);
	}

	private void spawnDeliveryVillage(Location at, Player to) {
		var level = ((CraftWorld) at.getWorld()).getHandle();
		DeliveryVillager b = (DeliveryVillager) CustomEntity.DELIVERY_VILLAGER.getType()
				.spawn(level, null, null, new BlockPos(at.getX(), at.getY(), at.getZ()), MobSpawnType.COMMAND, false, false);
		b.deliverTo(((CraftPlayer) to).getHandle());

		to.sendMessage("Your food is coming!");
	}

}
