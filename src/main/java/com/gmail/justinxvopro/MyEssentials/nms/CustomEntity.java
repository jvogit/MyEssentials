package com.gmail.justinxvopro.MyEssentials.nms;

import com.gmail.justinxvopro.MyEssentials.Core;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.npc.Villager;

public class CustomEntity<T extends Entity> {
	public static final CustomEntity<DeliveryVillager> DELIVERY_VILLAGER = new CustomEntity<>("delivery_villager", EntityType.Builder.of(DeliveryVillager::new, MobCategory.MISC));

	private final String id;
	private final EntityType.Builder builder;
	private EntityType<T> type;
	private CustomEntity(String id, EntityType.Builder builder) {
		this.id = id;
		this.builder = builder;
	}

	public EntityType<T> getType() {
		return this.type;
	}

	private void register() {
		type = register(id, builder);
	}

	public static void registerEntities() {
		ReflectUtils.unfreezeRegistry();

		DELIVERY_VILLAGER.register();

		Registry.ENTITY_TYPE.freeze();
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder type) {
		ResourceLocation key = new ResourceLocation(id);
		if (Registry.ENTITY_TYPE.containsKey(key)) {
			Core.LOGGER.info(id + " is already registered!");
			return (EntityType) Registry.ENTITY_TYPE.get(key);
		}

		Core.LOGGER.info("Registered " + id);
		return (EntityType) Registry.register(Registry.ENTITY_TYPE, id, (EntityType<T>) type.build(id));
	}
}
