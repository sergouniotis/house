package com.tsergouniotis.house.graph;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.tsergouniotis.house.entities.Payment;

public final class GraphUtils {

	private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	protected static final String TARGET_DATE_STR = "2018-01-01";

	private GraphUtils() {

	}

	protected static Map<String, BigDecimal> map(List<Payment> payments) {
		Map<String, BigDecimal> map = new TreeMap<>();
		for (Payment p : payments) {

			String key = p.getPaymentDay().format(YYYY_MM_DD);
			BigDecimal value = p.getAmount();
			if (map.containsKey(key)) {
				value = value.add(map.get(key));
				map.put(key, value);
			} else {
				map.put(key, value);
			}

		}
		return map;
	}

}
