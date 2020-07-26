package com.gmail.justinxvopro.MyEssentials.nms;

import java.lang.reflect.Field;

public class ReflectUtils {

	public static <E, T> T getPrivateField(String fieldName, Class<E> clazz, E instance, Class<T> returned) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);

			return returned.cast(field.get(instance));
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		return null;
	}

}
