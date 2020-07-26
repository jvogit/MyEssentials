package com.gmail.justinxvopro.MyEssentials.nms;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;

import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.EntityTypes.Builder;
import net.minecraft.server.v1_16_R1.EntityTypes.b;
import net.minecraft.server.v1_16_R1.EnumCreatureType;
import net.minecraft.server.v1_16_R1.WorldServer;

public enum CustomEntity {
	DELIVERY_VILLAGER("delivery_villager", EnumCreatureType.CREATURE, DeliveryVillager::new),
	RIDEABLE_POLAR_BEAR("rideable_polar_bear", EnumCreatureType.CREATURE, RideablePolarBear::new);

	private String customName; 
	private EnumCreatureType typeName;
	private b<?> b;
	private EntityTypes<? extends Entity> custom_type;

	private <T extends Entity> CustomEntity(String customName, EnumCreatureType typeName, b<T> b) {
		this.customName = customName;
		this.b = b;
	}

	public String getCustomName() {
		return customName;
	}

	public Entity spawn(Location loc) {
		WorldServer w = ((CraftWorld) loc.getWorld()).getHandle();
		Entity entity = this.custom_type.spawnCreature(w, null, null, null, new BlockPosition(loc.getX(), loc.getY(), loc.getZ()),
				null, false, false);
		
		return entity;
	}
	
	public void inject() throws Exception {
		this.custom_type = injectNewEntity(typeName, customName, b);
	}
	
	public static void registerEntities() {
		Stream.of(values()).forEach(t -> {
			try {
				t.inject();
				Bukkit.getLogger().log(Level.INFO, "Registered " + t.getCustomName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	private static <T extends Entity> EntityTypes<T> injectNewEntity(EnumCreatureType type, String name, b<T> b) throws Exception {
		Constructor<Builder> constructor = EntityTypes.Builder.class.getDeclaredConstructor(b.class, EnumCreatureType.class);
		constructor.setAccessible(true);
		return constructor.newInstance(b, type).a(name);
	}
}
