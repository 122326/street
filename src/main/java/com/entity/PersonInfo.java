package com.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.util.Date;


@TableName("tb_person_info")
public class PersonInfo {
	private Long userId;
	private String userName;
	private String password;
	private String profileImg;
	//0未知，1男性，2女性
	private String sex;
	@JSONField(format = "yyyy-MM-dd")
	private Date birth;
	private String phone;
	private Long souCoin;
	private String realName;
	private String identificationNumber;
	//0普通用户，1管理员
	private Integer userType;
	private String email;
	private Date createTime;
	private Date lastEditTime;
	//0禁止使用搜街，1允许使用搜街
	private Integer enableStatus;
	@Version
	private Integer version;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getSouCoin() {
		return souCoin;
	}

	public void setSouCoin(Long souCoin) {
		this.souCoin = souCoin;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "PersonInfo{" +
				"userId=" + userId +
				", userName='" + userName + '\'' +
				", profileImg='" + profileImg + '\'' +
				", sex='" + sex + '\'' +
				", birth=" + birth +
				", phone='" + phone + '\'' +
				", souCoin=" + souCoin +
				", realName='" + realName + '\'' +
				", identificationNumber='" + identificationNumber + '\'' +
				", userType=" + userType +
				", email='" + email + '\'' +
				", createTime=" + createTime +
				", lastEditTime=" + lastEditTime +
				", enableStatus=" + enableStatus +
				", version=" + version +
				'}';
	}

}
