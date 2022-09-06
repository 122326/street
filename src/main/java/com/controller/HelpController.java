package com.controller;

import com.entity.Appeal;
import com.entity.Help;
import com.entity.PersonInfo;
import com.service.AppealService;
import com.service.HelpService;
import com.service.PersonInfoService;
import com.service.UsersInformService;
import com.util.ScheduleTask;
import com.util.HttpServletRequestUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/help")
@Api(value = "HelpController|对帮助操作的控制器")
public class HelpController {
	@Autowired
	private HelpService helpService;
	@Autowired
	private AppealService appealService;
	@Autowired
	private UsersInformService usersInformService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private ScheduleTask scheduleTask;

	@RequestMapping(value = "/gethelplistbyappealid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据求助ID获取帮助信息列表(分页)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int") })
	private Map<String, Object> getHelpListByAppealId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		if (appealId <= 0) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效！");
		} else {
			try {
				Help helpCondition = new Help();
				helpCondition.setAppealId(appealId);
				List<Help> helps = helpService.getHelpListFY(helpCondition, null, null, pageIndex, pageSize,null,null);
				//更新状态
				scheduleTask.updateHelpStatus(helps);
				modelMap.put("helpList", helps);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}
		return modelMap;
	}


	@RequestMapping(value = "/gethelplistbyuserid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据查询条件获得帮助", notes = "进行中:helpStatus=1（返回的helpStatus=0表示还没有被选中，helpStatus=1表示已被选中）,已完成:helpStatus=2,已失效:helpStatus=3")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", required = true, dataType = "Long") ,
			@ApiImplicitParam(paramType = "query", name = "helpStatus", value = "帮助状态", required = false, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助Id", required = false, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "startTime", value = "时间范围（开始）", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "endTime", value = "时间范围（截止）", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "souCoin", value = "搜币数量", required = false, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int") })
	private Map<String, Object> getHelpListByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		Long userId = HttpServletRequestUtil.getLong(request,"userId");
		Integer helpStatus = HttpServletRequestUtil.getInteger(request, "helpStatus");
		Long helpId = HttpServletRequestUtil.getLong(request,"helpId");
		Date startTime = HttpServletRequestUtil.getDate(request, "startTime");
		Date endTime = HttpServletRequestUtil.getDate(request, "endTime");
		Long souCoin = HttpServletRequestUtil.getLong(request, "souCoin");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		if(pageIndex<0||pageSize<0){
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入正确的分页信息！");
			return modelMap;
		}
		System.out.println("分页数据：页码："+pageIndex+"页面大小："+pageSize);
		PersonInfo pe=personInfoService.getPersonInfoByUserId(userId);
		if(pe==null)
		{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入正确的userId！");
			return modelMap;
		}

		try {
			Help help = new Help();
			help.setUserId(userId);
			help.setHelpStatus(helpStatus);
			help.setHelpId(helpId);
			help.setAllCoin(souCoin);
			System.out.println("从前端获得的查询帮助列表的信息组成的help对象："+help);
			List<Help> helps = helpService.getHelpListFY(help, startTime, endTime, pageIndex, pageSize,null,null);
			System.out.println("向前的返回的帮助列表"+helps.toString());
			//更新状态
			scheduleTask.updateHelpStatus(helps);
			modelMap.put("helpList", helps);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}



	@RequestMapping(value = "/addHelp/{endTime}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "创建帮助")
	//@ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", required = false, dataType = "Long")
	private Map<String, Object> addHelp(
			@PathVariable("endTime") String endTime,
			@RequestBody @ApiParam(name = "help", required = true) Help help,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		//判断该用户是否已经对该求助发出过帮助请求
		List<Help> hl=new ArrayList<Help>();
		hl=helpService.getHelpListFY(help,null,null,1,10,null,null);
		System.out.println("在数据库找到的同样帮助信息对象："+hl.toString());
		if(!hl.isEmpty()){
			modelMap.put("success", false);
			modelMap.put("errMsg", "该用户已经向此求助发出过帮助请求！");
			return modelMap;
		}

		Date et=new Date();
		try {
			et = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("添加帮助时从前端收到的结束时间："+endTime);
		System.out.println("添加帮助时转换后的结束时间："+et);
		System.out.println("添加帮助时从前端收到的help对象："+help.toString());
		//Long userId = HttpServletRequestUtil.getLong(request,"userId");
		//help.setUserId(userId);
		help.setEndTime(et);
		Help helpExecution;
		try {
			helpExecution = helpService.addHelp(help);
			System.out.println("成功添加帮助时传回的数据："+helpExecution.getHelpId());
			modelMap.put("success", true);
			modelMap.put("helpId", helpExecution.getHelpId());
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/commenthelp", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "评论帮助", notes = "评论帮助需要输入帮助Id、完成分、效率分和态度分")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "求助用户Id", required = true, dataType = "Long") ,
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助Id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "completion", value = "完成分", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "efficiency", value = "效率分", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "attitude", value = "态度分", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "commentInfo", value = "评论信息",required = false, dataType = "String")})
	private Map<String, Object> commentHelp(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		int completion = HttpServletRequestUtil.getInt(request, "completion");
		int efficiency = HttpServletRequestUtil.getInt(request, "efficiency");
		int attitude = HttpServletRequestUtil.getInt(request, "attitude");
		String commentInfo = HttpServletRequestUtil.getString(request,"commentInfo");
		Long userId = HttpServletRequestUtil.getLong(request,"userId");
		Help help = helpService.getByHelpId(helpId);
		Appeal appeal = appealService.getByAppealId(help.getAppealId());
		if (appeal.getUserId() != userId) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId与userId不匹配！");
			return modelMap;
		}
		help.setCompletion(completion);
		help.setEfficiency(efficiency);
		help.setAttitude(attitude);
		help.setCommentInfo(commentInfo);
		System.out.println(help);

		Help helpExecution;
		try {
			helpExecution = helpService.modifyHelp(help);
			modelMap.put("success", true);
			modelMap.put("helpId", helpExecution.getHelpId());
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/selectHelper", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "选择帮助者")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助Id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "userId", value = "用户Id", required = false, dataType = "Long")  })
	private Map<String, Object> selectHelper(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		Long userId = HttpServletRequestUtil.getLong(request,"userId");
		try {
			Help he = helpService.selectHelp(helpId, appealId, userId);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		modelMap.put("success", true);
		return modelMap;
	}

	@RequestMapping(value = "/additionsoucoin", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "求助者追赏")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userId", value = "求助用户Id", required = true, dataType = "Long") ,
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "additionSouCoin", value = "追赏金数", required = true, dataType = "Long") })
	private Map<String, Object> additionSouCoin(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		Long additionSouCoin = HttpServletRequestUtil.getLong(request, "additionSouCoin");
		Help help = helpService.getByHelpId(helpId);
		if (help.getAppealId() != appealId) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId和helpId不匹配！");
			return modelMap;
		}
		Long appealUserId =  HttpServletRequestUtil.getLong(request,"userId");
		try {
			Help he = helpService.additionSouCoin(helpId, appealUserId, additionSouCoin);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
