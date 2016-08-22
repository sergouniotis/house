package com.tsergouniotis.house.graph;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.transaction.Transactional;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.tsergouniotis.house.entities.Payment;

public class TotalCostGraphBuilder {

	private LineChartModel chart;

	public TotalCostGraphBuilder() {

		this.chart = new LineChartModel();

		chart.setAnimate(true);
		chart.setTitle("Total Expenditures");
		chart.setLegendPosition("e");

		Axis yAxis = chart.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(200_000);

		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		axis.setMax(GraphUtils.TARGET_DATE_STR);
		axis.setTickFormat("%b %#d, %y");

		chart.getAxes().put(AxisType.X, axis);
	}

	@Transactional
	public LineChartModel build(List<Payment> payments) {

		if (!Objects.isNull(payments)) {
			LineChartSeries tempoSeries = new LineChartSeries("Tempo");

			Map<String, BigDecimal> map = GraphUtils.map(payments);

			for (Entry<String, BigDecimal> entry : map.entrySet()) {
				tempoSeries.set(entry.getKey(), entry.getValue());
			}

			chart.addSeries(tempoSeries);

			LineChartSeries totalSeries = new LineChartSeries("Total");
			BigDecimal total = new BigDecimal(0);
			for (Entry<String, BigDecimal> entry : map.entrySet()) {
				total = total.add(entry.getValue());
				totalSeries.set(entry.getKey(), total);
			}

			chart.addSeries(totalSeries);
		}

		return chart;

	}

}
