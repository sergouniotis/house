package com.tsergouniotis.house.models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.entities.PFile;
import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.repositories.CreditorRepository;
import com.tsergouniotis.house.repositories.PaymentRepository;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ViewScoped
@Named("paymentModel")
// @ManagedBean(name = "paymentModel")
public class PaymentModel implements Serializable {

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

	public Double getTotal() {
		return paymentRepository.findSum();
	}

	public Double getTotalPerCreditor(Creditor creditor) {
		return paymentRepository.findSumPerCreditor(creditor);
	}

	public void save() {
		paymentRepository.update(selectedPayment);
		selectedPayment = null;

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

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
