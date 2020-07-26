package com.gmail.justinxvopro.MyEssentials.nms;

import java.util.function.Consumer;

import org.bukkit.Location;

import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.PathEntity;
import net.minecraft.server.v1_14_R1.PathfinderGoal;

public class PathFinderWalkToLocation extends PathfinderGoal {
	public EntityInsentient entity;
	public Location loc;
	public boolean arrived;
	private Consumer<PathFinderWalkToLocation> consumerArrival;

	public PathFinderWalkToLocation(EntityInsentient entity, Location loc) {
		this(entity, loc, null);
	}

	public PathFinderWalkToLocation(EntityInsentient entity, Location loc,
			Consumer<PathFinderWalkToLocation> consumerArrival) {
		this.entity = entity;
		this.loc = loc;
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
		PathEntity path = entity.getNavigation().a(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		arrivalCheck();
		entity.getNavigation().a(path, 0.5d);
	}

	private void arrivalCheck() {
		double diffX = (loc.getX() - entity.locX);
		double diffZ = (loc.getZ() - entity.locZ);

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
