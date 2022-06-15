package com.gmail.justinxvopro.MyEssentials.nms;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public final class ReflectUtils {

	public static void unfreezeRegistry() {
		// https://github.com/iSach/UltraCosmetics/blob/master/v1_19_R1/src/main/java/be/isach/ultracosmetics/v1_18_R2/customentities/CustomEntities.java
		Class<?> registryClass = MappedRegistry.class;
		try {
			Field intrusiveHolderCache = registryClass.getDeclaredField("cc");
			intrusiveHolderCache.setAccessible(true);
			intrusiveHolderCache.set(Registry.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
			Field frozen = registryClass.getDeclaredField("ca");
			frozen.setAccessible(true);
			frozen.set(Registry.ENTITY_TYPE, false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
	}
}
