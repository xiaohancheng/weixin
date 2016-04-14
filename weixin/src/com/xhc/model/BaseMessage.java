package com.xhc.model;

// default package

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * BaseMessage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "basemessage", catalog = "weixin_kf")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseMessage implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 4418582410769037043L;
	private Long MsgId;
	private String FromUserName;
	private String ToUserName;
	private Long CreateTime;
	private String MsgType;
	private transient Set<ChatGroup> chatGroups = new HashSet<ChatGroup>(0);

	// Constructors

	/** default constructor */
	public BaseMessage() {
	}

	/** minimal constructor */
	public BaseMessage(Long MsgId, String FromUserName, String ToUserName,
			Long CreateTime, String MsgType) {
		this.MsgId = MsgId;
		this.FromUserName = FromUserName;
		this.ToUserName = ToUserName;
		this.CreateTime = CreateTime;
		this.MsgType = MsgType;
	}

	/** full constructor */
	public BaseMessage(Long MsgId, String FromUserName, String ToUserName,
			Long CreateTime, String MsgType, Set<ChatGroup> chatGroups) {
		this.MsgId = MsgId;
		this.FromUserName = FromUserName;
		this.ToUserName = ToUserName;
		this.CreateTime = CreateTime;
		this.MsgType = MsgType;
		this.chatGroups = chatGroups;
	}

	// Property accessors
	@Id
	@Column(name = "MsgId", unique = true, nullable = false)
	public Long getMsgId() {
		return this.MsgId;
	}

	public void setMsgId(Long MsgId) {
		this.MsgId = MsgId;
	}

	@Column(name = "FromUserName", nullable = false, length = 30)
	public String getFromUserName() {
		return this.FromUserName;
	}

	public void setFromUserName(String FromUserName) {
		this.FromUserName = FromUserName;
	}

	@Column(name = "ToUserName", nullable = false, length = 30)
	public String getToUserName() {
		return this.ToUserName;
	}

	public void setToUserName(String ToUserName) {
		this.ToUserName = ToUserName;
	}

	@Column(name = "CreateTime", nullable = false)
	public Long getCreateTime() {
		return this.CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	@Column(name = "MsgType", nullable = false, length = 10)
	public String getMsgType() {
		return this.MsgType;
	}

	public void setMsgType(String MsgType) {
		this.MsgType = MsgType;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "baseMessage")
	public Set<ChatGroup> getChatGroups() {
		return this.chatGroups;
	}

	public void setChatGroups(Set<ChatGroup> chatGroups) {
		this.chatGroups = chatGroups;
	}
}