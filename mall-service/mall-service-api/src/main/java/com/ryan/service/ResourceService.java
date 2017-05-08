package com.ryan.service;

import java.util.List;

import com.ryan.dto.ResourceDTO;

public interface ResourceService {

	public List<ResourceDTO> findByParentId(Long id);
	
	public ResourceDTO findByPrimaryKey(Long id);
	
	public ResourceDTO save(ResourceDTO resourceDTO);
	
	public void deleteByPrimaryKey(Long id);
	
	public List<ResourceDTO> findByIdIn(Long[] ids);
}
