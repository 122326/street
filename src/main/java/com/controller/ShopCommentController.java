package com.controller;


import com.entity.*;
import com.service.OrderService;
import com.service.SService;
import com.service.ShopCommentService;
import com.util.HttpServletRequestUtil;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopcomment")
@Api(value = "ShopCommentController|对服务评论操作的控制器")
public class ShopCommentController {
		@Autowired
		private ShopCommentService shopCommentService;

		@Autowired
		private SService sService;
		@Autowired
		private OrderService OrderService;
		private static final Logger log = LogManager.getLogger(ShopCommentController.class);



		//根据查询条件获取评论列表 分页
		@RequestMapping(value = "/listShopComment", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据shopID、orderId、userId、commentContent获取其所有服务评论信息（分页）")
		@ApiImplicitParams({
				@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "3"),
				@ApiImplicitParam(paramType = "query", name = "orderId", value = "订单ID", required = false, dataType = "Long", example = "1"),
				@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = false, dataType = "Long", example = "1"),
				@ApiImplicitParam(paramType = "query", name = "commentContent", value = "评论内容", required = false, dataType = "String", example = "测试shopComment内容"),
				@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
				@ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int") })
		private Map<String, Object> listShopComment(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			List<ShopComment> se = null;
			// 获取分页信息
			int pageIndex = HttpServletRequestUtil.getInt(request, "page");
			int pageSize = HttpServletRequestUtil.getInt(request, "rows");
			// 空值判断
			if (pageIndex > 0 && pageSize > 0) {
				ShopComment shopCommentCondition = new ShopComment();
				long shopId=HttpServletRequestUtil.getLong(request, "shopId");
				if(shopId>0) {
					shopCommentCondition.setShopId(shopId);
					long orderId = HttpServletRequestUtil.getLong(request, "orderId");
					if (orderId > 0)
						shopCommentCondition.setOrderId(orderId);
					long userId = HttpServletRequestUtil.getLong(request, "userId");
					if (userId > 0)
						shopCommentCondition.setUserId(userId);
					String shopCommentContent = HttpServletRequestUtil.getString(request, "commentContent");
					if (shopCommentContent != null) {
						try {
							// 若传入评论内容，则将评论内容解码后添加到查询条件里，进行模糊查询
							shopCommentCondition.setCommentContent(URLDecoder.decode(shopCommentContent, "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							modelMap.put("success", false);
							modelMap.put("errMsg", e.toString());
						}
					}
					try {
						// 根据查询条件分页返回评论列表
						se = shopCommentService.getShopCommentList(shopCommentCondition, pageIndex, pageSize, null, null);
					} catch (Exception e) {
						modelMap.put("success", false);
						modelMap.put("errMsg", e.toString());
						return modelMap;
					}
					if (se != null) {
						modelMap.put("commentList", se);
						modelMap.put("success", true);
					} else {
						// 取出数据为空，也返回new list以使得前端不出错
						modelMap.put("commentList", new ArrayList<ShopComment>());
						modelMap.put("success", true);
					}
					return modelMap;
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", "请输入正确的店铺Id！");
					return modelMap;
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确的分页信息！");
				return modelMap;
			}
		}


		/**
		 * 根据评论id返回评论信息
		 *
		 * @param request
		 * @return
		 */
		@RequestMapping(value = "/searchshopcommentbyid", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据shopCommentId获取服务评论信息")
		@ApiImplicitParam(paramType = "query", name = "shopCommentId", value = "服务评价ID", required = true, dataType = "Long", example = "3")
		private Map<String, Object> searchShopCommentById(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			ShopComment shopComment = null;
			// 从请求中获取shopCommentId
			long shopCommentId = HttpServletRequestUtil.getLong(request, "shopCommentId");
			if (shopCommentId > 0) {
				try {
					// 根据Id获取评论实例
					shopComment = shopCommentService.getByShopCommentId(shopCommentId);

				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}
				if (shopComment != null) {
					modelMap.put("shopComment", shopComment);
					modelMap.put("success", true);
				} else {
					modelMap.put("shopComment", new ArrayList<ShopComment>());
					modelMap.put("success", true);
				}
				return modelMap;
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确的CommentId!");
				return modelMap;
			}
		}



		//添加评论
		@RequestMapping(value = "/addshopcomment", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "添加服务评价信息")
		private Map<String, Object> addShopComment(
				@RequestBody @ApiParam(name = "ShopComment", value = "传入json格式,要传orderId", required = true)ShopComment shopComment, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			// 空值判断
			if (shopComment != null) {
				try {
					//添加评论
					shopCommentService.addShopComment(shopComment);
					OrderInfo order=OrderService.getByOrderId(shopComment.getOrderId());
					order.setOrderStatus(2);
					try {
						//更新订单
						OrderInfo a = OrderService.modifyOrder(order);
					} catch (Exception e) {
						modelMap.put("success", false);
						modelMap.put("errMsg", e.toString());
						return modelMap;
					}
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}

			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入评论信息！");
			}
			return modelMap;
		}


		//更新评论
		@RequestMapping(value = "/modifyshopcomment", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "修改服务评价信息")
		private Map<String, Object> modifyShopComment(
                @RequestBody @ApiParam(name = "ShopComment", value = "传入json格式,要传shopCommentId", required = true)ShopComment shopComment, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			System.out.println(shopComment.toString());
			// 空值判断
			if (shopComment != null && shopComment.getShopCommentId() != null) {
				try {
					//更新评论
					shopCommentService.modifyShopComment(shopComment);
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}

			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入评论信息");
			}
			return modelMap;
		}



		//删除评论
		@RequestMapping(value = "/deleteshopcomment", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "删除服务评价信息")
		@ApiImplicitParam(paramType = "query", name = "shopCommentId", value = "服务评价ID", required = true, dataType = "Long", example = "3")
		private Map<String, Object> deleteShopComment(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			// 从请求中获取shopCommentId
			long shopCommentId = HttpServletRequestUtil.getLong(request, "shopCommentId");
			if (shopCommentId > 0) {
				try {

					//删除评论
					shopCommentService.deleteShopComment(shopCommentId);
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}
			}
			else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确的shopCommentId！");
				return modelMap;
			}
			return modelMap;
		}


}
