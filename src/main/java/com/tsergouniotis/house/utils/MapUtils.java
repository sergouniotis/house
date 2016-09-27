package com.tsergouniotis.house.utils;

import java.util.Map;

public final class MapUtils {

	private MapUtils() {

	}

	public static boolean isEmpty(Map<?, ?> map) {
		return null == map || map.isEmpty();
	}

}
