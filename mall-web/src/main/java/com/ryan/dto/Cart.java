package com.ryan.dto;

import java.math.BigDecimal;
import java.util.List;

public class Cart {
	private List<CartItem> cartItems;
	private int allCount;
	private BigDecimal allPrice;

	public Cart(List<CartItem> cartItems, int allCount, BigDecimal allPrice) {
		super();
		this.cartItems = cartItems;
		this.allCount = allCount;
		this.allPrice = allPrice;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public BigDecimal getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(BigDecimal allPrice) {
		this.allPrice = allPrice;
	}

	public static CartItem cartContainSku(Cart cart, Long id) {
		List<CartItem> cartItems = cart.getCartItems();
		for (CartItem cartItem : cartItems) {
			if (cartItem.getSkuId() == id) {
				return cartItem;
			}
		}
		return null;
	}
}
