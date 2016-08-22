package com.tsergouniotis.house.models;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.LineChartModel;

import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.graph.CostByCreditorGraphBuilder;
import com.tsergouniotis.house.graph.CostByTypeGraphBuilder;
import com.tsergouniotis.house.graph.TotalCostGraphBuilder;
import com.tsergouniotis.house.repositories.PaymentRepository;

@ViewScoped
@Named("graphModel")
public class GraphModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8742878181176779566L;

	private LineChartModel byTypeChart;

	private LineChartModel totalChart;

	private BarChartModel byCreditorChart;

	@Inject
	private PaymentRepository paymentRepository;

	public LineChartModel getByTypeChart() {
		return byTypeChart;
	}

	public LineChartModel getTotalChart() {
		return totalChart;
	}

	public BarChartModel getByCreditorChart() {
		return byCreditorChart;
	}

	@PostConstruct
	@Transactional // we need transactional in order to get creditors ( which
					// are lazily loaded ) from the database
	public void init() {

		List<Payment> payments = paymentRepository.findAll();

		this.byTypeChart = new CostByTypeGraphBuilder().build(payments);

		this.totalChart = new TotalCostGraphBuilder().build(payments);

		this.byCreditorChart = new CostByCreditorGraphBuilder().build(payments);
	}

}
