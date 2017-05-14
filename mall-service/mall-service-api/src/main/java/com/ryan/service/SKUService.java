package com.ryan.service;

import java.util.List;
import java.util.Map;

import com.ryan.dto.SKUDTO;

public interface SKUService {

	public Map<String, Object> pageSKUList(Integer page, Integer length, String searchVal, String orderDir,String orderCol);

	List<SKUDTO> findBySpecOptionIds(String specOptionIds);

	SKUDTO save(SKUDTO skuDTO);

	SKUDTO findByPrimaryKey(Long id);

	void deleteByPrimaryKey(Long id);

	List<SKUDTO> findByProductId(Long id);
}
