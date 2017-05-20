package com.ryan.service;

import java.util.List;

import com.ryan.dto.AddressDTO;

public interface AddressService {

	public AddressDTO findByPrimaryKey(Long id);

	public List<AddressDTO> findByUserId(Long id);
}
