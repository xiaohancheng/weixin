package com.xhc.model;

// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * PicLibrary entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "piclibrary", catalog = "weixin_kf")
public class PicLibrary implements java.io.Serializable {

	private static final long serialVersionUID = 7677685425673609113L;
	private Integer plId;
	private Employee employee;
	private String plTitle;
	private String plUrl;
	private Integer plStatus;

	// Constructors

	/** default constructor */
	public PicLibrary() {
	}

	/** full constructor */
	public PicLibrary(Employee employee, String plTitle, String plUrl,
			Integer plStatus) {
		this.employee = employee;
		this.plTitle = plTitle;
		this.plUrl = plUrl;
		this.plStatus = plStatus;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "pl_id", unique = true, nullable = false)
	public Integer getPlId() {
		return this.plId;
	}

	public void setPlId(Integer plId) {
		this.plId = plId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pl_creater", nullable = false)
	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name = "pl_title", nullable = false)
	public String getPlTitle() {
		return this.plTitle;
	}

	public void setPlTitle(String plTitle) {
		this.plTitle = plTitle;
	}

	@Column(name = "pl_url", nullable = false)
	public String getPlUrl() {
		return this.plUrl;
	}

	public void setPlUrl(String plUrl) {
		this.plUrl = plUrl;
	}

	@Column(name = "pl_status", nullable = false)
	public Integer getPlStatus() {
		return this.plStatus;
	}

	public void setPlStatus(Integer plStatus) {
		this.plStatus = plStatus;
	}

}