package com.ryan.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ryan.dto.Cart;
import com.ryan.dto.CartItem;
import com.ryan.dto.ProductDTO;
import com.ryan.dto.SKUDTO;
import com.ryan.service.SKUService;

@Controller
@RequestMapping("/cart")
public class CartController {

	private static final Logger log = LoggerFactory.getLogger(CartController.class);

	@Autowired
	SKUService skuService;

	@RequestMapping("/{id}")
	public String addToCart(@PathVariable Long id, HttpServletRequest request, Model model) {
		log.info("sku id: {}", id);
		SKUDTO skuDTO = skuService.findByPrimaryKey(id);
		HttpSession session = request.getSession();
		Object cartObj = session.getAttribute("cart");
		if (cartObj == null) {
			log.info("购物车为空,添加sku id: {},name: {}", id, skuDTO.getProductDTO().getName());
			ProductDTO productDTO = skuDTO.getProductDTO();
			List<CartItem> cartItems = new ArrayList<>();
			CartItem cartItem = new CartItem(id, productDTO.getId(), productDTO.getName(), 1);
			cartItems.add(cartItem);
			Cart cart = new Cart(cartItems, 1);
			session.setAttribute("cart", cart);
		} else {
			log.info("购物车不为空！");
			Cart cart = (Cart) cartObj;
			CartItem cartItem = Cart.cartContainSku(cart, id);
			log.info("从购物查找到的要添加的item：{}", cartItem);
			if (cartItem == null) {
				log.info("{},{},此商品之前没有！", id, skuDTO.getProductDTO().getName());
				ProductDTO productDTO = skuDTO.getProductDTO();
				CartItem newCartItem = new CartItem(id, productDTO.getId(), productDTO.getName(), 1);
				cart.getCartItems().add(newCartItem);
				cart.setAllCount(cart.getAllCount() + 1);
			} else {
				int count = cartItem.getCount();
				log.info("{},{},此商品存在！ count: {}", id, skuDTO.getProductDTO().getName(), count);
				cartItem.setCount(count + 1);
				cart.setAllCount(cart.getAllCount() + 1);
			}
			session.setAttribute("cart", cart);
		}
		log.info("sku:{}", skuDTO);
		model.addAttribute("sku", skuDTO);
		return "cart/index";
	}
}
