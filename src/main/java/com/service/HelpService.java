package com.service;


import com.entity.Help;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public interface HelpService {
	/**
	 * 根据helpCondition分页返回相应帮助列表
	 *
	 */
	List<Help> getHelpListFY(Help helpCondition, Date startTime, Date endTime,
								int pageIndex, int pageSize, String sort, String order);

	/**
	 * 根据helpCondition获取相应HelpList
	 *
	 */
	List<Help> getHelpList(Help helpCondition);

	/**
	 * 通过帮助ID获取帮助信息
	 *
	 */
	Help getByHelpId(long helpId);

	/**
	 * 更新帮助信息，主要是更新帮助状态以及添加求助者评价
	 *
	 */
	Help modifyHelp(Help help) ;

	/**
	 * 添加帮助
	 *
	 */
	Help addHelp(Help help);

	/**
	 * 选择帮助者
	 */
	Help selectHelp(Long helpId, Long appealId, Long appealUserId);

	/**
	 * 追赏金
	 *
	 */
	Help additionSouCoin(Long helpId, Long appealUserId, Long additionSouCoin);
}
