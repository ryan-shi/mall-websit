package com.ryan.dto;

public class CartItem {
	private Long skuId;
	private Long productId;
	private String productName;
	private int count;

	public CartItem(Long skuId, Long productId, String productName, int count) {
		super();
		this.skuId = skuId;
		this.productId = productId;
		this.productName = productName;
		this.count = count;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "CartItem [skuId=" + skuId + ", productId=" + productId + ", productName=" + productName + ", count="
				+ count + "]";
	}
}
