package com.ryan.service;

import java.util.List;

import com.ryan.dto.SpecOptionDTO;

public interface SpecOptionService {

	List<SpecOptionDTO> findBySpecId(Long specId);

	SpecOptionDTO findByPrimaryKey(Long id);

	SpecOptionDTO save(SpecOptionDTO specOptionDTO);

	void deleteByPrimaryKey(Long id);

}
