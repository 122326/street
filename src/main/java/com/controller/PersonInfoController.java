package com.controller;


import com.dto.ImageHolder;
import com.util.*;
import com.entity.PersonInfo;
import com.service.PersonInfoService;
import com.service.UsersInformService;
import com.vo.LoginVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/PersonInfo")
public class PersonInfoController {
	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private PersonInfoService personInfoService;

	@Autowired
	private UsersInformService usersInformService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RedisUtil redisUtil;


	/**
	 * 根据用户ID查询用户的详细信息
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getPersonInfoByUserId/{userId}",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getPersonInfoByuserId(@PathVariable("userId") Long userId ){
		HashMap<String, Object> modelMap = new HashMap<>();
		log.info("用户Id"+userId);
		if (userId != null&&userId>0){
			try {
				PersonInfo personInfo = personInfoService.getPersonInfoByUserId(userId);
				log.info("查看用户信息时都传给前端了啥："+personInfo.toString());
				modelMap.put("success", true);
				modelMap.put("personInfo", personInfo);
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
	 * 注册
	 * @param loginVo
	 * @return
	 */
	@RequestMapping(value = "/addPersonInfo", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addPersonInfo(@RequestBody(required = false) LoginVo loginVo) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前端请求中获取用户信息
		PersonInfo pi=new PersonInfo();
		pi.setPhone(loginVo.getPhone());
		pi.setPassword(passwordEncoder.encode(loginVo.getPassword()));
		pi.setUserName(loginVo.getPhone());
		pi.setUserType(0);
		pi.setEnableStatus(1);
		//pi.setPassword(MD5Util.getMD5(password));
		// 判断用户是否已存在
		PersonInfo pe=new PersonInfo();
		pe=personInfoService.getPersonInfoByPhone(loginVo.getPhone());
		log.info(loginVo.getPhone());
		String s="用过";
		if(pe==null){s="没用过";}
		System.out.println("注册时看这个Id有没有用过："+s);
		if (pe==null) {
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
	@PostMapping("/login")
	@ResponseBody
	//@ApiOperation(value = "登录接口", notes = "post请求,参数为用户名username, 密码password", httpMethod = "POST")
	public Result login(@RequestBody LoginVo loginVo) {
		return personInfoService.login(loginVo);
	}

//	@GetMapping("/logout")
//	@ApiOperation(value = "退出", httpMethod = "GET")
//	public Result logout() {
//
//		//删除菜单信息
//
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth != null) {
//			SecurityContextHolder.getContext().setAuthentication(null);
//		}
//		return Result.success("退出成功");
//	}

	/**
	 * 修改用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifypersonInfo", method = RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> modifyPersonInfo(HttpServletRequest request) {
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
		
		CommonsMultipartFile profileImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
		}
		System.out.println(profileImg);
		System.out.println("修改信息时传过来的："+pi.toString());
		// 非空判断
		if (userId != null) {
			try {
				PersonInfo ae;
				if (profileImg == null) {
					ae = personInfoService.modifyPersonInfo(pi, null);
				}else {
					ImageHolder imageHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
					ae = personInfoService.modifyPersonInfo(pi, imageHolder);
				}
				modelMap.put("success", true);
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

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

	@ApiOperation(value = "获取当前登录用户的信息")
	@GetMapping("/getLoginInfo")
	public Result getLoginInfo(Principal principal) {
		if (null == principal) {
			return null;
		}
		String username = principal.getName();
		PersonInfo user = personInfoService.getPersonInfoByPhone(username);
		return Result.success("获取当前用户信息",SecurityUtil.getUserInfo());
	}

	@ApiOperation(value = "退出", httpMethod = "GET")
	@GetMapping("/logout")
	public Result logout(HttpServletRequest request, HttpServletResponse response) {
		redisUtil.delKey("userInfo_"+SecurityUtil.getUsername());
		//清除spring security用户认证信息
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			log.info("清除认证信息");
		}
		return Result.success("退出成功");
	}

}
