package com.ryan.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="sku")
public class SKU implements Serializable{
	private static final long serialVersionUID = 8709314978832570511L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private BigDecimal price;
	private BigDecimal priceNow;
	private Integer score;
	private Integer stock;
	private String picture;
	@Column(name="spec_option_ids",length=100)
	private String specOptionIds;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sku_spec_option", joinColumns = { @JoinColumn(name = "sku_id") }, inverseJoinColumns = {
			@JoinColumn(name = "spec_option_id") })
	private List<SpecOption> specOptions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<SpecOption> getSpecOptions() {
		return specOptions;
	}

	public void setSpecOptions(List<SpecOption> specOptions) {
		this.specOptions = specOptions;
	}
}
