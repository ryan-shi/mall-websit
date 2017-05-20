package com.ryan.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.domain.Address;

public interface AddressRespository extends JpaRepository<Address, Long> {
	
}
