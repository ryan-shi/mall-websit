package com.ryan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ryan.domain.SKU;

import java.lang.String;
import java.util.List;

public interface SKURepository extends JpaRepository<SKU, Long>, JpaSpecificationExecutor<SKU> {
	List<SKU> findBySpecOptionIds(String specOptionIds);
	
	List<SKU> findByProductId(Long id);
}
