package com.gmail.justinxvopro.MyEssentials.commands;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.gmail.justinxvopro.MyEssentials.Core;

public class DisarmCommand implements CommandExecutor, Listener {

	public DisarmCommand(Core core) {
		core.getServer().getPluginManager().registerEvents(this, core);
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (!(s instanceof Player)) {
			return false;
		}

		Player p = (Player) s;

		if (args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target != null) {
				p.sendMessage("Disarmed " + target.getName());
				this.disarm(target);
				return true;
			} else {
				p.sendMessage("Player offline!");
				return false;
			}
		}

		return true;
	}

	private void disarm(Player target) {
		target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 1.0f);
		Stream.of(target.getInventory().getArmorContents()).forEach(item -> {
			this.dropItem(target.getLocation(), item);
		});

		this.dropItem(target.getLocation(), target.getInventory().getItemInMainHand());
		this.dropItem(target.getLocation(), target.getInventory().getItemInOffHand());
	}

	private void dropItem(Location loc, ItemStack stack) {
		if (stack == null || stack.getType() == Material.AIR)
			return;
		Item iteme = loc.getWorld().dropItemNaturally(loc, stack.clone());
		iteme.setPickupDelay(20 * 1);
		iteme.setVelocity(iteme.getVelocity().multiply(
				(ThreadLocalRandom.current().nextBoolean() ? -1 : 1) * ThreadLocalRandom.current().nextDouble() + 1d));
		stack.setAmount(0);
	}
}
