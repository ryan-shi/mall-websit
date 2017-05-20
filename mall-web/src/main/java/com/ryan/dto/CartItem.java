package com.ryan.dto;

import java.math.BigDecimal;

public class CartItem {
	private Long skuId;
	private Long productId;
	private String productName;
	private String skuName;
	private String skuPicture;
	private BigDecimal skuNowPrice;
	private int count;

	public CartItem(Long skuId, Long productId, String productName, String skuName, String skuPicture,
			BigDecimal skuNowPrice, int count) {
		super();
		this.skuId = skuId;
		this.productId = productId;
		this.productName = productName;
		this.skuName = skuName;
		this.skuPicture = skuPicture;
		this.skuNowPrice = skuNowPrice;
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

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuPicture() {
		return skuPicture;
	}

	public void setSkuPicture(String skuPicture) {
		this.skuPicture = skuPicture;
	}

	public BigDecimal getSkuNowPrice() {
		return skuNowPrice;
	}

	public void setSkuNowPrice(BigDecimal skuNowPrice) {
		this.skuNowPrice = skuNowPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
