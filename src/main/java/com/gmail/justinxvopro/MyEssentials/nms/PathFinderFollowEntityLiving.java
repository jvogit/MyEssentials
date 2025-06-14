package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.function.Consumer;

public class PathFinderFollowEntityLiving extends Goal {
	public Mob entity;
	public Entity target;
	private boolean arrived = false;
	private Consumer<PathFinderFollowEntityLiving> consumerArrival;

	public PathFinderFollowEntityLiving(Mob entity, Entity target) {
		this(entity, target, (x) -> {});
	}

	public PathFinderFollowEntityLiving(Mob entity, Entity target,
										Consumer<PathFinderFollowEntityLiving> consumerArrival) {
		this.entity = entity;
		this.target = target;
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
			this.entity.getNavigation().moveTo(this.target, 0.5d);
		}
		arrivalCheck();
	}

	private void arrivalCheck() {
		if (this.entity.position().subtract(target.position()).horizontalDistanceSqr() < 4.f) {
			if (!arrived) {
				this.consumerArrival.accept(this);
				arrived = true;
			}
		} else {
			arrived = false;
		}
	}
}
