package com.controller;

import com.entity.*;
import com.service.OrderService;
import com.service.PersonInfoService;
import com.service.SService;
import com.service.UsersInformService;
import com.util.HttpServletRequestUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/order")
@Api(value = "OrderController|对服务订单操作的控制器")
public class OrderController {
	@Autowired
	private OrderService OrderService;
	@Autowired
	private SService sService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private UsersInformService wechatAuthService;
	
		//通过userId获取订单列表 分页
		@RequestMapping(value = "/getOrderlistbyuserid", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据userID获取其所有订单信息（分页）")
		@ApiImplicitParams({
				@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1"),
				@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
				@ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int") })
		private Map<String, Object> getOrderListByUserId(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			Long userId = HttpServletRequestUtil.getLong(request, "userId");
			int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
			int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
			if(userId>0){
				try {
					List<OrderInfo> se = OrderService.getByUserId(userId, pageIndex, pageSize);
					int pageNum = pageIndex;
					modelMap.put("orderList", se);
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
				}
			}else{
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确userId！");
			}
			return modelMap;
		}


		//通过userId和订单状态获取90天内订单列表
		@RequestMapping(value = "/getOrderlistbyuo", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据userID和订单状态获取90天内的订单列表")
		@ApiImplicitParams({
				@ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1"),
				@ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态", required = true, dataType = "int", example = "0"),})
		private Map<String, Object> getOrderListByUO(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			Long userId = HttpServletRequestUtil.getLong(request, "userId");
			int orderStatus= HttpServletRequestUtil.getInt(request, "orderStatus");
			if(userId>0) {
				try {
					List<OrderInfo> se = OrderService.getOrderList2(userId, orderStatus);
					modelMap.put("orderList", se);
					modelMap.put("orderStatus", orderStatus);
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
				}
			}else{
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确userId！");
			}
			return modelMap;
		}



		/**
		 * 根据Service_id返回订单列表
		 *
		 * @param request
		 * @return
		 */
		@RequestMapping(value = "/searchorderbyserviceid", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据ServiceId获取服务订单信息")
		@ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务ID", required = true, dataType = "Long", example = "3"),
							@ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "int"),
							@ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "int")} )

		private Map<String, Object> searchOrderByServiceId(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			List<OrderInfo> Order = null;
			// 从请求中获取ServiceId
			long ServiceId = HttpServletRequestUtil.getLong(request, "serviceId");
			int pageIndex =HttpServletRequestUtil.getInt(request,"pageIndex");
			int pageSize =HttpServletRequestUtil.getInt(request,"pageSize");

			if (ServiceId > 0) {
				try {
					// 根据Id获取订单实例
					Order = OrderService.getByServiceId(ServiceId,pageIndex,pageSize);

				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}
				if (Order != null) {
					modelMap.put("orderList", Order);
					modelMap.put("success", true);
				} else {
					modelMap.put("orderList", new ArrayList<OrderInfo>());
					modelMap.put("success", true);
				}
				return modelMap;
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确serviceId！");
				return modelMap;
			}
		}


		/**
		 * 根据订单id返回订单信息
		 *
		 * @param request
		 * @return
		 */
		@RequestMapping(value = "/searchorderbyid", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据OrderId获取服务订单信息")
		@ApiImplicitParam(paramType = "query", name = "OrderId", value = "订单ID", required = true, dataType = "Long", example = "3")
		private Map<String, Object> searchOrderById(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			OrderInfo Order = null;
			// 从请求中获取OrderId
			long OrderId = HttpServletRequestUtil.getLong(request, "OrderId");
			if (OrderId > 0) {
				try {
					// 根据Id获取订单实例
					Order = OrderService.getByOrderId(OrderId);

				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}
				if (Order != null) {
					modelMap.put("order", Order);
					modelMap.put("success", true);
				} else {
					modelMap.put("rows", new ArrayList<OrderInfo>());
					modelMap.put("success", true);
				}
				return modelMap;
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确订单Id！");
				return modelMap;
			}
		}


		//通过shopId orderStatus获取订单列表
		@RequestMapping(value = "/getorderlistbyshopId", method = RequestMethod.GET)
		@ResponseBody
		@ApiOperation(value = "根据shopId orderStatus获取其近三个月订单信息")
		@ApiImplicitParams({
				@ApiImplicitParam(paramType = "query", name = "shopId", value = "店铺ID", required = true, dataType = "Long", example = "1"),
				@ApiImplicitParam(paramType = "query", name = "orderStatus", value = "订单状态", required = true, dataType = "int", example = "0"),})
		private Map<String, Object> getOrderListByShopId(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			int orderStatus= HttpServletRequestUtil.getInt(request, "orderStatus");
			if (shopId > 0) {
				try {
					List<ServiceInfo> se = sService.getByShopId2(shopId);
					List<OrderInfo> orderlist=new ArrayList<OrderInfo>();
					List<ServiceInfo> servicelist=se;
					for(int i=0;i<se.size();i++)
					{
						List<OrderInfo> ore = OrderService.getByServiceId2(servicelist.get(i).getServiceId(),orderStatus);
						orderlist.addAll(ore);
					}
					Collections.sort(orderlist , (OrderInfo b1, OrderInfo b2) -> b2.getCreateTime().compareTo(b1.getCreateTime()));
					modelMap.put("orderList",orderlist );
					modelMap.put("orderNum",orderlist.size() );
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
				}
			}else{
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入正确shopId!");
			}

			return modelMap;
		}


		//添加订单
		@RequestMapping(value = "/addorder", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "添加订单信息")
		private Map<String, Object> addOrder(
				@RequestBody @ApiParam(name = "Order", value = "传入json格式,要传orderId", required = true)OrderInfo Order, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();


			// 空值判断
			if (Order != null && Order.getOrderId() != null) {
				try {
					//添加订单
					Order.setCreateTime(LocalDateTime.now());
					OrderInfo ae = OrderService.addOrder(Order);
					modelMap.put("success", true);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}

			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入订单信息!");
			}
			return modelMap;
		}


		//更新订单
		@RequestMapping(value = "/changeorderstatus", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "修改订单信息")
		private Map<String, Object> changeOrderStatus(
				@RequestBody @ApiParam(name = "Order", value = "传入json格式,要传orderId", required = true)OrderInfo Order, HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			System.out.println(Order.toString());
			// 空值判断
			if (Order != null && Order.getOrderId() != null) {
				try {
					Order.setOverTime(LocalDateTime.now());
					//更新订单
					OrderInfo ae = OrderService.modifyOrder(Order);
					modelMap.put("success", true);
					modelMap.put("order", Order);
				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}

			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入订单信息!");
			}
			return modelMap;
		}


		//删除订单
		@RequestMapping(value = "/deleteorder", method = RequestMethod.POST)
		@ResponseBody
		@ApiOperation(value = "删除订单信息")
		@ApiImplicitParam(paramType = "query", name = "OrderId", value = "订单ID", required = true, dataType = "Long", example = "3")
		private Map<String, Object> deleteOrder(HttpServletRequest request) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			// 从请求中获取OrderId
			long OrderId = HttpServletRequestUtil.getLong(request, "OrderId");
				if (OrderId>0) {
					try {
						//删除订单
						OrderService.deleteOrder(OrderId);
						modelMap.put("success", true);
					} catch (Exception e) {
						modelMap.put("success", false);
						modelMap.put("errMsg", e.toString());
						return modelMap;
					}
	
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "请输入正确的orderId！");
				}
			return modelMap;
		}


}

