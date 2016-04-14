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
 * VoiLibrary entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "voilibrary", catalog = "weixin_kf")
public class VoiLibrary implements java.io.Serializable {
	private static final long serialVersionUID = -7255039640266763076L;
	private Integer vlId;
	private Employee employee;
	private String vlTitle;
	private String vlContent;
	private Integer vlStatus;

	// Constructors

	/** default constructor */
	public VoiLibrary() {
	}

	/** full constructor */
	public VoiLibrary(Employee employee, String vlTitle, String vlContent,
			Integer vlStatus) {
		this.employee = employee;
		this.vlTitle = vlTitle;
		this.vlContent = vlContent;
		this.vlStatus = vlStatus;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "vl_id", unique = true, nullable = false)
	public Integer getVlId() {
		return this.vlId;
	}

	public void setVlId(Integer vlId) {
		this.vlId = vlId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vl_creater", nullable = false)
	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name = "vl_title", nullable = false)
	public String getVlTitle() {
		return this.vlTitle;
	}

	public void setVlTitle(String vlTitle) {
		this.vlTitle = vlTitle;
	}

	@Column(name = "vl_content", nullable = false)
	public String getVlContent() {
		return this.vlContent;
	}

	public void setVlContent(String vlContent) {
		this.vlContent = vlContent;
	}

	@Column(name = "vl_status", nullable = false)
	public Integer getVlStatus() {
		return this.vlStatus;
	}

	public void setVlStatus(Integer vlStatus) {
		this.vlStatus = vlStatus;
	}

}