package com.entity;

//求助视图实体
import java.util.Date;

public class AppealImg {
	private Long appealImgId;//求助图片代号
	private String imgAddr;//图片地址
	private Long appealId;//求助代号
	private Date createTime;//创建时间

	public Long getAppealImgId() {
		return appealImgId;
	}

	public void setAppealImgId(Long appealImgId) {
		this.appealImgId = appealImgId;
	}

	public String getImgAddr() {
		return imgAddr;
	}

	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}

	public Long getAppealId() {
		return appealId;
	}

	public void setAppealId(Long appealId) {
		this.appealId = appealId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
