package com.gmail.justinxvopro.MyEssentials.nms;

import java.util.function.Consumer;

import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.EntityInsentient;
import net.minecraft.server.v1_16_R1.EntityLiving;
import net.minecraft.server.v1_16_R1.PathEntity;
import net.minecraft.server.v1_16_R1.PathfinderGoal;

public class PathFinderFollowEntityLiving extends PathfinderGoal {
	public EntityInsentient entity;
	public EntityLiving target;
	private boolean arrived = false;
	private Consumer<PathFinderFollowEntityLiving> consumerArrival;

	public PathFinderFollowEntityLiving(EntityInsentient entity, EntityLiving target) {
		this(entity, target, null);
	}

	public PathFinderFollowEntityLiving(EntityInsentient entity, EntityLiving target,
			Consumer<PathFinderFollowEntityLiving> consumerArrival) {
		this.entity = entity;
		this.target = target;
		this.consumerArrival = consumerArrival;
	}

	@Override
	public boolean a() {
		return true;
	}

	@Override
	public boolean b() {
		return false;
	}

	@Override
	public void c() {
		PathEntity path = entity.getNavigation().a(new BlockPosition(target.locX(), target.locY(), target.locZ()), 0); // o val ?
		arrivalCheck();
		entity.getNavigation().a(path, 0.5d);
	}

	private void arrivalCheck() {
		double diffX = (target.locX() - entity.locX());
		double diffZ = (target.locZ() - entity.locZ());

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
