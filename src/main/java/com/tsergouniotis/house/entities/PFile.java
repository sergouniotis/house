package com.tsergouniotis.house.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_content")
	private byte[] fileContent;

	@Column(name = "file_content_type")
	private String contentType;

	/**
	 * JPA Constructor
	 */
	public PFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PFile(String fileName, byte[] fileContent, String contentType) {
		super();
		this.fileName = fileName;
		this.fileContent = fileContent;
		this.contentType = contentType;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
