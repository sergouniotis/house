package com.tsergouniotis.house.utils;

import java.util.Collection;

public final class CollectionUtils {

	private CollectionUtils() {

	}

	public static boolean isEmpty(Collection<?> value) {
		return null == value || value.size() < 1;
	}

	public static boolean isNotEmpty(Collection<?> value) {
		return !isEmpty(value);
	}

}
