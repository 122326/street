package com.service.impl;

import com.dao.AppealDao;
import com.dao.AppealImgDao;
import com.dao.HelpDao;
import com.dao.PersonInfoDao;
import com.util.ImageHolder;
import com.entity.Appeal;
import com.entity.AppealImg;
import com.entity.Help;
import com.entity.PersonInfo;
import com.service.AppealService;
import com.util.ImageUtil;
import com.util.PageCalculator;
import com.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class AppealServiceImpl implements AppealService {
	@Autowired
	private AppealDao appealDao;
	@Autowired
	private AppealImgDao appealImgDao;
	@Autowired
	private PersonInfoDao personInfoDao;
	@Autowired
	private HelpDao helpDao;


	@Override
	@Transactional
	public List<Appeal> getAppealList(Appeal appealCondition, int pageIndex, int pageSize, String sort, String order)
	{
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		return appealDao.queryAppealListFY(appealCondition, rowIndex, pageSize,sort,order);
	}

	@Override
	public List<Appeal> getNearbyAppealList(float maxlat, float minlat, float maxlng, float minlng,
			String appealTitle) {
		List<Appeal> appealList = appealDao.queryNearbyAppealList(maxlat, minlat, maxlng, minlng, appealTitle);
		if (appealList != null) {
			Date today = new Date();
			//调用迭代器访问列表
			Iterator<Appeal> iter = appealList.iterator();
			while (iter.hasNext()) {
				Appeal value = iter.next();
				List<AppealImg> appealImgList = appealImgDao.getAppealImgList(value.getAppealId());
				value.setAppealImgList(appealImgList);
				if (value.getEndTime().getTime() < today.getTime()) {// 去除已过时失效的求助
					iter.remove();
				}
			}
		}
		return appealList;
	}

	@Override
	public Appeal getByAppealId(Long appealId){
		Appeal appeal = appealDao.queryByAppealId(appealId);
		return appeal;
	}

	@Override
	public AppealImg uploadImg(Long appealId, ImageHolder appealImg, Long userId)
	{
		Appeal appeal = appealDao.queryByAppealId(appealId);
		if (appealImg != null && appealImg.getImage() != null && appealImg.getImageName() != null
					&& !"".equals(appealImg.getImageName())) {
			addAppealImg(appealId, appealImg);
		}
		AppealImg aImg=appealImgDao.getAppealImg(appealId);
		return aImg;
	}

	private void addAppealImg(long appealId, ImageHolder appealImgHolder) {
		// 获取图片存储路径，这里直接存放到相应求助的文件夹底下
		String dest = PathUtil.getAppealImgPath(appealId);
		String imgAddr = ImageUtil.generateNormalImg(appealImgHolder, dest);
		imgAddr=imgAddr.replace("\\","/");
		AppealImg appealImg = new AppealImg();
		appealImg.setImgAddr(imgAddr);
		appealImg.setAppealId(appealId);
		appealImg.setCreateTime(new Date());
		appealImgDao.insertAppealImg(appealImg);
	}

	@Override
	public Appeal addAppeal(Appeal appeal) {
		Long userId = appeal.getUserId();
		PersonInfo personInfo = personInfoDao.queryPersonInfoByUserId(userId);
		Long souCoin = personInfo.getSouCoin();
		// 给求助信息赋初始值
		appeal.setAppealStatus(0);
		// 添加求助信息
		appealDao.insertAppeal(appeal);
		return appeal;
	}

	@Override
	public Appeal modifyAppeal(Appeal appeal){
		appealDao.updateAppeal(appeal);
		return appeal;
	}

	@Override
	@Transactional
	public Appeal completeAppeal(Long appealId, Long helpId, Long appealUserId)
	{
		Long souCoin = 0l;
		Appeal appeal = appealDao.queryByAppealId(appealId);
		souCoin = appeal.getSouCoin();
		PersonInfo appealPersonInfo = personInfoDao.queryPersonInfoByUserId(appealUserId);
		Long appealerSouCoin = appealPersonInfo.getSouCoin();
		Help help = helpDao.queryByHelpId(helpId);
		Long helpUserId = help.getUserId();
		PersonInfo helpPersonInfo = personInfoDao.queryPersonInfoByUserId(helpUserId);
		appealPersonInfo.setSouCoin(appealerSouCoin - souCoin);
		personInfoDao.updatePersonInfo(appealPersonInfo);
		Long helperSouCoin = helpPersonInfo.getSouCoin();
		helpPersonInfo.setSouCoin(helperSouCoin + souCoin);
		personInfoDao.updatePersonInfo(helpPersonInfo);
		appeal.setAppealStatus(2);
		appealDao.updateAppeal(appeal);
		help.setHelpStatus(2);
		help.setAllCoin(souCoin);
		helpDao.updateHelp(help);
		return appeal;
	}

	@Override
	public void cancelAppeal(Long userId, Long appealId) {
		Appeal appeal = appealDao.queryByAppealId(appealId);
		appeal.setAppealStatus(3);
		appealDao.updateAppeal(appeal);
	}


	@Override
	public List<AppealImg> getAppealImgList(AppealImg appealImg, int pageIndex, int pageSize, String sort, String order) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的商铺图片列表
		List<AppealImg> appealImgList = appealImgDao.queryAppealImgList(appealImg, rowIndex, pageSize,sort,order);
		return appealImgList;
	}

	@Override
	public void delAppealImg(Long appealImgId) {
		if (appealImgId != null) {
			AppealImg appealImg = appealImgDao.getAppealImg(appealImgId);
			if (appealImg != null) {
				ImageUtil.deleteFileOrPath(appealImg.getImgAddr());
				appealImgDao.deleteAppealImgById(appealImgId);
			}
			else{
			}
		} else{
		}
	}

	@Override
	public AppealImg createAppealImg(Long appealId, ImageHolder appealImgHolder)
	{
		if (appealId != null) {
			addAppealImg(appealId, appealImgHolder);
			}
		AppealImg appealImg=new AppealImg();
		appealImg=appealImgDao.getAppealImg(appealId);
		return appealImg;
	}

}
