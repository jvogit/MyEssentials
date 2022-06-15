package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.Location;

import java.util.function.Consumer;

public class PathFinderWalkToLocation extends Goal {
	public Mob entity;
	public Location loc;
	public boolean arrived;
	private Consumer<PathFinderWalkToLocation> consumerArrival;

	public PathFinderWalkToLocation(Mob entity, Location loc) {
		this(entity, loc, null);
	}

	public PathFinderWalkToLocation(Mob entity, Location loc,
			Consumer<PathFinderWalkToLocation> consumerArrival) {
		this.entity = entity;
		this.loc = loc;
		this.consumerArrival = consumerArrival;
	}

	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public void tick() {
		arrivalCheck();
		entity.getNavigation().moveTo(loc.getX(), loc.getY(), loc.getZ(), 0.5d);
	}

	private void arrivalCheck() {
		double diffX = (loc.getX() - entity.getX());
		double diffZ = (loc.getZ() - entity.getZ());

		if (diffX < 1 && diffX > -1 && diffZ < 1 && diffZ > -1) {
			if (!arrived) {
				if (this.consumerArrival != null)
					this.consumerArrival.accept(this);
				arrived = true;
			}
		} else {
			arrived = false;
		}
	}

}
