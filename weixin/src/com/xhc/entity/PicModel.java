package com.xhc.entity;

import java.io.Serializable;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class PicModel implements Serializable {

	private static final long serialVersionUID = 4166587229868330248L;
	private String picId;
	@NotEmpty(message = "图片标题不能为空")
	private String picTitle;
	private List<MultipartFile> images;

	public String getPicTitle() {
		return picTitle;
	}

	public void setPicTitle(String picTitle) {
		this.picTitle = picTitle;
	}

	public List<MultipartFile> getImages() {
		return images;
	}

	public void setImages(List<MultipartFile> images) {
		this.images = images;
	}

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}
}
