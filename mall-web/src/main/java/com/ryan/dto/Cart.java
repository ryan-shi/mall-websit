package com.ryan.dto;

import java.util.List;

public class Cart {
	private List<CartItem> cartItems;
	private int allCount;

	public Cart(List<CartItem> cartItems, int allCount) {
		super();
		this.cartItems = cartItems;
		this.allCount = allCount;
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
