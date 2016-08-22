package com.tsergouniotis.house.graph;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.tsergouniotis.house.entities.CreditorType;
import com.tsergouniotis.house.entities.Payment;

public class CostByTypeGraphBuilder {

	private LineChartModel chart;

	public CostByTypeGraphBuilder() {
		this.chart = new LineChartModel();

		chart.setAnimate(true);
		chart.setTitle("Expenditure per type");
		chart.setZoom(true);
		chart.getAxis(AxisType.Y).setLabel("Values");
		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		axis.setMax(GraphUtils.TARGET_DATE_STR);
		axis.setTickFormat("%b %#d, %y");

		chart.getAxes().put(AxisType.X, axis);
	}

	public LineChartModel build(List<Payment> payments) {

		if (null != payments && payments.size() > 0) {

			LineChartSeries material = new LineChartSeries(CreditorType.MATERIAL.name());
			material.setLabel(CreditorType.MATERIAL.name());

			Map<String, BigDecimal> map = GraphUtils.map(payments.stream()
					.filter(e -> CreditorType.MATERIAL.equals(e.getCreditor().getType())).collect(Collectors.toList()));
			BigDecimal total = new BigDecimal(0);
			for (Entry<String, BigDecimal> e : map.entrySet()) {
				total = total.add(e.getValue());
				material.set(e.getKey(), total);
			}
			chart.addSeries(material);

			LineChartSeries worker = new LineChartSeries(CreditorType.WORKER.name());
			worker.setLabel(CreditorType.WORKER.name());

			map = GraphUtils.map(payments.stream().filter(e -> CreditorType.WORKER.equals(e.getCreditor().getType()))
					.collect(Collectors.toList()));
			total = new BigDecimal(0);
			for (Entry<String, BigDecimal> e : map.entrySet()) {
				total = total.add(e.getValue());
				worker.set(e.getKey(), total);
			}

			chart.addSeries(worker);
		}

		return chart;

	}

}
