package com.tsergouniotis.house.utils;

import java.util.Objects;

public final class StringUtils {

	private StringUtils() {

	}

	public static boolean isNonEmpty(String value) {
		return null != value && value.length() > 0;
	}

	public static boolean hasText(String value) {
		return Objects.isNull(value) || value.length() < 1;
	}

}
