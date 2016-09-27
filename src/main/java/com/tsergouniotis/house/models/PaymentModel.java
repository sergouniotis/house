package com.tsergouniotis.house.models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.entities.PFile;
import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.repositories.PaymentRepository;
import com.tsergouniotis.house.utils.CollectionUtils;

@ViewScoped
@Named("paymentModel")
// @ManagedBean(name = "paymentModel")
public class PaymentModel extends LazyDataModel<Payment> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5209787022951181350L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentModel.class);

	@Inject
	private PaymentRepository paymentRepository;

	private Payment selectedPayment;

	public List<Payment> getPayments() {
		return paymentRepository.findAll();
	}

	public void selectPayment(Payment payment) {
		this.selectedPayment = payment;
	}

	public void setSelectedPayment(Payment selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	public Payment getSelectedPayment() {
		if (Objects.isNull(selectedPayment)) {
			this.selectedPayment = new Payment();
		}
		return selectedPayment;
	}

	public BigDecimal getTotal() {
		return paymentRepository.findSum();
	}

	public BigDecimal getTotalPerCreditor(Creditor creditor) {
		return paymentRepository.findSumPerCreditor(creditor);
	}

	public void save() {
		try {
			paymentRepository.saveOrUpdate(selectedPayment);
			LOGGER.info("Payment " + selectedPayment.getCode() + " saved.");
		} catch (Exception e) {
			String message = Objects.isNull(e.getCause()) ? e.getMessage() : e.getCause().getMessage();
			FacesUtils.error(message);
		} finally {
			selectedPayment = null;
		}
	}

	public void delete() {
		try {
			paymentRepository.delete(this.selectedPayment);
			LOGGER.info("Payment " + selectedPayment.getCode() + " deleted.");
		} catch (Exception e) {
			String message = Objects.isNull(e.getCause()) ? e.getMessage() : e.getCause().getMessage();
			FacesUtils.error(message);
		}
	}

	public void creditorChange(ActionEvent e) {
		this.selectedPayment.setCreditor(null);
	}

	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		PFile file = new PFile(uploadedFile.getFileName(), uploadedFile.getContents(), uploadedFile.getContentType());
		getSelectedPayment().setFile(file);
		FacesMessage message = new FacesMessage("Succesful", uploadedFile.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public StreamedContent getSelectedPaymentContent(Payment payment) {

		PFile file = payment.getFile();

		byte[] data = nullSafeGetContent(file);

		if (null != data) {
			try (InputStream io = new ByteArrayInputStream(data)) {
				return new DefaultStreamedContent(io, file.getContentType(), file.getFileName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private byte[] nullSafeGetContent(PFile file) {
		byte[] data = null;
		if (null != file) {
			data = file.getFileContent();
		}
		return data;
	}

	@Override
	public List<Payment> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		Map<String, Boolean> sortMap = toSortMap(multiSortMeta);

		List<Payment> results = paymentRepository.find(first, pageSize, sortMap, filters);

		setRowCount(paymentRepository.count(filters).intValue());

		return results;
	}

	@Override
	public List<Payment> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		Map<String, Boolean> sortMap = toSortMap(sortField, sortOrder);

		List<Payment> results = paymentRepository.find(first, pageSize, sortMap, filters);

		setRowCount(paymentRepository.count(filters).intValue());

		return results;
	}

	@Override
	public Object getRowKey(Payment object) {
		return object.getId();
	}

	private Map<String, Boolean> toSortMap(List<SortMeta> multiSortMeta) {
		Map<String, Boolean> sortMap = new HashMap<>();

		if (CollectionUtils.isNotEmpty(multiSortMeta)) {
			for (SortMeta sortMeta : multiSortMeta) {
				String property = sortMeta.getSortField();

				Boolean value = sortMap.get(property);
				if (null != value) {
					sortMap.put(property, value);
				}
			}
		}
		return sortMap;
	}

	private Map<String, Boolean> toSortMap(String sortField, SortOrder sortOrder) {
		Map<String, Boolean> sortMap = new HashMap<>();
		if (null != sortOrder) {
			boolean ascending = SortOrder.ASCENDING.equals(sortOrder);
			sortMap.put(sortField, ascending);
		}
		return sortMap;
	}

}
