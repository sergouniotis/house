package com.tsergouniotis.house.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.tsergouniotis.house.entities.CreditorType;
import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.repositories.PaymentRepository;

@ViewScoped
@Named("graphModel")
public class GraphModel implements Serializable {

	private static final String TARGET_DATE_STR = "2018-01-01";

	private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * 
	 */
	private static final long serialVersionUID = -8742878181176779566L;

	private LineChartModel dateModel;

	private LineChartModel lineModel1;

	private List<Payment> payments;

	@Inject
	private PaymentRepository paymentRepository;

	public LineChartModel getDateModel() {
		return dateModel;
	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	@PostConstruct
	public void init() {

		this.payments = paymentRepository.findAll();

		createDateModel();

		createLinearModel();
	}

	@Transactional
	private void createLinearModel() {

		if (null != payments && payments.size() > 0) {
			this.lineModel1 = new LineChartModel();

			LineChartSeries series1 = new LineChartSeries("Tempo");

			Map<String, BigDecimal> map = map(payments);

			for (Entry<String, BigDecimal> entry : map.entrySet()) {
				series1.set(entry.getKey(), entry.getValue());
			}

			lineModel1.addSeries(series1);

			LineChartSeries series2 = new LineChartSeries("Total");
			BigDecimal total = new BigDecimal(0);
			for (Entry<String, BigDecimal> entry : map.entrySet()) {
				total = total.add(entry.getValue());
				series2.set(entry.getKey(), total);
			}

			lineModel1.addSeries(series2);

			lineModel1.setTitle("Total Expenditures");
			lineModel1.setLegendPosition("e");

			Axis yAxis = lineModel1.getAxis(AxisType.Y);
			yAxis.setMin(0);
			yAxis.setMax(200_000);

			DateAxis axis = new DateAxis("Dates");
			axis.setTickAngle(-50);
			axis.setMax(TARGET_DATE_STR);
			axis.setTickFormat("%b %#d, %y");

			lineModel1.getAxes().put(AxisType.X, axis);

			// lineModel2.setTitle("Category Chart");
			// lineModel2.setLegendPosition("e");
			// lineModel2.setShowPointLabels(true);
			// lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Years"));
			// yAxis = lineModel2.getAxis(AxisType.Y);
			// yAxis.setLabel("Births");
			// yAxis.setMin(0);
			// yAxis.setMax(200);

		}
	}

	private static Map<String, BigDecimal> map(List<Payment> payments) {
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

	@Transactional
	private void createDateModel() {
		dateModel = new LineChartModel();

		if (null != payments && payments.size() > 0) {

			LineChartSeries material = new LineChartSeries(CreditorType.MATERIAL.name());

			LineChartSeries worker = new LineChartSeries(CreditorType.WORKER.name());

			Map<String, BigDecimal> map = map(payments.stream()
					.filter(e -> CreditorType.MATERIAL.equals(e.getCreditor().getType())).collect(Collectors.toList()));
			BigDecimal total = new BigDecimal(0);
			for (Entry<String, BigDecimal> e : map.entrySet()) {
				total = total.add(e.getValue());
				material.set(e.getKey(), total);
			}

			map = map(payments.stream().filter(e -> CreditorType.WORKER.equals(e.getCreditor().getType()))
					.collect(Collectors.toList()));
			total = new BigDecimal(0);
			for (Entry<String, BigDecimal> e : map.entrySet()) {
				total = total.add(e.getValue());
				worker.set(e.getKey(), total);
			}

			dateModel.addSeries(material);
			dateModel.addSeries(worker);

			dateModel.setTitle("Expenditure per date : Zoom for Details");
			dateModel.setZoom(true);
			dateModel.getAxis(AxisType.Y).setLabel("Values");
			DateAxis axis = new DateAxis("Dates");
			axis.setTickAngle(-50);
			axis.setMax(TARGET_DATE_STR);
			axis.setTickFormat("%b %#d, %y");

			dateModel.getAxes().put(AxisType.X, axis);
		}

	}

}
