package com.ryan.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CatalogDTO implements Serializable {
	private static final long serialVersionUID = 8365446900874065471L;

	private Long id;
	private Long parentId;
	private String name;
	private String description;
	private Boolean hasChildren = false;
	private Date createTime;
	private Date updateTime;

	private List<SpecDTO> specDTOs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
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

	public List<SpecDTO> getSpecDTOs() {
		return specDTOs;
	}

	public void setSpecDTOs(List<SpecDTO> specDTOs) {
		this.specDTOs = specDTOs;
	}
}
