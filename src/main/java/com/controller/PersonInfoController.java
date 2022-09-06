package com.controller;


import com.util.ImageHolder;
import com.entity.PersonInfo;
import com.service.PersonInfoService;
import com.service.UsersInformService;
import com.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/PersonInfo")
public class PersonInfoController {
	@Autowired
	private PersonInfoService personInfoService;
	private UsersInformService usersInformService;

	// 根据用户ID查询用户的详细信息
	@RequestMapping(value = "/getPersonInfoByUserId/{userId}",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getPersonInfoByuserId(@PathVariable("userId") Long userId ){
		HashMap<String, Object> modelMap = new HashMap<>();
		System.out.println("用户Id"+userId);
		if (userId != null&&userId>0){
			try {
				PersonInfo personInfo = personInfoService.getPersonInfoByUserId(userId);
				System.out.println("查看用户信息时都传给前端了啥："+personInfo.toString());
				modelMap.put("success", true);
				modelMap.put("personInfo",personInfo);
			}catch (Exception e){
				modelMap.put("success", false);
				modelMap.put("error",e.toString());
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("error","请输入正确userId！");
		}
		return modelMap;
	}


	/**
	 * 用户注册
	 */
	@RequestMapping(value = "/addPersonInfo", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addPersonInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前端请求中获取用户信息
		//long userId=HttpServletRequestUtil.getLong(request,"userId");
		String phone=HttpServletRequestUtil.getString(request,"phone");
		String password=HttpServletRequestUtil.getString(request,"password");
		System.out.println("用户账号："+phone+"密码"+password);
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		int userType = HttpServletRequestUtil.getInt(request, "userType");

		PersonInfo pi = new PersonInfo();
		pi.setPhone(phone);
		pi.setUserName(userName);
		pi.setUserType(userType);
		pi.setEnableStatus(enableStatus);
		pi.setPassword(password);
		// 判断用户是否已存在
		PersonInfo pe=new PersonInfo();
		pe=personInfoService.getPersonInfoByPhone(phone);
		String s="用过";
		if(pe==null){s="没用过";}
		System.out.println("注册时看这个Id有没有用过："+s);
		if (pi==null) {
			if(pi.getPassword()!=null) {
				try {
					PersonInfo ae;
					ae = personInfoService.addPersonInfo(pi, null);
					modelMap.put("userId", ae.getUserId());
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					System.out.println("添加时出错:"+e.toString());
				}
			}else{
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入用户密码!");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户已存在!");
		}
		return modelMap;
	}


	/**
	 * 用户登录
	 *
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userLogin(HttpServletRequest request) {
		System.out.println("调用登陆函数");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String phone = HttpServletRequestUtil.getString(request, "phone");
		String password = HttpServletRequestUtil.getString(request, "password");
		PersonInfo personInfo = new PersonInfo();
		try {
			personInfo = personInfoService.getPersonInfoByPhone(phone);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			System.out.println(e.toString());
			return modelMap;
		}
		if (personInfo == null) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户不存在！");
				return modelMap;
		}
		try{
			String ps=personInfoService.getPersonInfoByPhone(phone).getPassword();
			System.out.println("数据库的密码"+ps);
			System.out.println("输入的密码"+password);
			if(!ps.equals(password))
			{
				modelMap.put("success", false);
				modelMap.put("errMsg", "密码错误！");
				return modelMap;
			}
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("userId", personInfoService.getPersonInfoByPhone(phone).getUserId());
		modelMap.put("success", true);
		return modelMap;
	}


	/**
	 * 修改用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifypersonInfo", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyPersonInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前端请求中获取用户信息
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		String phone = HttpServletRequestUtil.getString(request, "phone");
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String email = HttpServletRequestUtil.getString(request, "email");
		String sex = HttpServletRequestUtil.getString(request, "sex");
		Date birth = HttpServletRequestUtil.getDate(request, "birth");
		Long souCoin = HttpServletRequestUtil.getLong(request, "souCoin");
		int userType = HttpServletRequestUtil.getInt(request, "userType");
		System.out.println("收到的生日："+birth);

		PersonInfo pi = new PersonInfo();
		pi.setUserId(userId);
		pi.setUserName(userName);
		pi.setEmail(email);
		pi.setSex(sex);
		pi.setBirth(birth);
		pi.setPhone(phone);
		pi.setSouCoin(souCoin);
		pi.setUserType(userType);
		pi.setEnableStatus(enableStatus);
		
		/*CommonsMultipartFile profileImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
		}*/
		System.out.println("修改信息时传过来的："+pi.toString());
		// 非空判断
		if (userId != null) {
			try {
				PersonInfo ae;
				//if (profileImg == null) {
					ae = personInfoService.modifyPersonInfo(pi, null);
				/*} else {
					ImageHolder imageHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
					ae = personInfoService.modifyPersonInfo(pi, imageHolder);
				}*/
				modelMap.put("success", true);
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			} /*catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}*/

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入需要修改的帐号信息");
		}
		return modelMap;
	}

	private void handleImage(HttpServletRequest request, ImageHolder profileImg) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 取出profileImg并构建ImageHolder对象
		CommonsMultipartFile profileImgFile = (CommonsMultipartFile) multipartRequest.getFile("profileImg");
		if (profileImgFile != null && profileImg != null) {
			profileImg.setImage(profileImgFile.getInputStream());
			profileImg.setImageName(profileImgFile.getOriginalFilename());
		}
	}


	@RequestMapping(value = "/uploadprofileimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> uploadprofileImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = request.getHeader("token");
		ImageHolder profileImg = new ImageHolder("", null);
		Long userId = HttpServletRequestUtil.getLong(request,"userId");
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if (commonsMultipartResolver.isMultipart(request)) {
				handleImage(request, profileImg);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			System.out.println(e.toString());
			return modelMap;
		}
		// 2.上传个人信息图片
		PersonInfo pe;
		try {
			pe = personInfoService.uploadImg(userId,profileImg);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}


	@ResponseBody
	@RequestMapping("/loginin") //二级接口
	public Map login() {
		System.out.println("登陆");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("success", true);
		return  modelMap;
	}
}
