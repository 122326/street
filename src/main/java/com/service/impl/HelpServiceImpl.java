package com.service.impl;

import com.dao.AppealDao;
import com.dao.HelpDao;
import com.dao.PersonInfoDao;
import com.entity.Appeal;
import com.entity.Help;
import com.entity.PersonInfo;
import com.service.HelpService;
import com.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class HelpServiceImpl implements HelpService {
	@Autowired
	private HelpDao helpDao;
	@Autowired
	private AppealDao appealDao;
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public List<Help> getHelpListFY(Help helpCondition, Date startTime, Date endTime, int pageIndex, int pageSize, String sort, String order) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的帮助列表
		List<Help> helpList = helpDao.queryHelpListFY(helpCondition, startTime, endTime, rowIndex, pageSize,sort,order);
		return helpList;
	}

	@Override
	public Help getByHelpId(long helpId) {
		return helpDao.queryByHelpId(helpId);
	}


	@Override
	public Help modifyHelp(Help help) {
		helpDao.updateHelp(help);
		return help;
	}

	@Override
	public Help addHelp(Help help){
		Long userId = help.getUserId();
		Long appealId = help.getAppealId();
		Appeal appeal = appealDao.queryByAppealId(appealId);
		Help helpCondition = new Help();
		helpCondition.setAppealId(appealId);
		helpCondition.setUserId(userId);
		List<Help> helpList = helpDao.queryHelpList(helpCondition);
		Float avgCompletion = 0f;
		Float avgEfficiency = 0f;
		Float avgAttitude = 0f;
		helpCondition.setAppealId(null);
		helpCondition.setHelpStatus(2);// 查找已完成的帮助
		helpList = helpDao.queryHelpList(helpCondition);
		for (Help help1 : helpList) {
			avgCompletion += help1.getCompletion();
			avgEfficiency += help1.getEfficiency();
			avgAttitude += help1.getAttitude();
		}
		int helpSize = helpList.size();
		if (helpSize > 0) {
			avgCompletion = avgCompletion / helpSize;
			avgAttitude = avgAttitude / helpSize;
			avgEfficiency = avgEfficiency / helpSize;
			}
		// 给帮助信息赋上以前的评价
		help.setAvgAttitude(avgAttitude);
		help.setAvgCompletion(avgCompletion);
		help.setAvgEfficiency(avgEfficiency);

		help.setEndTime(appeal.getEndTime());
		// 给帮助信息赋初始值
		help.setHelpStatus(0);
		help.setAdditionalCoin(0l);
		help.setAttitude(0);
		help.setEfficiency(0);
		help.setCompletion(0);
		help.setAllCoin(0l);
		// 添加帮助信息
		helpDao.insertHelp(help);
		return help;
	}

	@Override
	public List<Help> getHelpList(Help helpCondition) {
		List<Help> helpList = helpDao.queryHelpList(helpCondition);
		return helpList;
	}

	@Override
	@Transactional
	public Help selectHelp(Long helpId, Long appealId, Long appealUserId) {
		Appeal appeal = appealDao.queryByAppealId(appealId);
		//求助状态：已选定帮助者
		appeal.setAppealStatus(1);
		Help helpCondition = helpDao.queryByHelpId(helpId);
		helpCondition.setAppealId(appealId);
		//帮助状态：已被选定
		helpCondition.setHelpStatus(1);
		return helpCondition;
	}

	@Override
	@Transactional
	public Help additionSouCoin(Long helpId, Long appealUserId, Long additionSouCoin)
	{
		Help help = helpDao.queryByHelpId(helpId);
		//求助者减钱
		Appeal appeal = appealDao.queryByAppealId(help.getAppealId());
		PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(appealUserId);
		Long appealerSouCoin = personInfo.getSouCoin();
		personInfo.setSouCoin(appealerSouCoin - additionSouCoin);
		personInfoDao.updatePersonInfo(personInfo);
		//帮助者加钱
		Long helpUserId = help.getUserId();
		personInfo = personInfoDao.queryPersonInfoByUserId(helpUserId);
		appealerSouCoin = personInfo.getSouCoin();
		personInfo.setSouCoin(appealerSouCoin + additionSouCoin);
		personInfoDao.updatePersonInfo(personInfo);
		//更新帮助信息
		help.setAdditionalCoin(additionSouCoin);
		Long allCoin = help.getAllCoin();
		help.setAllCoin(allCoin + additionSouCoin);
		helpDao.updateHelp(help);
		return help;
	}

}
