package com.gmail.justinxvopro.MyEssentials;

import com.gmail.justinxvopro.MyEssentials.commands.OpTridentCommand;
import com.gmail.justinxvopro.MyEssentials.commands.PickUpCommand;
import com.gmail.justinxvopro.MyEssentials.commands.SitCommand;
import com.gmail.justinxvopro.MyEssentials.managers.ExplosionManager;
import com.gmail.justinxvopro.MyEssentials.managers.ItemFrameInvisibleManager;
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

import io.papermc.paper.ServerBuildInfo;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class Core extends JavaPlugin {
	public static ComponentLogger LOGGER;
	public static final String VERSION = "1.21";

	@Override
	public void onLoad() {
		LOGGER = getComponentLogger();
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();

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
		this.getCommand("nophantom").setExecutor(new DisablePhantom(this));
		this.registerNMSCommands(VERSION);

		// set up managers
		new TeleportManager(this);
		new ExplosionManager(this);
		new ItemFrameInvisibleManager(this);
		this.getServer().getPluginManager().registerEvents(new SilkTouchSpawnerListener(), this);
	}

	public static String colored(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static boolean checkNMS(String version) {
		return ServerBuildInfo.buildInfo().minecraftVersionId().contains(version);
	}

	private void registerNMSCommands(String version) {
		if (!checkNMS(version)) {
			LOGGER.info(String.format("NMS not %s Disabling NMS Commands. . .", version));
			return;
		}
		this.getCommand("package").setExecutor(new PackageCommand());
	}
}
