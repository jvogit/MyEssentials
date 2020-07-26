package com.gmail.justinxvopro.MyEssentials.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.justinxvopro.MyEssentials.Core;
import com.gmail.justinxvopro.MyEssentials.managers.LocationManager;

public class DimensionCommand implements CommandExecutor {
	@SuppressWarnings("unused")
	private LocationManager manager;
	private String[] valid;

	public DimensionCommand(Core core) {
		List<String> string = core.getConfig().getStringList("dimensions");
		valid = string.toArray(new String[string.size()]);
		this.loadWorlds();
		this.manager = new LocationManager(core);
	}

	private void loadWorlds() {
		Core.LOGGER.info("Attempting to load worlds if any. . .");
		Stream.of(Bukkit.getWorldContainer().listFiles()).filter(file -> {
			return file.isDirectory() && Arrays.asList(valid).contains(file.getName().toLowerCase());
		}).forEach(world -> {
			Core.LOGGER.info("Detected " + world.getName() + " has potential world! Loading . . .");
			Bukkit.createWorld(new WorldCreator(world.getName()));
			Core.LOGGER.info("Loaded " + world.getName());
		});
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String raw, String[] args) {
		if (args.length == 0) {
			s.sendMessage("Available Worlds:\n" + Stream.of(valid).collect(Collectors.joining("\n")));
			return false;
		}

		if (!(s instanceof Player) && args.length != 2) {
			return false;
		}

		Optional<String> name = Stream.of(valid).filter(str -> str.equalsIgnoreCase(args[0])).findAny();

		if (!name.isPresent()) {
			s.sendMessage("Available Worlds:\n" + Stream.of(valid).collect(Collectors.joining("\n")));
			return false;
		}

		World world = Bukkit.getWorld(name.get());

		if (world == null) {
			s.sendMessage("That world does not currently exist!");
			return true;
		}

		if (args.length == 2) {
			if (!s.hasPermission("myessentials.operator"))
				return false;

			Player target = Bukkit.getPlayer(args[1]);
			if (target != null) {
				s.sendMessage("Successfully used Dimension command on " + target.getName());
				this.transportToDimension(target, world);
			} else {
				s.sendMessage("Player offline");
			}

			return true;
		}

		Player p = (Player) s;

		this.transportToDimension(p, world);
		return true;
	}

	private void transportToDimension(Player p, World world) {
		if (p.getWorld() == world) {
			p.sendMessage("Sending you to the world's spawnpoint. . .");
			p.teleport(world.getSpawnLocation(), TeleportCause.PLUGIN);
		} else {
			p.teleport(world.getSpawnLocation(), TeleportCause.PLUGIN);
			p.sendMessage("Sending you to a magical world. . .");
		}
	}

}
