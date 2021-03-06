package com.tsergouniotis.house.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "creditors")
@NamedQueries({ @NamedQuery(name = "Creditor.findByName", query = "SELECT c FROM Creditor c where c.name=:name") })
public class Creditor implements Serializable, Comparable<Creditor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "database_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDITORSEQ")
	@SequenceGenerator(name = "CREDITORSEQ", sequenceName = "creditor_seq", allocationSize = 1)
	private Long id;

	@Column(name = "creditor_name")
	private String name;

	@Column(name = "description")
	private String description;

	@Enumerated
	@Column(name = "creditor_type")
	private CreditorType type;

	/**
	 * JPA Constructor
	 */
	public Creditor() {
		this.type = CreditorType.WORKER;
	}

	public Creditor(String name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CreditorType getType() {
		return type;
	}

	public void setType(CreditorType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Creditor other = (Creditor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(Creditor o) {
		if (Objects.isNull(o)) {
			return 1;
		}

		return getName().compareTo(o.getName());
	}

}
