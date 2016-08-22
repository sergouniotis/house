package com.tsergouniotis.house.graph;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.entities.Payment;

public class CostByCreditorGraphBuilder {

	private BarChartModel model;

	public CostByCreditorGraphBuilder() {
		this.model = new BarChartModel();

	}

	public BarChartModel build(List<Payment> payments) {

		Map<String, BigDecimal> creditorAmount = new TreeMap<>();
		if (null != payments && payments.size() > 0) {
			for (Payment payment : payments) {

				Creditor creditor = payment.getCreditor();
				String key = creditor.getName();
				if (!creditorAmount.containsKey(key)) {
					creditorAmount.put(key, payment.getAmount());
				} else {
					BigDecimal amount = creditorAmount.get(key);

					amount = amount.add(payment.getAmount());

					creditorAmount.put(key, amount);
				}

			}
		}

		Set<Entry<String, BigDecimal>> entrySet = creditorAmount.entrySet();
		BarChartSeries cs = new BarChartSeries();
		for (Entry<String, BigDecimal> entry : entrySet) {
			cs.set(entry.getKey(), entry.getValue());
		}
		model.addSeries(cs);

		return model;
	}

}
