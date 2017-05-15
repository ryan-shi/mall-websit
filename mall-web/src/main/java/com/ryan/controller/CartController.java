package com.ryan.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ryan.dto.SKUDTO;
import com.ryan.service.SKUService;

@Controller
@RequestMapping("/cart")
public class CartController {

	private static final Logger log = LoggerFactory.getLogger(CartController.class);

	@Autowired
	SKUService skuService;

	@SuppressWarnings("unchecked")
	@RequestMapping("/{id}")
	public String addToCart(@PathVariable Long id, HttpServletRequest request, Model model) {
		log.info("sku id: {}", id);
		SKUDTO skuDTO = skuService.findByPrimaryKey(id);
		HttpSession session = request.getSession();
		Object cartObj = session.getAttribute("cart");
		int allcount = 0;
		if (cartObj == null) {
			log.info("购物车为空！");
			Map<Long, Integer> cart = new HashMap<>();
			cart.put(id, 1);
			session.setAttribute("cart", cart);
			session.setAttribute("cart", cart);
		} else {
			log.info("购物车不为空！");
			Map<Long, Integer> cart = (Map<Long, Integer>) cartObj;
			if (cart.get(id) == null) {
				log.info("{},{},此商品之前没有！", id, skuDTO.getProductDTO().getName());
				cart.put(id, 1);
			} else {
				int count = cart.get(id);
				log.info("{},{},此商品存在！ count: {}", id, skuDTO.getProductDTO().getName(), count);
				cart.put(id, ++count);
				session.setAttribute("cart", cart);
			}
		}
		log.info("sku:{}", skuDTO);
		model.addAttribute("sku", skuDTO);
		return "cart/index";
	}
}
