package com.ryan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.domain.Spec;

import java.util.List;

public interface SpecRepository extends JpaRepository<Spec, Long> {
	
	List<Spec> findByCatalogIdOrderBySortAsc(Long catalogId);
}
