package com.ryan.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryan.domain.Address;
import com.ryan.domain.User;
import com.ryan.dto.AddressDTO;
import com.ryan.dto.UserDTO;
import com.ryan.repository.AddressRespository;
import com.ryan.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRespository addressRespository;

	@Override
	public AddressDTO findByPrimaryKey(Long id) {
		Address address = addressRespository.findOne(id);
		return addressToAddressDTO(address);
	}
	
	@Override
	public List<AddressDTO> findByUserId(Long id){
		return null;
	}

	public AddressDTO addressToAddressDTO(Address address){
		AddressDTO addressDTO=new AddressDTO();
		BeanUtils.copyProperties(address, addressDTO);
		User user=address.getUser();
		UserDTO userDTO=new UserDTO();
		if(user!=null)
			BeanUtils.copyProperties(user, userDTO);
		addressDTO.setUserDTO(userDTO);
		return addressDTO;
	}
}
