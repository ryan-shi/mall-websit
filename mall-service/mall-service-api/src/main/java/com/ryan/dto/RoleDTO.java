package com.ryan.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RoleDTO implements Serializable {
	private static final long serialVersionUID = -3120401672645009571L;
	
	private Long id;
	private String name;
	private String description;
	private Date createTime;
	private Date updateTime;

	private List<ResourceDTO> resourceDTOs;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<ResourceDTO> getResourceDTOs() {
		return resourceDTOs;
	}

	public void setResourceDTOs(List<ResourceDTO> resourceDTOs) {
		this.resourceDTOs = resourceDTOs;
	}
}
