package com.gmail.justinxvopro.MyEssentials.commands;

import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.justinxvopro.MyEssentials.Core;

public class SmiteCommand implements CommandExecutor, Listener {

	public SmiteCommand(Core core) {
		core.getServer().getPluginManager().registerEvents(this, core);
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 0) {
			return false;
		}

		Player target = Bukkit.getPlayer(arg3[0]);

		if (target == null) {
			arg0.sendMessage(arg3 + " not online!");
			return false;
		}

		target.getWorld().strikeLightning(target.getLocation());
		Bukkit.broadcastMessage(arg0.getName() + " has smited " + target.getName());

		return true;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();

		if (!p.isOp())
			return;

		Location base = p.getLocation().add(0, -1, 0);
		this.lightningEffects(base);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();

		if (!p.isOp())
			return;

		Location base = p.getLocation().add(0, -1, 0);
		this.lightningEffects(base);
	}

	public void lightningEffects(Location base) {
		Location[] corners = { base.add(1, 0, 1), base.add(1, 0, -1), base.add(-1, 0, -1), base.add(-1, 0, 1) };

		Stream.of(corners).forEach(base.getWorld()::strikeLightningEffect);
	}

}
