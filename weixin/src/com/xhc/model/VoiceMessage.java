package com.xhc.model;

// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * VoiceMessage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "voicemessage", catalog = "weixin_kf")
@PrimaryKeyJoinColumn(name = "vm_id")
public class VoiceMessage extends BaseMessage implements java.io.Serializable {

	private static final long serialVersionUID = -8056310642986024772L;
	private String format;
	private String voiUrl;
	private String mediaId;

	// Constructors

	/** default constructor */
	public VoiceMessage() {
	}

	/** full constructor */
	public VoiceMessage(String format, String voiUrl, String mediaId) {
		this.format = format;
		this.voiUrl = voiUrl;
		this.mediaId = mediaId;
	}

	@Column(name = "Format", nullable = false)
	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "VoiUrl", nullable = false)
	public String getVoiUrl() {
		return this.voiUrl;
	}

	public void setVoiUrl(String voiUrl) {
		this.voiUrl = voiUrl;
	}

	@Column(name = "MediaId", nullable = false)
	public String getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

}