package com.service.impl;

import com.dao.AppealDao;
import com.dao.HelpDao;
import com.entity.Appeal;
import com.entity.Help;
import com.service.AppealCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AppealCheckServiceImpl implements AppealCheckService {
	@Autowired
	private AppealDao appealDao;
	@Autowired
	private HelpDao helpDao;

	@Override
	@Transactional
	public void checkAppeal()  {
		Appeal appealCondition = new Appeal();
		appealCondition.setAppealStatus(0);
		List<Appeal> appealList = appealDao.queryAppealList(appealCondition);
		Date now = new Date();
		for (Appeal appeal : appealList) {
			if (appeal.getEndTime().before(now)) {
				appeal.setAppealStatus(3);
				appealDao.updateAppeal(appeal);
			}
		}
	}

	@Override
	@Transactional
	public void checkHelp(){
		Help helpCondition = new Help();
		helpCondition.setHelpStatus(0);
		List<Help> helpList=helpDao.queryHelpList(helpCondition);
		Date now=new Date();
		for(Help help:helpList){
			if(help.getEndTime().before(now)){
				help.setHelpStatus(3);
				helpDao.updateHelp(help);
			}
		}
	}

}
