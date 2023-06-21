package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public final class ReflectUtils {

	public static void unfreezeRegistry() {
		// https://github.com/datatags/UltraCosmetics/blob/master/v1_20_R1/src/main/java/be/isach/ultracosmetics/v1_20_R1/customentities/CustomEntities.java
		Class<?> registryClass = MappedRegistry.class;
		try {
			Field intrusiveHolderCache = registryClass.getDeclaredField("m");
			intrusiveHolderCache.setAccessible(true);
			intrusiveHolderCache.set(Registry.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
			Field frozen = registryClass.getDeclaredField("l");
			frozen.setAccessible(true);
			frozen.set(Registry.ENTITY_TYPE, false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
	}
}
