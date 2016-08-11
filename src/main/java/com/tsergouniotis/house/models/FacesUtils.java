package com.tsergouniotis.house.models;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public final class FacesUtils {

	private FacesUtils() {

	}

	public static FacesMessage newFacesMessage(Severity severityInfo, String summary, String message) {
		return new FacesMessage(severityInfo, summary, message);
	}

	public static void info(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				newFacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
	}

	public static void warn(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				newFacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", message));
	}

	public static void error(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				newFacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", message));
	}

	public static void fatal(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				newFacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", message));
	}

}
