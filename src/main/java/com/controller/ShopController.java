package com.controller;

import com.util.ImageHolder;
import com.entity.Shop;
import com.service.ShopService;
import com.service.UsersInformService;
import com.util.HttpServletRequestUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/shop")
public class ShopController {
	@Autowired
	private ShopService shopService;
	private UsersInformService usersInformService;


	/**
	 * 店铺模糊查询
	 */
	@RequestMapping("/getshoplistbylikename")
	@ResponseBody
	@ApiImplicitParams({@ApiImplicitParam(paramType = "query", name ="key", value = "关键字", required = true, dataType = "String"),
						@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
						@ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int")})
	public Map<String, Object> getShop(HttpServletRequest request)
	{
		Map<String,Object> modelMap = new HashMap<String, Object>();
		String key = HttpServletRequestUtil.getString(request,"key");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		try {
			// 模糊查询已绑定商铺对象
			Shop shopCondition=new Shop();
			shopCondition.setShopName(key);
			List<Shop> shops=new ArrayList<Shop>();
			shops=shopService.getShopList(shopCondition,pageIndex,pageSize,null,null);
			modelMap.put("sucess",true);
			modelMap.put("shoplist",shops);
			return modelMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelMap.put("sucess",false);
		return modelMap;
	}



	@RequestMapping(value = "/getshoplistbyuserid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据用户ID获取其所有商铺信息（分页）")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "用户userId", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "sort", value = "按照什么来排序", required = true, dataType = "String", example = "time"),
			@ApiImplicitParam(paramType = "query", name = "order", value = "排序方式", required = true, dataType = "String", example = "DESC")})
	private Map<String, Object> getShopListByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		String sort = HttpServletRequestUtil.getString(request, "sort");
		String order = HttpServletRequestUtil.getString(request, "order");
		long userId = HttpServletRequestUtil.getLong(request, "userId");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setUserId(userId);
			List<Shop> se = shopService.getShopList(shopCondition, pageIndex, pageSize,sort,order);
			int pageNum = (int) (se.size() / pageSize);
			if (pageNum * pageSize < se.size())
				pageNum++;
			modelMap.put("shopList", se);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}




	/**
	 * 根据shopid返回店铺信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchshopbyid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> searchShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop shop = null;
		// 从请求中获取店铺Id
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId != null) {
			try {
				// 根据Id获取店铺实例
				shop = shopService.getByShopId(shopId);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
			if (shop != null) {
				modelMap.put("rows", shop);
				modelMap.put("total", 1);
				modelMap.put("success", true);
			} else {
				modelMap.put("rows", shop);
				modelMap.put("total", 0);
				modelMap.put("success", true);
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "空的查询信息");
			return modelMap;
		}
	}


	/**
	 * 根据位置返回店铺信息
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchnearbyshops", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "返回2.5km内的所有商铺")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "longitude", value = "屏幕中心的经度", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "latitude", value = "屏幕中心的纬度", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "cover", value = "搜索范围", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "shopName", value = "商铺名", required = false, dataType = "String"), })
	private Map<String, Object> searchNearbyShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		float cover = HttpServletRequestUtil.getFloat(request, "cover");
		if(cover<=0.0){cover=2.5f;}
		String shopName = HttpServletRequestUtil.getString(request, "shopName");
		float minlat = 0f;// 定义经纬度四个极限值
		float maxlat = 0f;
		float minlng = 0f;
		float maxlng = 0f;

		// 先计算查询点的经纬度范围
		float r = 6371;// 地球半径千米
		float dlng = (float) (2 * Math.asin(Math.sin(cover / (2 * r)) / Math.cos(latitude * Math.PI / 180)));
		dlng = (float) (dlng * 180 / Math.PI);
		float dlat = cover / r;
		dlat = (float) (dlat * 180 / Math.PI);
		if (dlng < 0) {
			minlng = longitude + dlng;
			maxlng = longitude - dlng;
		} else {
			minlng = longitude - dlng;
			maxlng = longitude + dlng;
		}
		minlat = latitude - dlat;
		maxlat = latitude + dlat;
		try {
			List<Shop> se = shopService.getNearbyShopList(maxlat, minlat, maxlng, minlng, shopName);
			modelMap.put("shopList", se);
			modelMap.put("minlng", minlng);
			modelMap.put("maxlng", maxlng);
			modelMap.put("minlat", minlat);
			modelMap.put("maxlat", maxlat);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}


	/**
	 *注册店铺
	 *
	 * @return
	 */
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	@ApiImplicitParam(paramType = "query", name = "userId", value = "用户userId", required = true, dataType = "Long")
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = request.getHeader("token");
		// 注册商铺
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		Shop shop=new Shop();
		shop.setUserId(userId);
		try {
			shop = shopService.addShop(shop);
			modelMap.put("success", true);
			modelMap.put("shopId", shop.getShopId());
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}


	/**
	 * 修改店铺信息
	 * 
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		String shopName = HttpServletRequestUtil.getString(request, "shopName");
		String businessLicenseCode = HttpServletRequestUtil.getString(request, "businessLicenseCode");
		int perCost = HttpServletRequestUtil.getInt(request, "perCost");
		String phone = HttpServletRequestUtil.getString(request, "phone");
		String province = HttpServletRequestUtil.getString(request, "province");
		String city = HttpServletRequestUtil.getString(request, "city");
		String district = HttpServletRequestUtil.getString(request, "district");
		String fullAddress = HttpServletRequestUtil.getString(request, "fullAddress");
		String shopMoreInfo = HttpServletRequestUtil.getString(request, "shopMoreInfo");
		int isMobile = HttpServletRequestUtil.getInt(request, "isMobile");
		Float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		Float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		String businessScope = HttpServletRequestUtil.getString(request, "businessScope");
		
		Shop shop = new Shop();
		shop.setShopId(shopId);
		shop.setUserId(userId);
		shop.setShopName(shopName);
		shop.setBusinessLicenseCode(businessLicenseCode);
		shop.setPerCost(perCost);
		shop.setPhone(phone);
		shop.setProvince(province);
		shop.setCity(city);
		shop.setDistrict(district);
		shop.setFullAddress(fullAddress);
		shop.setShopMoreInfo(shopMoreInfo);
		shop.setIsMobile(isMobile);
		shop.setLatitude(latitude);
		shop.setLongitude(longitude);
		shop.setEnableStatus(enableStatus);
		shop.setBusinessScope(businessScope);

		CommonsMultipartFile profileImg = null;
		CommonsMultipartFile businessLicenseImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
			businessLicenseImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("businessLicenseImg");
		}
		// 空值判断
		if (shop.getShopId() != null) {
			try {
				ImageHolder profileImgHolder = null;
				if (profileImg != null) {
					profileImgHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
				}
				ImageHolder businessLicenseImgHolder = null;
				if (businessLicenseImg != null) {
					businessLicenseImgHolder = new ImageHolder(businessLicenseImg.getOriginalFilename(),
							businessLicenseImg.getInputStream());
				}
				Shop se = shopService.modifyShop(shop, businessLicenseImgHolder, profileImgHolder);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
		}
		return modelMap;
	}

	/**
	 * 添加店铺信息
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long userId = HttpServletRequestUtil.getLong(request, "userId");
		String shopName = HttpServletRequestUtil.getString(request, "shopName");
		String businessLicenseCode = HttpServletRequestUtil.getString(request, "businessLicenseCode");
		int perCost = HttpServletRequestUtil.getInt(request, "perCost");
		String phone = HttpServletRequestUtil.getString(request, "phone");
		String province = HttpServletRequestUtil.getString(request, "province");
		String city = HttpServletRequestUtil.getString(request, "city");
		String district = HttpServletRequestUtil.getString(request, "district");
		String fullAddress = HttpServletRequestUtil.getString(request, "fullAddress");
		String shopMoreInfo = HttpServletRequestUtil.getString(request, "shopMoreInfo");
		int isMobile = HttpServletRequestUtil.getInt(request, "isMobile");
		Float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		Float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		int enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
		String businessScope = HttpServletRequestUtil.getString(request, "businessScope");
		
		Shop shop = new Shop();
		shop.setUserId(userId);
		shop.setShopName(shopName);
		shop.setBusinessLicenseCode(businessLicenseCode);
		shop.setPerCost(perCost);
		shop.setPhone(phone);
		shop.setProvince(province);
		shop.setCity(city);
		shop.setDistrict(district);
		shop.setFullAddress(fullAddress);
		shop.setShopMoreInfo(shopMoreInfo);
		shop.setIsMobile(isMobile);
		shop.setLatitude(latitude);
		shop.setLongitude(longitude);
		shop.setEnableStatus(enableStatus);
		shop.setBusinessScope(businessScope);

		CommonsMultipartFile profileImg = null;
		CommonsMultipartFile businessLicenseImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			profileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("profileImg");
			businessLicenseImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("businessLicenseImg");
		}
		// 空值判断

		try {
			ImageHolder profileImgHolder = null;
			if (profileImg != null) {
				profileImgHolder = new ImageHolder(profileImg.getOriginalFilename(), profileImg.getInputStream());
			}
			ImageHolder businessLicenseImgHolder = null;
			if (businessLicenseImg != null) {
				businessLicenseImgHolder = new ImageHolder(businessLicenseImg.getOriginalFilename(),
						businessLicenseImg.getInputStream());
			}
			Shop se = shopService.addShop(shop, businessLicenseImgHolder, profileImgHolder);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		return modelMap;
	}
}
