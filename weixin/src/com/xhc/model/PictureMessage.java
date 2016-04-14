package com.xhc.model;

// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * PictureMessage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "picturemessage", catalog = "weixin_kf")
@PrimaryKeyJoinColumn(name = "pm_id")
public class PictureMessage extends BaseMessage implements java.io.Serializable {

	private static final long serialVersionUID = 8998909295314442722L;
	private String picUrl;
	private String mediaId;

	// Constructors

	/** default constructor */
	public PictureMessage() {
	}

	/** full constructor */
	public PictureMessage(String picUrl, String mediaId) {
		this.picUrl = picUrl;
		this.mediaId = mediaId;
	}

	@Column(name = "PicUrl", nullable = false)
	public String getPicUrl() {
		return this.picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Column(name = "MediaId", nullable = false)
	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

}