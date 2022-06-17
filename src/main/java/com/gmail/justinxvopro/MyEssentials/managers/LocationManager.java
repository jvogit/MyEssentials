package com.gmail.justinxvopro.MyEssentials.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.justinxvopro.MyEssentials.Core;

public class LocationManager implements Listener {
	private Map<UUID, List<Location>> saved_world_locations = new HashMap<>();

	private final File LOCATIONS_FILE;
	private final FileConfiguration FILE_CONFIGURATION;

	public LocationManager(Core core) {
		this.LOCATIONS_FILE = new File(core.getDataFolder(), "locations.yml");
		if (!this.LOCATIONS_FILE.exists()) {
			Core.LOGGER.info("Making a new locations.yml file at " + this.LOCATIONS_FILE.getAbsolutePath());
			core.saveResource("locations.yml", false);
		}
		this.FILE_CONFIGURATION = YamlConfiguration.loadConfiguration(LOCATIONS_FILE);
		this.FILE_CONFIGURATION.getKeys(false).forEach(key -> {
			Core.LOGGER.info(key + " has saved locations!");
			this.FILE_CONFIGURATION.getList(key).forEach(loc -> {
				Core.LOGGER.info(key + " adding location " + loc);
				try {
					saveWorldLocation(UUID.fromString(key), (Location) loc);
				} catch (Exception exception) {
					// location is invalid. ex: world may be gone!
					Core.LOGGER.warning(
							"Received a location, but it is an invalid! Skipping. . ." + exception.getMessage());
				}
			});
		});

		core.getServer().getPluginManager().registerEvents(this, core);
	}

	public void saveWorldLocation(UUID uuid, Location l) {
		Optional<List<Location>> locations = Optional.ofNullable(this.saved_world_locations.get(uuid));
		if (locations.isPresent()) {
			replaceLocation(locations.get(), l);
		} else {
			this.saved_world_locations.put(uuid, new ArrayList<Location>(Arrays.asList(l)));
		}
	}

	public void replaceLocation(List<Location> to, Location with) {
		for (int i = 0; i < to.size(); i++) {
			if (to.get(i).getWorld().equals(with.getWorld())) {
				to.remove(i);
				break;
			}
		}

		to.add(with);
	}

	public void storeLocation(Player p, Location l) {
		this.saveWorldLocation(p.getUniqueId(), l);
		this.FILE_CONFIGURATION.set(p.getUniqueId().toString(), this.saved_world_locations.get(p.getUniqueId()));
		try {
			Core.LOGGER.info("Attempting to save " + p + " location " + l);
			this.FILE_CONFIGURATION.save(this.LOCATIONS_FILE);
		} catch (IOException e) {
			e.printStackTrace();
			Core.LOGGER.severe("Unable to save location to memory: " + p.getUniqueId() + " " + l.toString() + " "
					+ e.getMessage());
		}
	}

	public Optional<List<Location>> getLocations(Player p) {
		return Optional.ofNullable(this.saved_world_locations.get(p.getUniqueId()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTeleport(PlayerTeleportEvent e) {
		if ((e.getCause() != TeleportCause.COMMAND && e.getCause() != TeleportCause.PLUGIN)
				|| e.getFrom().getWorld().equals(e.getTo().getWorld()) || e.isCancelled())
			return;

		storeLocation(e.getPlayer(), e.getFrom());

		if (e.getCause() == TeleportCause.PLUGIN)
			getLocations(e.getPlayer()).ifPresent(list -> {
				e.setTo(list.stream().filter(loc -> loc.getWorld().equals(e.getTo().getWorld())).findAny()
						.orElse(e.getTo()));
			});

		e.getPlayer().sendMessage("Your location in the previous dimension has been saved!");
	}

}
