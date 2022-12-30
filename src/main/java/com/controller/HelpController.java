package com.controller;

import com.dto.HelpExecution;
import com.entity.Appeal;
import com.entity.Help;
import com.entity.PersonInfo;
import com.enums.HelpStateEnum;
import com.exceptions.HelpOperationException;
import com.service.AppealService;
import com.service.HelpService;
import com.service.PersonInfoService;
import com.service.UsersInformService;
import com.util.ScheduleTask;
import com.util.HttpServletRequestUtil;
import com.util.SecurityUtil;
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
	private PersonInfoService personInfoService;
	@Autowired
	private ScheduleTask scheduleTask;

	@RequestMapping(value = "/gethelplistbyappealid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据求助ID获取求助信息列表(分页)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的列数", required = true, dataType = "int") })
	private Map<String, Object> getHelpListByAppealId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		if (appealId <= 0) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效");
		} else {
			try {
				Help helpCondition = new Help();
				helpCondition.setAppealId(appealId);
				HelpExecution helpExecution = helpService.getHelpListFY(helpCondition, null, null, pageIndex, pageSize,null,null);
				List<Help> helps = helpExecution.getHelpList();

				//更新状态
				scheduleTask.updateHelpStatus(helps);

				for (Help help2 : helps) {
					PersonInfo personInfo = personInfoService.getPersonInfoByUserId(help2.getUserId());
					help2.setPersonInfo(personInfo);
				}
				int pageNum = (int) (helpExecution.getCount() / pageSize);
				if (pageNum * pageSize < helpExecution.getCount())
					pageNum++;
				modelMap.put("helpList", helps);
				modelMap.put("pageNum", pageNum);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}
		return modelMap;
	}

	@RequestMapping(value = "/queryishelp", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询是否帮助了这个求助")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String") })
	private Map<String, Object> queryisHelp(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		String token = request.getHeader("token");
		Long userId = SecurityUtil.getUserInfo().getUserId();
		if (appealId <= 0) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId无效");
		} else {
			try {
				Help helpCondition = new Help();
				helpCondition.setAppealId(appealId);
				helpCondition.setUserId(userId);
				HelpExecution helpExecution = helpService.getHelpListFY(helpCondition, null, null, 0, 100,null,null);
				if (helpExecution.getCount() > 0) {
					modelMap.put("ishelp", true);
					modelMap.put("success", true);
				} else {
					modelMap.put("ishelp", false);
					modelMap.put("success", true);
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}
		return modelMap;
	}

	@RequestMapping(value = "/gethelplistbyuserid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据用户ID和帮助状态查询帮助（可增加输入的条件有：帮助状态，指定日期范围，搜币（大于等于输入搜币）信息(分页)", notes = "进行中:helpStatus=1（返回的helpStatus=0表示还没有被选中，helpStatus=1表示已被选中）,已完成:helpStatus=2,已失效:helpStatus=3")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "helpStatus", value = "帮助状态", required = false, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "startTime", value = "时间范围（下限）", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "endTime", value = "时间范围（上限）", required = false, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "souCoin", value = "搜币数量", required = false, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页的数目", required = true, dataType = "int") })
	private Map<String, Object> getHelpListByUserId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		String token = request.getHeader("token");
		Integer helpStatus = HttpServletRequestUtil.getInteger(request, "helpStatus");
		Date startTime = HttpServletRequestUtil.getDate(request, "startTime");
		Date endTime = HttpServletRequestUtil.getDate(request, "endTime");
		Long souCoin = HttpServletRequestUtil.getLong(request, "souCoin");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		Long userId = SecurityUtil.getUserInfo().getUserId();
		try {
			Help help = new Help();
			help.setUserId(userId);
			help.setHelpStatus(helpStatus);
			help.setAllCoin(souCoin);
			System.out.println(help);
			HelpExecution helpExecution = helpService.getHelpListFY(help, startTime, endTime, pageIndex, pageSize,null,null);
			if (helpExecution.getState() == HelpStateEnum.SUCCESS.getState()) {
				int pageNum = (int) (helpExecution.getCount() / pageSize);
				if (pageNum * pageSize < helpExecution.getCount())
					pageNum++;

				List<Help> helps = helpExecution.getHelpList();
				//更新状态
				scheduleTask.updateHelpStatus(helps);

				modelMap.put("helpList", helps);
				modelMap.put("pageNum", pageNum);
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", HelpStateEnum.stateOf(helpExecution.getState()).getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/gethelpbyhelpid", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "根据帮助ID获取帮助信息")
	@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助Id", required = true, dataType = "Long")
	private Map<String, Object> getHelpByHelpId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		if (helpId != null) {
			try {
				// 获取帮助信息
				Help help = helpService.getByHelpId(helpId);
				modelMap.put("help", help);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "helpId无效");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addHelp", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "创建帮助")
	@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String")
	private Map<String, Object> addHelp(
			@RequestBody @ApiParam(name = "help", value = "传入json格式,只传appealId和appealTitle就好了", required = true) Help help,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = request.getHeader("token");
		Long userId = SecurityUtil.getUserInfo().getUserId();
		help.setUserId(userId);
		HelpExecution helpExecution;
		try {
			helpExecution = helpService.addHelp(help);
			if (helpExecution.getState() == HelpStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("helpId", helpExecution.getHelp().getHelpId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", helpExecution.getStateInfo());
			}
		} catch (HelpOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/commenthelp", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "帮助评论", notes = "评论帮助需要输入帮助Id、完成分、效率分和态度分")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "token", value = "包含求助者用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助Id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "completion", value = "完成分", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "efficiency", value = "效率分", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "attitude", value = "态度分", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "commentInfo", value = "评论信息", dataType = "String")})
	private Map<String, Object> commentHelp(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		int completion = HttpServletRequestUtil.getInt(request, "completion");
		int efficiency = HttpServletRequestUtil.getInt(request, "efficiency");
		int attitude = HttpServletRequestUtil.getInt(request, "attitude");
		String commentInfo = HttpServletRequestUtil.getString(request,"commentInfo");
		System.out.println(commentInfo);

		String token = request.getHeader("token");

		Long userId = SecurityUtil.getUserInfo().getUserId();

		Help help = helpService.getByHelpId(helpId);
		Appeal appeal = appealService.getByAppealId(help.getAppealId());
		if (appeal.getUserId() != userId) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "该求助不属于该用户");
			return modelMap;
		}
		help.setCompletion(completion);
		help.setEfficiency(efficiency);
		help.setAttitude(attitude);
		help.setCommentInfo(commentInfo);
		System.out.println(help);

		HelpExecution helpExecution;
		try {
			helpExecution = helpService.modifyHelp(help);
			if (helpExecution.getState() == HelpStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
				modelMap.put("helpId", helpExecution.getHelp().getHelpId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", helpExecution.getStateInfo());
			}
		} catch (HelpOperationException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/selectHelper", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "选择帮助的人")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助Id", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String") })
	private Map<String, Object> selectHelper(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		String token = request.getHeader("token");

		Long userId = SecurityUtil.getUserInfo().getUserId();
		try {
			HelpExecution he = helpService.selectHelp(helpId, appealId, userId);
			if (he.getState() != HelpStateEnum.SUCCESS.getState()) {
				modelMap.put("success", false);
				modelMap.put("errMsg", he.getStateInfo());
			}
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
			@ApiImplicitParam(paramType = "header", name = "token", value = "包含用户信息的token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "query", name = "appealId", value = "求助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "helpId", value = "帮助ID", required = true, dataType = "Long"),
			@ApiImplicitParam(paramType = "query", name = "additionSouCoin", value = "追赏金数", required = true, dataType = "Long") })
	private Map<String, Object> additionSouCoin(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String token = request.getHeader("token");
		Long appealId = HttpServletRequestUtil.getLong(request, "appealId");
		Long helpId = HttpServletRequestUtil.getLong(request, "helpId");
		Long additionSouCoin = HttpServletRequestUtil.getLong(request, "additionSouCoin");
		Help help = helpService.getByHelpId(helpId);
		if (help.getAppealId() != appealId) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "appealId和helpId不对应");
			return modelMap;
		}
		Long appealUserId = SecurityUtil.getUserInfo().getUserId();
		try {
			HelpExecution he = helpService.additionSouCoin(helpId, appealUserId, additionSouCoin);
			if (he.getState() != HelpStateEnum.SUCCESS.getState()) {
				modelMap.put("success", false);
				modelMap.put("errMsg", he.getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		modelMap.put("success", true);
		return modelMap;
	}
}
