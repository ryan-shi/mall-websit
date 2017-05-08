package com.ryan.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SpecDTO implements Serializable{
	private static final long serialVersionUID = 8865423728821618484L;
	
	private Long id;
	private String name;
	private String code;
	private Integer sort;
	private Date createTime;
	private Date updateTime;
	
	private List<SpecOptionDTO> specOptionDTOs;

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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	public List<SpecOptionDTO> getSpecOptionDTOs() {
		return specOptionDTOs;
	}

	public void setSpecOptionDTOs(List<SpecOptionDTO> specOptionDTOs) {
		this.specOptionDTOs = specOptionDTOs;
	}

	public CatalogDTO getCatalogDTO() {
		return catalogDTO;
	}

	public void setCatalogDTO(CatalogDTO catalogDTO) {
		this.catalogDTO = catalogDTO;
	}	
}