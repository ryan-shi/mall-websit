package com.ryan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.domain.Catalog;

import java.lang.Boolean;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

	List<Catalog> findByParentId(Long parent);
	
	List<Catalog> findByHasChildren(Boolean haschildren);
}
