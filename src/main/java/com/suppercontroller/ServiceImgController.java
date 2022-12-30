package com.suppercontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.dto.ConstantForSuperAdmin;
import com.dto.ImageHolder;
import com.dto.ServiceExecution;
import com.entity.ServiceImg;
import com.entity.ServiceInfo;
import com.enums.ServiceStateEnum;
import com.service.SService;
import com.util.HttpServletRequestUtil;

import static com.util.NameUtil.HumpToUnderline;
import static com.util.SQLUtil.sqlValidate;

@Controller
@RequestMapping("/superadmin")
public class ServiceImgController {
	@Autowired
	private SService sService;

	@RequestMapping(value = "/listserviceimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> listServiceImgs(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ServiceExecution aie = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_NO);
		int pageSize = HttpServletRequestUtil.getInt(request, ConstantForSuperAdmin.PAGE_SIZE);
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			try {
				ServiceImg serviceImg = new ServiceImg();




				String sort = HttpServletRequestUtil.getString(request, "sort");
				if(sort!=null) sort = HumpToUnderline(sort);
				String order = HttpServletRequestUtil.getString(request, "order");
				//检测sort和order是否合法
				if(sort!=null){
					if (sqlValidate(sort)) {
						modelMap.put("Warning:","您的行为已被记录，请勿再次操作，否则报警处理!");
						return modelMap;
					}}
				if(order!=null){
					if (sqlValidate(order)) {
						modelMap.put("Warning:","您的行为已被记录，请勿再次操作，否则报警处理!");
						return modelMap;
					}}
				aie = sService.getServiceImgList(serviceImg, pageIndex, pageSize,sort,order);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (aie.getServiceImgList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, aie.getServiceImgList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, aie.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ServiceImg>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	@RequestMapping(value = "/searchserviceimgsbyserviceid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchServiceImgsbyServiceId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ServiceExecution aie = null;
		// 获取serviceID
		Long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
		// 空值判断
		if (serviceId != null) {
			try {
				aie = sService.getServiceImg(serviceId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (aie.getServiceImgList() != null) {
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, aie.getServiceImgList());
				modelMap.put(ConstantForSuperAdmin.TOTAL, aie.getCount());
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, new ArrayList<ServiceImg>());
				modelMap.put(ConstantForSuperAdmin.TOTAL, 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}

	@RequestMapping(value = "/deleteserviceimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> deleteServiceImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
		try {
			ServiceImg serviceImg=new ServiceImg();
			serviceImg.setServiceId(serviceId);
			ServiceExecution serviceImgExecution = sService.deleteServiceImg(serviceImg);
			//先删除服务图片，在清空服务
			ServiceInfo service=sService.getByServiceId(serviceId);
			service.setServiceImgAddr("");
			sService.modifyService(service);
			if (serviceImgExecution.getState() != ServiceStateEnum.SUCCESS.getState()) {
				modelMap.put("success", false);
				modelMap.put("errMsg", serviceImgExecution.getStateInfo());
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}

	@RequestMapping(value = "/addserviceimg", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addServiceImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取服务ID
		Long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");

		CommonsMultipartFile serviceImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			serviceImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("imgAddr");
		}
		if (serviceImg == null) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "缺少服务图片");
			return modelMap;
		}
		try {
			ImageHolder serviceImgHolder = new ImageHolder(serviceImg.getOriginalFilename(), serviceImg.getInputStream());
			ServiceExecution serviceImgExecution = sService.createServiceImg(serviceId, serviceImgHolder);
			if (serviceImgExecution.getState() != ServiceStateEnum.SUCCESS.getState()) {
				modelMap.put("success", false);
				modelMap.put("errMsg", serviceImgExecution.getStateInfo());
				return modelMap;
			}
			
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
