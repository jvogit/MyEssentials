package com.gmail.justinxvopro.MyEssentials.nms;

import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.DataConverterRegistry;
import net.minecraft.server.v1_14_R1.DataConverterTypes;
import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EntityTypes.b;
import net.minecraft.server.v1_14_R1.EnumCreatureType;
import net.minecraft.server.v1_14_R1.IRegistry;
import net.minecraft.server.v1_14_R1.SharedConstants;
import net.minecraft.server.v1_14_R1.WorldServer;

public enum CustomEntity {
	DELIVERY_VILLAGER("delivery_villager", "villager", DeliveryVillager::new),
	RIDEABLE_POLAR_BEAR("rideable_polar_bear", "polar_bear", RideablePolarBear::new);

	private b<? extends Entity> b;
	private String customName, typeName;
	private EntityTypes<? extends Entity> custom_type;

	private <T extends Entity> CustomEntity(String customName, String typeName, b<T> b) {
		this.customName = customName;
		this.typeName = typeName;
		this.b = b;
	}

	private void register() {
		this.custom_type = CustomEntity.register(customName, typeName, b);
	}

	public String getCustomName() {
		return customName;
	}

	public String getTypeName() {
		return typeName;
	}

	public Entity spawn(Location loc) {
		WorldServer w = ((CraftWorld) loc.getWorld()).getHandle();
		Entity entity = this.custom_type.b(w, null, null, null, new BlockPosition(loc.getX(), loc.getY(), loc.getZ()),
				null, false, false);

		w.addEntity(entity);
		return entity;
	}

	public static void registerEntities() {
		Stream.of(CustomEntity.values()).forEach(CustomEntity::register);
	}

	public static <T extends Entity> EntityTypes<T> register(String customName, String type, b<T> b) {
		Map<String, Type<?>> types = (Map<String, Type<?>>) DataConverterRegistry.a()
				.getSchema(DataFixUtils.makeKey(SharedConstants.a().getWorldVersion()))
				.findChoiceType(DataConverterTypes.ENTITY).types();
		types.put("minecraft:" + customName, types.get("minecraft:" + type));

		EntityTypes.a<T> a = EntityTypes.a.a(b, EnumCreatureType.CREATURE);

		return IRegistry.a(IRegistry.ENTITY_TYPE, customName, a.a(customName));
	}
}
