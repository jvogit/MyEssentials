package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class PathFinderWalkToLocation extends Goal {
	public Mob entity;
	public Vec3 loc;
	public boolean arrived = false;
	private Consumer<PathFinderWalkToLocation> consumerArrival;

	public PathFinderWalkToLocation(Mob entity, Vec3 loc) {
		this(entity, loc, (x) -> {});
	}

	public PathFinderWalkToLocation(Mob entity, Vec3 loc,
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
		if (entity.getNavigation().isDone())
		{
			entity.getNavigation().moveTo(loc.x, loc.y, loc.z, 0.5d);
		}
		arrivalCheck();
	}

	private void arrivalCheck() {
		if (this.entity.position().subtract(loc).horizontalDistanceSqr() < 4.f) {
			if (!arrived) {
				this.consumerArrival.accept(this);
				arrived = true;
			}
		} else {
			arrived = false;
		}
	}
}
