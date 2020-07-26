package com.gmail.justinxvopro.MyEssentials;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.justinxvopro.MyEssentials.nms.RandomUtils;

public class LuckyFishingEventTask extends BukkitRunnable {

	@Override
	public void run() {
		if (RandomUtils.chance(50))
			return;
		PotionEffect lucky_effect = new PotionEffect(PotionEffectType.LUCK,
				20 * (ThreadLocalRandom.current().nextInt(120) + 90), 255);
		Bukkit.getOnlinePlayers().forEach(lucky_effect::apply);
		Bukkit.broadcastMessage(ChatColor.GREEN + "Lucky fishing event has started!!");
	}

	public BukkitTask schedule(Plugin plugin) {
		int delay = 20 * 60 * 10;
		return this.runTaskTimer(plugin, delay, delay);
	}

	public static BukkitTask scheduleTask(Plugin plugin) {
		return new LuckyFishingEventTask().schedule(plugin);
	}

}
