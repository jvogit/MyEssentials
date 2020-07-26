package com.gmail.justinxvopro.MyEssentials.nms;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

	@SafeVarargs
	public static <T> T chooseRandomly(T... l) {
		return l[ThreadLocalRandom.current().nextInt(l.length)];
	}

	public static boolean chance(int chance) {
		return ThreadLocalRandom.current().nextInt(100) < chance;
	}

}
