package com.ryan.service;

import java.util.List;

import com.ryan.dto.CatalogDTO;

public interface CatalogService {
	List<CatalogDTO> findByParentId(Long parent);
	
	CatalogDTO findByPrimaryKey(Long id);
	
	CatalogDTO save(CatalogDTO catalogDTO);
	
	void deleteByPrimaryKey(Long id);

	List<CatalogDTO> findByHasChildren(boolean haschildren);
	
	List<CatalogDTO> findFirstLevelCatalog();
}
