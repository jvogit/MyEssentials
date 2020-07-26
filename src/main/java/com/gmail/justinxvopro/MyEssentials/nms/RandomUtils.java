package com.gmail.justinxvopro.MyEssentials.nms;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

	public static <T> T chooseRandomly(T... lT) {
		return lT[ThreadLocalRandom.current().nextInt(lT.length)];
	}

	public static boolean chance(int chance) {
		return ThreadLocalRandom.current().nextInt(100) < chance;
	}

}
