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
 * ChatGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "chatgroup", catalog = "weixin_kf")
public class ChatGroup implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -6435204469619141432L;
	private Integer cgId;
	private BaseMessage baseMessage;
	private Long cgGroupId;

	// Constructors

	/** default constructor */
	public ChatGroup() {
	}

	/** full constructor */
	public ChatGroup(BaseMessage baseMessage, Long cgGroupId) {
		this.baseMessage = baseMessage;
		this.cgGroupId = cgGroupId;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "cg_id", unique = true, nullable = false)
	public Integer getCgId() {
		return this.cgId;
	}

	public void setCgId(Integer cgId) {
		this.cgId = cgId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cg_msg_id", nullable = false)
	public BaseMessage getBaseMessage() {
		return this.baseMessage;
	}

	public void setBaseMessage(BaseMessage baseMessage) {
		this.baseMessage = baseMessage;
	}

	@Column(name = "cg_group_id", nullable = false)
	public Long getCgGroupId() {
		return this.cgGroupId;
	}

	public void setCgGroupId(Long cgGroupId) {
		this.cgGroupId = cgGroupId;
	}

}