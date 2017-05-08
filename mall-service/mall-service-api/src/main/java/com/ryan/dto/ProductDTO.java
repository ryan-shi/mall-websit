package com.ryan.dto;

import java.io.Serializable;
import java.util.Date;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 2367330993254629141L;
	
	private Long id;
	private String name;
	private String code;
	private String introduce;
	private Short status = 1;
	private Integer sellCount = 0;
	private Integer commentCount = 0;
	private Date createTime;
	private Date updateTime;

	private CatalogDTO catalogDTO;
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Integer getSellCount() {
		return sellCount;
	}

	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
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

	public CatalogDTO getCatalogDTO() {
		return catalogDTO;
	}

	public void setCatalogDTO(CatalogDTO catalogDTO) {
		this.catalogDTO = catalogDTO;
	}
}
