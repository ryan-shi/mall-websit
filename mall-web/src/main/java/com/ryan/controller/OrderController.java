package com.ryan.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ryan.dto.AddressDTO;
import com.ryan.dto.Cart;
import com.ryan.dto.CartItem;
import com.ryan.dto.OrderDTO;
import com.ryan.dto.OrderItemDTO;
import com.ryan.dto.UserDTO;
import com.ryan.service.AddressService;
import com.ryan.service.OrderService;
import com.ryan.service.UserService;

@Controller
@RequestMapping("/order")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	OrderService orderService;
	@Autowired
	UserService userService;
	@Autowired
	AddressService addressService;

	@GetMapping("/fillOrderMessage")
	public String fillOrderMessage() {
		return "order/fillOrderMessage";
	}

	@PostMapping("/makeOrder")
	public String makeOrder(Principal user, HttpSession session, Model model) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setCreateTime(new Date());
		Cart cart = (Cart) session.getAttribute("cart");
		String orderId = String.valueOf(System.currentTimeMillis());
		log.info("订单号：{}", orderId);
		log.info("user {}", user.getName());
		UserDTO userDTO = userService.findByUsername(user.getName(), 1);
		orderDTO.setUserDTO(userDTO);
		AddressDTO addressDTO = addressService.findByPrimaryKey(new Long(1));
		log.info("address:{}", addressDTO.getId());
		orderDTO.setAddressDTO(addressDTO);
		orderDTO.setOrderId(orderId);
		orderDTO.setPrice(cart.getAllPrice());

		List<CartItem> cartItems = cart.getCartItems();
		List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
		for (CartItem cartItem : cartItems) {
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			orderItemDTO.setSkuId(cartItem.getSkuId());
			orderItemDTO.setSkuName(cartItem.getSkuName());
			orderItemDTO.setPrice(cartItem.getSkuNowPrice());
			orderItemDTO.setNum(cartItem.getCount());
			orderItemDTOs.add(orderItemDTO);
		}
		orderDTO.setOrderItemDTOs(orderItemDTOs);
		log.info("OrderItemDTO", orderDTO.getOrderItemDTOs().size());
		OrderDTO savedOrderDTO = orderService.save(orderDTO);
		model.addAttribute("order", savedOrderDTO);
		return "order/payOrder";
	}
}
