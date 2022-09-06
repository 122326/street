package com.dao;


import com.entity.Appeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppealDao {
	/**
	 * 根据经纬度范围查找求助
	 */
	List<Appeal> queryNearbyAppealList(@Param("maxlat") float maxlat, @Param("minlat") float minlat,
                                       @Param("maxlng") float maxlng, @Param("minlng") float minlng, @Param("appealTitle") String appealTitle);

	/**
	 * 根据查询条件分页返回求助信息
	 * 可输入的条件有：求助名（模糊），求助状态，省份，城市，地区，求助者的用户ID，指定日期范围（大于开始时间，小于失效时间），搜币（大于搜币）
	 * @param rowIndex        从第几行开始取数据
	 * @param pageSize        返回的条数
	 */
	List<Appeal> queryAppealListFY(@Param("appealCondition") Appeal appealCondition, @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);
	/**
	 * 查询求助，可输入的条件有：求助名（模糊），求助状态，省份，城市，地区，求助者的用户ID
	 */
	List<Appeal> queryAppealList(@Param("appealCondition") Appeal appealCondition);


	/**
	 * 通过appeal id查询求助
	 *
	 */
	Appeal queryByAppealId(long appealId);

	/**
	 * 添加求助
	 *
	 */
	void insertAppeal(Appeal appeal);

	/**
	 * 更新求助信息
	 *
	 */
	void updateAppeal(Appeal appeal);


}
