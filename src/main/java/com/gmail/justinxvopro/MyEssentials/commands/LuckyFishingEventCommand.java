package com.gmail.justinxvopro.MyEssentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.justinxvopro.MyEssentials.Core;
import com.gmail.justinxvopro.MyEssentials.LuckyFishingEventTask;

public class LuckyFishingEventCommand implements CommandExecutor {

	private Core core;
	BukkitTask event_task;

	public LuckyFishingEventCommand(Core core) {
		this.core = core;
		event_task = LuckyFishingEventTask.scheduleTask(core);
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		event_task.cancel();
		LuckyFishingEventTask new_task = new LuckyFishingEventTask();
		new_task.run();
		event_task = new_task.schedule(core);

		return true;
	}

}
