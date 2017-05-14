package com.ryan.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SKUDTO implements Serializable{
	private static final long serialVersionUID = 8709314978832570511L;
	
	private Long id;
	private BigDecimal price;
	private BigDecimal priceNow;
	private Integer score;
	private Integer stock;
	private String picture;
	private String specOptionIds;
	private Date createTime;
	private Date updateTime;
	
	private ProductDTO productDTO;
	
	private List<SpecOptionDTO> specOptionDTOs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPriceNow() {
		return priceNow;
	}

	public void setPriceNow(BigDecimal priceNow) {
		this.priceNow = priceNow;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getSpecOptionIds() {
		return specOptionIds;
	}

	public void setSpecOptionIds(String specOptionIds) {
		this.specOptionIds = specOptionIds;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public List<SpecOptionDTO> getSpecOptionDTOs() {
		return specOptionDTOs;
	}

	public void setSpecOptionDTOs(List<SpecOptionDTO> specOptionDTOs) {
		this.specOptionDTOs = specOptionDTOs;
	}
	
}
