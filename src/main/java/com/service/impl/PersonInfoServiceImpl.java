package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.PersonInfoDao;
import com.dto.ImageHolder;
import com.util.*;
import com.entity.PersonInfo;
import com.service.PersonInfoService;
import com.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PersonInfoServiceImpl extends ServiceImpl<PersonInfoDao, PersonInfo> implements PersonInfoService {
	@Autowired
	private PersonInfoDao personInfoDao;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public PersonInfo getPersonInfoByUserId(long userId) {
		return personInfoDao.queryPersonInfoByUserId(userId);
	}

	@Override
	public PersonInfo getPersonInfoByPhone(String phone){return  personInfoDao.queryPersonInfoByPhone(phone);}

	@Override
	public PersonInfo getPersonInfoByUserName(String userName){return personInfoDao.queryPersonInfoByUserName(userName);}


	@Override
	@Transactional
	public PersonInfo modifyPersonInfo(PersonInfo personInfo, ImageHolder thumbnail) {
		personInfo.setLastEditTime(new Date());
		if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null
				&& !"".equals(thumbnail.getImageName())) {
			PersonInfo tempPersonInfo = personInfoDao.queryPersonInfoByUserId(personInfo.getUserId());
			if (tempPersonInfo.getProfileImg() != null) {
				ImageUtil.deleteFileOrPath(tempPersonInfo.getProfileImg());
			}
			addProfileImg(personInfo, thumbnail);
		}
		// 修改个人信息
		personInfoDao.updatePersonInfo(personInfo);
		return personInfo;
	}

	private void addProfileImg(PersonInfo personInfo, ImageHolder thumbnail) {
		// 获取图片目录的相对值路径
		String dest = PathUtil.getuserProfileImgPath(personInfo.getUserId());

		dest=dest.replace("\\","/");
		String imgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		personInfo.setProfileImg(imgAddr);
	}

	@Override
	public List<PersonInfo> getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize, String sort, String order) {
		// 页转行
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 获取用户信息列表
		List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(personInfoCondition, rowIndex, pageSize,sort,order);
		return personInfoList;
	}

	@Override
	@Transactional
	public PersonInfo addPersonInfo(PersonInfo personInfo, ImageHolder thumbnail)
	{
		personInfo.setCreateTime(new Date());
		if(personInfo.getSouCoin()==null)
			personInfo.setSouCoin(0l);
		personInfoDao.insertPersonInfo(personInfo);
		if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null
				&& !"".equals(thumbnail.getImageName())) {
			addProfileImg(personInfo, thumbnail);
		}
		// 更新头像的图片地址
		personInfoDao.updatePersonInfo(personInfo);
		return personInfo;
	}

	@Override
	public PersonInfo uploadImg(long userId, ImageHolder profileImg){
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(userId);
		PersonInfo temPerson = personInfoDao.queryPersonInfoByUserId(userId);
		if (profileImg != null && profileImg.getImage() != null && profileImg.getImageName() != null
				&& !"".equals(profileImg.getImageName())) {
			if (temPerson.getProfileImg() != null) {
				ImageUtil.deleteFileOrPath(temPerson.getProfileImg());
			}
			addProfileImg(personInfo, profileImg);
		}
		personInfo.setLastEditTime(new Date());

		personInfoDao.updatePersonInfo(personInfo);
		return personInfo;
	}

	@Override
	public Boolean isRealNameAuthenticated(Long userId) {
		PersonInfo personInfo = personInfoDao.selectOne(new QueryWrapper<PersonInfo>().eq("user_id", userId));
		return personInfo != null && personInfo.getRealName() != null && personInfo.getIdentificationNumber() != null;
	}

	@Override
	public Result login(LoginVo loginVo) {
		log.info("登陆");
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginVo.getPhone());
		if (null == userDetails || !passwordEncoder.matches(loginVo.getPassword(), userDetails.getPassword())) {
			log.info("用户名或密码错误！！");
			return Result.fail("用户名或密码错误！");
		}
		if (!userDetails.isEnabled()) {
			log.info("该帐号已被禁用，请联系管理员！");
			return Result.fail("该帐号已被禁用，请联系管理员！");
		}
		log.info("token");

		//更新security登录用户对象
		//UsernamePasswordAuthenticationToke：账户密码认证，是Authentication接口常用的实现类
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		String token = jwtTokenUtil.generateToken(userDetails);
		Map<String, String> map = new HashMap<>(16);
		map.put("tokenHead", tokenHead);
		log.info(tokenHead);
		map.put("token", token);
		log.info(token);
		log.info("111");
		return Result.success("登陆成功", map);

	}
}
