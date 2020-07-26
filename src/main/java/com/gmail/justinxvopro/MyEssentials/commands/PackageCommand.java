package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.gmail.justinxvopro.MyEssentials.nms.CustomEntity;
import com.gmail.justinxvopro.MyEssentials.nms.DeliveryVillager;

public class PackageCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player)) {
			return false;
		}

		Player p = (Player) s;
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

		this.spawnCustomVillager(p, p.getLocation());
		
		return true;
	}

	private void spawnCustomVillager(Player p, Location loc) {
		DeliveryVillager b = (DeliveryVillager) CustomEntity.DELIVERY_VILLAGER.spawn(loc.add(5, 1, 5));
		b.deliverTo(((CraftPlayer) p).getHandle());

		p.sendMessage("Your food is coming!");
	}

}
