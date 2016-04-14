package com.xhc.model;

// default package

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Employee entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "employee", catalog = "weixin_kf")
public class Employee implements java.io.Serializable {

	private static final long serialVersionUID = -5774719672114077488L;
	private String emId;
	@NotEmpty(message = "用户名不能为空")
	private String emName;
	@NotEmpty(message = "编号不能为空")
	private String emNo;
	private String emPassword;
	private String emSex;
	@Email(message = "邮箱格式不对")
	@Size(min = 1, max = 50, message = "邮箱长度1-50")
	private String emEmail;
	@Size(min = 11, max = 11, message = "请输入11位的手机号码")
	private String emPhone;
	private Integer emStatus;
	private String emIdentity;
	private transient Set<TmLibrary> tmLibraries = new HashSet<TmLibrary>(0);
	private transient Set<VoiLibrary> voiLibraries = new HashSet<VoiLibrary>(0);
	private transient Set<PicLibrary> picLibraries = new HashSet<PicLibrary>(0);

	// Constructors

	/** default constructor */
	public Employee() {
	}

	/** minimal constructor */
	public Employee(String emId, String emName, String emNo, String emPassword,
			String emSex, Integer emStatus) {
		this.emId = emId;
		this.emName = emName;
		this.emNo = emNo;
		this.emPassword = emPassword;
		this.emSex = emSex;
		this.emStatus = emStatus;
	}

	/** full constructor */
	public Employee(String emId, String emName, String emNo, String emPassword,
			String emSex, String emEmail, String emPhone, Integer emStatus,
			Set<TmLibrary> tmLibraries, Set<VoiLibrary> voiLibraries,
			Set<PicLibrary> picLibraries) {
		this.emId = emId;
		this.emName = emName;
		this.emNo = emNo;
		this.emPassword = emPassword;
		this.emSex = emSex;
		this.emEmail = emEmail;
		this.emPhone = emPhone;
		this.emStatus = emStatus;
		this.tmLibraries = tmLibraries;
		this.voiLibraries = voiLibraries;
		this.picLibraries = picLibraries;
	}

	// Property accessors
	@Id
	@Column(name = "em_id", unique = true, nullable = false, length = 36)
	public String getEmId() {
		return this.emId;
	}

	public void setEmId(String emId) {
		this.emId = emId;
	}

	@Column(name = "em_name", nullable = false, length = 20)
	public String getEmName() {
		return this.emName;
	}

	public void setEmName(String emName) {
		this.emName = emName;
	}

	@Column(name = "em_no", nullable = false, length = 20)
	public String getEmNo() {
		return this.emNo;
	}

	public void setEmNo(String emNo) {
		this.emNo = emNo;
	}

	@Column(name = "em_password", nullable = false, length = 35)
	public String getEmPassword() {
		return this.emPassword;
	}

	public void setEmPassword(String emPassword) {
		this.emPassword = emPassword;
	}

	@Column(name = "em_sex", nullable = false, length = 1)
	public String getEmSex() {
		return this.emSex;
	}

	public void setEmSex(String emSex) {
		this.emSex = emSex;
	}

	@Column(name = "em_email", length = 20)
	public String getEmEmail() {
		return this.emEmail;
	}

	public void setEmEmail(String emEmail) {
		this.emEmail = emEmail;
	}

	@Column(name = "em_phone", length = 11)
	public String getEmPhone() {
		return this.emPhone;
	}

	public void setEmPhone(String emPhone) {
		this.emPhone = emPhone;
	}

	@Column(name = "em_status", nullable = false)
	public Integer getEmStatus() {
		return this.emStatus;
	}

	public void setEmStatus(Integer emStatus) {
		this.emStatus = emStatus;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "employee")
	public Set<TmLibrary> getTmLibraries() {
		return this.tmLibraries;
	}

	public void setTmLibraries(Set<TmLibrary> tmLibraries) {
		this.tmLibraries = tmLibraries;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "employee")
	public Set<VoiLibrary> getVoiLibraries() {
		return this.voiLibraries;
	}

	public void setVoiLibraries(Set<VoiLibrary> voiLibraries) {
		this.voiLibraries = voiLibraries;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "employee")
	public Set<PicLibrary> getPicLibraries() {
		return this.picLibraries;
	}

	public void setPicLibraries(Set<PicLibrary> picLibraries) {
		this.picLibraries = picLibraries;
	}

	@Column(name = "em_identity", length = 10)
	public String getEmIdentity() {
		return emIdentity;
	}

	public void setEmIdentity(String emIdentity) {
		this.emIdentity = emIdentity;
	}

}