package com.ryan.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ryan.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	@Query("from Product a where a.status= ?1 and a.newProduct=?2")
	List<Product> findTopNNewProductByStatus(Short status, Boolean isNew, Pageable pageable);
	
	@Query("from Product a where a.status= ?1 and a.saleProduct=?2")
	List<Product> findTopNSaleProductByStatus(Short status, Boolean isSale, Pageable pageable);
}
