package com.ryan.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ryan.domain.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>,JpaSpecificationExecutor<Advertisement> {
	@Query("from Advertisement a where a.position= ?1")
	List<Advertisement> findTopNByPosition(Integer position,Pageable pageable);
}
