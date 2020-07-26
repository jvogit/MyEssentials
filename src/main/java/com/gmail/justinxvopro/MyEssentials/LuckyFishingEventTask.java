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
	private boolean initial_start_flag = false;
	
	public LuckyFishingEventTask() {
		
	}
	
	public LuckyFishingEventTask(boolean guarantee_start) {
		initial_start_flag = guarantee_start;
	}
	
	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().size() == 0 || RandomUtils.chance(50) && !initial_start_flag)
			return;
		initial_start_flag = false;
		PotionEffect lucky_effect = new PotionEffect(PotionEffectType.LUCK,
				20 * (ThreadLocalRandom.current().nextInt(120) + 90), 255);
		Bukkit.getOnlinePlayers().forEach(lucky_effect::apply);
		Bukkit.broadcastMessage(ChatColor.GREEN + "Lucky fishing event has started!!");
	}

	public BukkitTask schedule(Plugin plugin) {
		int delay = 20 * 60 * 120;
		return this.runTaskTimer(plugin, delay, delay);
	}

	public static BukkitTask scheduleTask(Plugin plugin) {
		return new LuckyFishingEventTask().schedule(plugin);
	}

}
