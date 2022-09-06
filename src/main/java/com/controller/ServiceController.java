package com.controller;

import com.dao.ServiceImgDao;
import com.util.ImageHolder;
import com.entity.ServiceImg;
import com.entity.ServiceInfo;
import com.entity.Shop;
import com.service.SService;
import com.service.ShopService;
import com.util.HttpServletRequestUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping("/service")
@Api(value = "ServiceController|对服务操作的控制器")
public class ServiceController {
	@Autowired
	private SService sService;
	@Autowired
	private ServiceImgDao serviceImgDao;
	@Autowired
	private ShopService shopService;

	//通过店铺id获取服务列表 分页 
	@RequestMapping(value = "/getservicelistbyshopid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据shopID获取服务信息（分页）")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "2"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的服务数目", required = true, dataType = "int") })
	private Map<String, Object> getServiceListByShopId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		try {
			ServiceInfo serviceCondition = new ServiceInfo();
			serviceCondition.setShopId(shopId);
			List<ServiceInfo> se = sService.getServiceList(serviceCondition, pageIndex, pageSize,null,null);
			int pageNum = pageIndex;
			List<ServiceInfo> serviceList=se;
			modelMap.put("serviceList", serviceList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		
		return modelMap;
	}


	//根据查询条件获取服务列表 分页
	@RequestMapping(value = "/listService", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据服务名称获取服务信息（分页）")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的服务数目", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "serviceName", value = "服务名称", required = true, dataType = "String", example = "测试service店铺")})
	private Map<String, Object> listService(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ServiceInfo> se = null;
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "page");
		int pageSize = HttpServletRequestUtil.getInt(request, "rows");
		// 空值判断
		if (pageIndex > 0 && pageSize > 0) {
			ServiceInfo serviceCondition = new ServiceInfo();	
			String serviceName = HttpServletRequestUtil.getString(request, "serviceName");
			if (serviceName != null) {
				try {
					// 模糊查询
					serviceCondition.setServiceName(URLDecoder.decode(serviceName, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}else{
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入服务名称！");
				return modelMap;
			}
			try {
				// 根据查询条件分页返回服务列表
				se = sService.getServiceList(serviceCondition, pageIndex, pageSize,null,null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (se != null) {
				modelMap.put("serviceList", se);
				modelMap.put("success", true);
			} else {
				// 取出数据为空，也返回new list以使得前端不出错
				modelMap.put("total", 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "查询信息为空");
			return modelMap;
		}
	}
	/**
	 * 根据Id返回服务信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchservicebyid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据serviceId获取该服务信息")
	@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> searchServiceById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ServiceInfo service = null;
		// 从请求中获取serviceId
		long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
		if (serviceId > 0) {
			try {
				// 根据Id获取服务实例
				service = sService.getByServiceId(serviceId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (service != null) {
                Shop shop = shopService.getByShopId(service.getShopId());
                service.setShopName(shop.getShopName());
				modelMap.put("serviceList", service);
				modelMap.put("success", true);
			} else {
				modelMap.put("serviceList", new ArrayList<ServiceInfo>());
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入正确服务Id！");
			return modelMap;
		}
	}

	//添加服务
	@RequestMapping(value = "/addservice", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "添加服务信息")
	private Map<String, Object> addService(
            @RequestBody @ApiParam(name = "ServiceInfo", value = "传入json格式", required = true) ServiceInfo serviceInfo, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 空值判断
		if (serviceInfo != null ) {
			try {
				//添加服务
				sService.addService(serviceInfo);
				Long shopId = serviceInfo.getShopId();
				Shop shop = shopService.getByShopId(shopId);
				shop.setEnableStatus(3);
				shopService.modifyShop(shop);
				modelMap.put("success", true);
				modelMap.put("serviceId", serviceInfo.getServiceId());
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入服务信息！");
		}
		return modelMap;
	}

	//更新服务
	@RequestMapping(value = "/modifyservice", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "修改服务信息（不修改图片）")
	private Map<String, Object> modifyService(
			@RequestBody @ApiParam(name = "ServiceInfo", value = "传入json格式,要传serviceId", required = true) ServiceInfo serviceInfo, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 空值判断
		if (serviceInfo != null && serviceInfo.getServiceId() != null) {
			try {
				//更新服务
				ServiceInfo ae = sService.modifyService(serviceInfo);
				Long shopId = serviceInfo.getShopId();
				Shop shop = shopService.getByShopId(shopId);
				shop.setEnableStatus(3);
				shopService.modifyShop(shop);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入服务信息！");
		}
		System.out.println(serviceInfo.toString());
		return modelMap;
	}


	//删除服务
	@RequestMapping(value = "/deleteservice", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除服务信息")
	@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> deleteService(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
		// 空值判断
		if (serviceId>0) {
			try {
				//删除服务
				sService.deleteService(serviceId);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "错误的serviceId");
		}
		return modelMap;
	}


	private void handleImage(HttpServletRequest request, ImageHolder serviceImg) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile serviceImgFile = (CommonsMultipartFile) multipartRequest.getFile("serviceImg");
		if (serviceImgFile != null && serviceImg != null) {
			serviceImg.setImage(serviceImgFile.getInputStream());
			serviceImg.setImageName(serviceImgFile.getOriginalFilename());
		}

	}


	@RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "上传服务相关图片，并更新服务图片地址")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3"),
			@ApiImplicitParam(paramType = "query", name = "createTime", value = "图片创建时间", required = true, dataType = "Date")
	})
	private Map<String, Object> uploadImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接收并转化相应的参数，包括服务id以及图片信息
		Long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
		Date createTime = HttpServletRequestUtil.getDate(request, "createTime");
		ImageHolder serviceImg = new ImageHolder("", null);
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if (commonsMultipartResolver.isMultipart(request)) {
				handleImage(request, serviceImg);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			System.out.println(e.getMessage());
			System.out.println(createTime);
			return modelMap;
		}
		// 2.上传服务图片
		ServiceImg se;
		try {
			se = sService.uploadImg(serviceId, serviceImg,createTime);
			//先更新服务其他信息，在更新服务照片
			ServiceInfo service=sService.getByServiceId(serviceId);
			service.setServiceImgAddr(serviceImgDao.getServiceImgList(serviceId).get(0).getImgAddr());
			sService.modifyService(service);
			modelMap.put("success", true);
			modelMap.put("serviceImgAddr", service.getServiceImgAddr());
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;
	}



	@RequestMapping(value = "/listserviceimg", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据serviceId获取该服务图片信息")
	@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> listServiceImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ServiceImg> aie = null;
		try {
			Long serviceId = HttpServletRequestUtil.getLong(request, "serviceId");
			aie = sService.getServiceImg(serviceId);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (aie!= null) {
			modelMap.put("serviceImgList", aie);
			modelMap.put("success", true);
		} else {
			modelMap.put("serviceImgList", new ArrayList<ServiceImg>());
			modelMap.put("success", true);
		}
		return modelMap;
		}


	@RequestMapping(value = "/deleteserviceimg", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除服务相关图片")
	@ApiImplicitParam(paramType = "query", name = "serviceImgId", value = "服务图片ID", required = true, dataType = "Long", example = "3")
	private Map<String, Object> deleteServiceImg(@RequestBody @ApiParam(name = "ServiceInfo", value = "传入json格式,要传serviceId", required = true)
															 ServiceImg serviceImg,
												 HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			sService.deleteServiceImg(serviceImg);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
