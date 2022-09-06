package com.dao;


import com.entity.Help;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface HelpDao {


	/**
	 * 根据查询条件分页返回帮助信息
	 * 可输入的条件有：求助ID，帮助者用户ID,帮助状态，指定日期范围，搜币（大于搜币）
	 * @param rowIndex 从第几行开始取数据
	 * @param pageSize 返回的条数
	 */
	List<Help> queryHelpListFY(@Param("helpCondition") Help helpCondition, @Param("startTime") Date startTime,
                               @Param("endTime") Date endTime, @Param("rowIndex") int rowIndex,
							   @Param("pageSize") int pageSize,
							   @Param("sort") String sort, @Param("order") String order);
	/**
	 * 查询帮助，可输入条件有：求助ID，帮助者用户Id，帮助状态
	 */
	List<Help> queryHelpList(@Param("helpCondition") Help help);


	/**
	 * 通过help id查询帮助
	 *
	 */
	Help queryByHelpId(long helpId);

	/**
	 * 添加帮助
	 *
	 */
	void insertHelp(Help help);

	/**
	 * 更新帮助信息（主要是更新帮助状态以及求助者的评价）
	 *
	 */
	void updateHelp(Help help);
}
