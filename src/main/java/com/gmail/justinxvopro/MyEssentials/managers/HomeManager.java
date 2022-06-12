package com.gmail.justinxvopro.MyEssentials.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import com.gmail.justinxvopro.MyEssentials.Core;

public class HomeManager implements Listener {
	private Map<UUID, Location> saved_world_locations = new HashMap<>();

	private final File HOME_FILE;
	private final FileConfiguration HOME_CONFIGURATION;

	public HomeManager(Core core) {
		this.HOME_FILE = new File(core.getDataFolder(), "home.yml");
		if (!this.HOME_FILE.exists()) {
			Core.LOGGER.info("Making a new home.yml file at " + this.HOME_FILE.getAbsolutePath());
			core.saveResource("home.yml", false);
		}
		this.HOME_CONFIGURATION = YamlConfiguration.loadConfiguration(HOME_FILE);
		this.HOME_CONFIGURATION.getKeys(false).stream().forEach(key -> {
			Object loc = this.HOME_CONFIGURATION.get(key);
			Core.LOGGER.info(key + " adding home location " + loc);
			try {
				saveHomeLocation(UUID.fromString(key), (Location) loc, false);
			} catch (IllegalArgumentException exception) {
				Core.LOGGER.warning(
						"Received a home location, but it is an invalid! Skipping. . ." + exception.getMessage());
			}
		});

		core.getServer().getPluginManager().registerEvents(this, core);
	}
	
	public void saveHomeLocation(UUID uuid, Location loc, boolean saveToFile) {
		this.saved_world_locations.put(uuid, loc);
		if (saveToFile) {
			this.HOME_CONFIGURATION.set(uuid.toString(), loc);
			try {
				this.HOME_CONFIGURATION.save(HOME_FILE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public Location getHomeLocation(UUID uuid) {
		return this.saved_world_locations.get(uuid);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInteract(PlayerBedEnterEvent event) {
		if (event.isCancelled()) return;
		saveHomeLocation(event.getPlayer().getUniqueId(), event.getBed().getLocation(), true);
		event.getPlayer().sendMessage("Your home location has been saved!");
	}
	
}
