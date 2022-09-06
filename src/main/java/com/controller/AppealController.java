package com.controller;

import com.util.ImageHolder;
import com.entity.*;
import com.service.AppealService;
import com.service.HelpService;
import com.service.UsersInformService;
import com.util.ScheduleTask;
import com.util.HttpServletRequestUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/appeal")
@Api(value = "AppealController|对求助操作的控制器")
public class AppealController {
	@Autowired
	private AppealService appealService;
	@Autowired
	private UsersInformService usersInformService;
	@Autowired
	private HelpService helpservice;
	@Autowired
	private ScheduleTask scheduleTask;


	/*
	* 根据查询条件分页获取求助
	* */
	@RequestMapping(value = "/getappeallistbyuserid", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "根据查询条件分页获取求助",notes = "appealStatus可选进行中:appealStatus=1（返回的appealStatus=0表示没有确定帮助者，appealStatus=1表示已确定帮助者）,已完成:appealStatus=2,已失效:appealStatus=3")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int")})
	private Map<String, Object> getAppealListByUserId(
			@RequestBody @ApiParam(name = "appeal", value = "传入json格式,不用传appealId", required = true) Appeal appeal,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		System.out.println("查询我的求助前端发回来的分页信息："+pageIndex+"   "+pageSize);
		System.out.println("查询我的求助前端发回来的appeal对象："+appeal.toString());
		if(pageIndex>0&&pageSize>0){
			if(appeal.getUserId()!=null) {
				try {
					List<Appeal> ae = appealService.getAppealList(appeal, pageIndex, pageSize, null, null);
					scheduleTask.updateAppealStatus(ae);
					System.out.println("查询我的求助时向前端返回的appeal列表："+ae.toString());
					modelMap.put("appealList", ae);
					modelMap.put("pageNum", pageIndex);
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				}
			}else{
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确的userId！");
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入正确的分页信息！");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getappealbyid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据求助ID获取求助信息")
	@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long")
	private Map<String, Object> getAppealByAppealId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		Appeal ap = appealService.getByAppealId(appealId);
		if (ap!=null) {
			try {
				// 获取求助信息
				Appeal appeal = appealService.getByAppealId(appealId);
				// 获取求助详情图列表
				AppealImg appealImg = new AppealImg();
				appealImg.setAppealId(appealId);
				List<AppealImg> appealImgList = appealService.getAppealImgList(appealImg, 0, 100, null, null);
				appeal.setAppealImgList(appealImgList);
				modelMap.put("appeal", appeal);
				modelMap.put("success", true);
				return modelMap;
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效!");
			return modelMap;
		}

	}

	@RequestMapping(value = "/registerappeal/{startTime}/{endTime}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "创建求助（不添加图片）", notes = "不用传appealId")
	//@ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", required = true, dataType = "Long")
	private Map<String, Object> registerAppeal(
			@PathVariable("startTime") String startTime ,
			@PathVariable("endTime") String endTime ,
			@RequestBody @ApiParam(name = "appeal", value = "传入json格式,不用传appealId", required = true) Appeal appeal,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 添加求助
		System.out.println("创建求助时收到的起始时间："+startTime+"；收到的结束时间："+endTime);
		System.out.println("创建求助时收到的appeal："+appeal.toString());
		Date st= null;
		try {
			st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date et= null;
		try {
			et = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("转换的起始时间："+st+"；转换的结束时间："+et);
		appeal.setStartTime(st);
		appeal.setEndTime(et);
		Appeal se;
		try {
			se = appealService.addAppeal(appeal);
			modelMap.put("success", true);
			modelMap.put("appealId", se.getAppealId());
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			System.out.println(e.toString());
		}
		return modelMap;
	}


	private void handleImage(HttpServletRequest request, ImageHolder appealImg) throws IOException {
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile appealImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("appealImg");
		if (appealImgFile != null && appealImg != null) {
			appealImg.setImage(appealImgFile.getInputStream());
			appealImg.setImageName(appealImgFile.getOriginalFilename());
		}
	}

	@RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "上传求助图片")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助id", required = true, dataType = "Long")})
	private Map<String, Object> uploadImg(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接收并转化相应的参数，包括求助id以及图片信息
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		if(appealService.getByAppealId(appealId)==null)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "该求助不存在！");
			return modelMap;
		}
		ImageHolder appealImg = new ImageHolder("", null);
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if (commonsMultipartResolver.isMultipart(request)) {
				handleImage(request, appealImg);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		Long userId = HttpServletRequestUtil.getLong(request,"userId");
		// 2.上传求助图片
		try {
			AppealImg ae = appealService.uploadImg(appealId, appealImg, userId);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		modelMap.put("success", true);
		return modelMap;
	}


	/*
	* 按范围搜索求助
	* */
	@RequestMapping(value = "/searchnearbyappeals", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "返回一定范围内的所有有效（没确定帮助人、没过时的）求助")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "longitude", value = "屏幕中心的经度", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "latitude", value = "屏幕中心的纬度", required = true, dataType = "Float"),
			@ApiImplicitParam(paramType = "query", name = "appealTitle", value = "求助标题", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "cover", value = "求助范围", required = true, dataType = "float")})
	private Map<String, Object> searchNearbyAppeals(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		float longitude = HttpServletRequestUtil.getFloat(request, "longitude");
		float latitude = HttpServletRequestUtil.getFloat(request, "latitude");
		String appealTitle = HttpServletRequestUtil.getString(request, "appealTitle");
		float cover = HttpServletRequestUtil.getFloat(request, "cover");
		if(cover<=0.0){cover=2.5f;}
		System.out.println("传回的经度："+longitude+"；传回的纬度："+latitude+"；传回的求助范围："+cover);
		if(latitude<-90||latitude>90||longitude>180||longitude<-180)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入有效经纬度！");
			return modelMap;
		}

		float minlat = 0f;// 定义经纬度四个极限值
		float maxlat = 0f;
		float minlng = 0f;
		float maxlng = 0f;

		// 先计算查询点的经纬度范围
		float r = 6371;// 地球半径千米
		float dis = cover;// 距离（单位：千米），没传数据或数据不合理则查询范围2.5km内的所有求助
		float dlng = (float) (2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(latitude * Math.PI / 180)));
		dlng = (float) (dlng * 180 / Math.PI);
		float dlat = dis / r;
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
			List<Appeal> ae = appealService.getNearbyAppealList(maxlat, minlat, maxlng, minlng, appealTitle);
			System.out.println("查看附近求助时找到的求助："+ae);
			modelMap.put("appealList", ae);
			modelMap.put("minlng", minlng);
			modelMap.put("maxlng", maxlng);
			modelMap.put("minlat", minlat);
			modelMap.put("maxlat", maxlat);
			modelMap.put("success", true);
		} catch (Exception e) {
			System.out.println(e.toString());
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}


	/*
	* 求助结束相关操作
	* */
	@RequestMapping(value = "/competeappeal", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "求助者确定完成求助并支付搜币")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "求助用户Id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助ID", required = true, dataType = "Long")})
	private Map<String, Object> competeAppeal(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		Long userId = HttpServletRequestUtil.getLong(request,"userId");
		Help help = helpservice.getByHelpId(helpId);
		if (help.getAppealId() != appealId) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId和helpId不匹配！");
			return modelMap;
		}
		try {
			Appeal ae = appealService.completeAppeal(appealId, helpId, userId);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}


	@RequestMapping(value = "/cancelappeal", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "求助者撤销求助")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "求助用户Id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助Id", required = true, dataType = "Long")})
	private Map<String, Object> cancelAppeal(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		Long appealUserId = HttpServletRequestUtil.getLong(request,"userId");
		try {
			appealService.cancelAppeal(appealUserId, appealId);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
	}

	@RequestMapping(value = "/delappealimg/{appealImgId}", method = RequestMethod.POST)
	@ApiOperation(value = "求助者删除图片")
	private Map<String, Object> delAppealImg(@PathVariable(name = "appealImgId") Long appealImgId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			appealService.delAppealImg(appealImgId);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
