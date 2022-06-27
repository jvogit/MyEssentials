package com.gmail.justinxvopro.MyEssentials;

import java.util.logging.Logger;

import com.gmail.justinxvopro.MyEssentials.commands.OpTridentCommand;
import com.gmail.justinxvopro.MyEssentials.commands.PickUpCommand;
import com.gmail.justinxvopro.MyEssentials.commands.SitCommand;
import com.gmail.justinxvopro.MyEssentials.managers.ExplosionManager;
import com.gmail.justinxvopro.MyEssentials.managers.ItemFrameInvisibleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.justinxvopro.MyEssentials.commands.BackCommand;
import com.gmail.justinxvopro.MyEssentials.commands.DimensionCommand;
import com.gmail.justinxvopro.MyEssentials.commands.DisablePhantom;
import com.gmail.justinxvopro.MyEssentials.commands.DisarmCommand;
import com.gmail.justinxvopro.MyEssentials.commands.HardmodeCommand;
import com.gmail.justinxvopro.MyEssentials.commands.HomeCommand;
import com.gmail.justinxvopro.MyEssentials.commands.LuckyFishingEventCommand;
import com.gmail.justinxvopro.MyEssentials.commands.PackageCommand;
import com.gmail.justinxvopro.MyEssentials.commands.PassiveCommand;
import com.gmail.justinxvopro.MyEssentials.commands.SmiteCommand;
import com.gmail.justinxvopro.MyEssentials.commands.StorageCommand;
import com.gmail.justinxvopro.MyEssentials.commands.TPACommand;
import com.gmail.justinxvopro.MyEssentials.commands.TPAHereCommand;
import com.gmail.justinxvopro.MyEssentials.commands.TPAcceptCommand;
import com.gmail.justinxvopro.MyEssentials.commands.TPDenyCommand;
import com.gmail.justinxvopro.MyEssentials.managers.MinecartManager;
import com.gmail.justinxvopro.MyEssentials.managers.TeleportManager;
import com.gmail.justinxvopro.MyEssentials.nms.CustomEntity;

public class Core extends JavaPlugin {
	public static Logger LOGGER;
	private String version = "1.19";

	@Override
	public void onLoad() {
		LOGGER = this.getLogger();

		if (checkNMS(version)) {
			this.getLogger().info("Registered Custom Entities");
			CustomEntity.registerEntities();
		} else {
			this.getLogger().info("NMS not 1.19 Disabling Custom Entities");
		}
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		if (!Bukkit.getVersion().contains("1.12")) {
			this.getCommand("nophantom").setExecutor(new DisablePhantom(this));
			this.getServer().getPluginManager().registerEvents(new SilkTouchSpawnerListener(), this);
		} else {
			this.getLogger().info("Certain Commands disabled");
		}

		// set up command executors
		this.getCommand("tpa").setExecutor(new TPACommand());
		this.getCommand("tpaccept").setExecutor(new TPAcceptCommand());
		this.getCommand("tpdeny").setExecutor(new TPDenyCommand());
		this.getCommand("tpahere").setExecutor(new TPAHereCommand());
		this.getCommand("home").setExecutor(new HomeCommand(this));
		this.getCommand("back").setExecutor(new BackCommand());
		this.getCommand("cartaddon").setExecutor(new MinecartManager(this));
		this.getCommand("dimension").setExecutor(new DimensionCommand(this));
		this.getCommand("storage").setExecutor(new StorageCommand());
		this.getCommand("smite").setExecutor(new SmiteCommand(this));
		this.getCommand("disarm").setExecutor(new DisarmCommand(this));
		var passiveCommand = new PassiveCommand(this);
		this.getCommand("passive").setExecutor(passiveCommand);
		this.getCommand("luckyfishingevent").setExecutor(new LuckyFishingEventCommand(this));
		this.getCommand("hardmode").setExecutor(new HardmodeCommand(this));
		this.getCommand("pickup").setExecutor(new PickUpCommand(this, passiveCommand));
		this.getCommand("sit").setExecutor(new SitCommand(this));
		this.getCommand("optrident").setExecutor(new OpTridentCommand(this));
		this.registerNMSCommands(version);

		// set up managers
		new TeleportManager(this);
		new ExplosionManager(this);
		new ItemFrameInvisibleManager(this);
	}

	public static String colored(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static boolean checkNMS(String version) {
		return Bukkit.getVersion().contains(version);
	}

	private void registerNMSCommands(String version) {
		if (!checkNMS(version)) {
			LOGGER.info("NMS not 1.19 Disabling NMS Commands. . .");
			return;
		}
		this.getCommand("package").setExecutor(new PackageCommand());
	}
}
