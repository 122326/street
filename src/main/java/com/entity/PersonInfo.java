package com.entity;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
//@TableName("tb_person_info")
public class PersonInfo implements UserDetails {
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

	/**
	 * 权限数据
	 * @return
	 */

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<>();
		list.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				switch (userType){
					case 1:
						return "ADMIN";
					default:
						return "user";
				}
			}
		});
		return null;
	}

	/**
	 * 用户名
	 * @return
	 */
	@Override
	@JsonIgnore
	public String getUsername() {
		return phone;
	}

	public String getUserName(){
		return userName;
	}



	/**
	 * 账户是否过期
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 账户是否被锁定
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 凭证是否过期
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 是否被禁用
	 * @return
	 */
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}


}
