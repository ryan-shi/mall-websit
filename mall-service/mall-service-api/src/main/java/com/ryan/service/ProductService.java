package com.ryan.service;

import java.util.Map;

import com.ryan.dto.ProductDTO;

public interface ProductService {

	public Map<String, Object> pageProductList(Integer page,Integer length,String searchVal, String orderDir,String orderCol);

	ProductDTO save(ProductDTO productDTO);

	ProductDTO findByPrimaryKey(Long id);

	void deleteByPrimaryKey(Long id);
	
}
