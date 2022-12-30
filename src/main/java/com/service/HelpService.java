package com.service;


import com.dto.HelpExecution;
import com.entity.Help;
import com.exceptions.HelpOperationException;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public interface HelpService {
	/**
	 * 根据helpCondition分页返回相应帮助列表
	 *
	 * @param helpCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public HelpExecution getHelpListFY(Help helpCondition, Date startTime, Date endTime, int pageIndex, int pageSize, String sort, String order);

	/**
	 * 根据helpCondition获取相应HelpList
	 * @return
	 */
	public HelpExecution getHelpList(Help helpCondition);

	/**
	 * 通过帮助ID获取帮助信息
	 *
	 * @param helpId
	 * @return
	 */
	Help getByHelpId(long helpId);

	/**
	 * 更新帮助信息，主要是更新帮助状态以及添加求助者评价
	 *
	 * @param help
	 * @return
	 * @throws HelpOperationException
	 */
	HelpExecution modifyHelp(Help help) throws HelpOperationException;

	/**
	 * 添加帮助
	 *
	 * @param help
	 * @return
	 * @throws HelpOperationException
	 */
	HelpExecution addHelp(Help help) throws HelpOperationException;

	/**
	 * 选择帮助者
	 *
	 * @param helpId
	 * @param appealId
	 * @throws HelpOperationException
	 */
	HelpExecution selectHelp(Long helpId, Long appealId, Long appealUserId) throws HelpOperationException;

	/**
	 * 追赏金
	 *
	 * @param helpId
	 * @param appealUserId
	 * @param additionSouCoin
	 */
	HelpExecution additionSouCoin(Long helpId, Long appealUserId, Long additionSouCoin) throws HelpOperationException;
}