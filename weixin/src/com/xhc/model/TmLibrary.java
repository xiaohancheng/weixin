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
import org.hibernate.validator.constraints.NotEmpty;

/**
 * TmLibrary entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tmlibrary", catalog = "weixin_kf")
public class TmLibrary implements java.io.Serializable {

	private static final long serialVersionUID = 241541368880208114L;
	private Integer tlId;
	private Employee employee;
	@NotEmpty(message = "标题不能为空")
	private String tlTitle;
	@NotEmpty(message = "内容不能为空")
	private String tlContent;
	private Integer tlStatus;

	// Constructors

	/** default constructor */
	public TmLibrary() {
	}

	/** full constructor */
	public TmLibrary(Employee employee, String tlTitle, String tlContent,
			Integer tlStatus) {
		this.employee = employee;
		this.tlTitle = tlTitle;
		this.tlContent = tlContent;
		this.tlStatus = tlStatus;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "tl_id", unique = true, nullable = false)
	public Integer getTlId() {
		return this.tlId;
	}

	public void setTlId(Integer tlId) {
		this.tlId = tlId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tl_creater", nullable = false)
	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Column(name = "tl_title", nullable = false)
	public String getTlTitle() {
		return this.tlTitle;
	}

	public void setTlTitle(String tlTitle) {
		this.tlTitle = tlTitle;
	}

	@Column(name = "tl_content", nullable = false)
	public String getTlContent() {
		return this.tlContent;
	}

	public void setTlContent(String tlContent) {
		this.tlContent = tlContent;
	}

	@Column(name = "tl_status", nullable = false)
	public Integer getTlStatus() {
		return this.tlStatus;
	}

	public void setTlStatus(Integer tlStatus) {
		this.tlStatus = tlStatus;
	}
	/*
	 * @Override public String toString(){ return
	 * "{\"tlId\":"+tlId+",\"emNo\":"+
	 * employee.getEmNo()+",\"tlTitle\":"+tlTitle+
	 * ",\"tlContent\":"+tlContent+",\"tlStatus\":"+tlStatus+"}"; }
	 */
}