package com.xhc.model;

// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * TextMessage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "textmessage", catalog = "weixin_kf")
@PrimaryKeyJoinColumn(name = "tm_id")
public class TextMessage extends BaseMessage implements java.io.Serializable {

	private static final long serialVersionUID = -4090355499897335482L;
	private String Content;

	// Constructors

	/** default constructor */
	public TextMessage() {
	}

	/** full constructor */
	public TextMessage(String content) {
		this.Content = content;
	}

	@Column(name = "Content", nullable = false)
	public String getContent() {
		return this.Content;
	}

	public void setContent(String content) {
		this.Content = content;
	}

}