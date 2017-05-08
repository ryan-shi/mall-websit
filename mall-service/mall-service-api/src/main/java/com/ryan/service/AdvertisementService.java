package com.ryan.service;

import java.util.Map;

import com.ryan.dto.AdvertisementDTO;

public interface AdvertisementService {
	public Map<String, Object> pageAdvertisementList(Integer page,Integer length,String searchVal, String orderDir,String orderCol);
	
	public AdvertisementDTO save(AdvertisementDTO advertisementDTO);
	
	public AdvertisementDTO findByPrimaryKey(Long id);
	
	public void deleteByPrimaryKey(Long id);
}
