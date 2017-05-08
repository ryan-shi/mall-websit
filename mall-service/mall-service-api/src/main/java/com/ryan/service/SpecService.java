package com.ryan.service;

import java.util.List;
import com.ryan.dto.SpecDTO;

public interface SpecService {
	SpecDTO save(SpecDTO specDTO);
	
	SpecDTO findByPrimaryKey(Long id);

	void deleteByPrimaryKey(Long id);

	List<SpecDTO> findByCatalogOrderBySortAsc(Long catalogId);
}
