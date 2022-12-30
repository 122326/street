package com.entity;

import java.util.Date;

public class LocalAuth {
	private Long localAuthId;
	private String userName;
	private String password;
	private Date createTime;
	private Date lastEditTime;
	private Long userId;
	private PersonInfo personInfo;
	
	public Long getLocalAuthId() {
		return localAuthId;
	}
	public void setLocalAuthId(Long localAuthId) {
		this.localAuthId = localAuthId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}

	@Override
	public String toString() {
		return "LocalAuth{" +
				"localAuthId=" + localAuthId +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", createTime=" + createTime +
				", lastEditTime=" + lastEditTime +
				", userId=" + userId +
				", personInfo=" + personInfo +
				'}';
	}
}
