package com.ryan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ryan.domain.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>,JpaSpecificationExecutor<Advertisement> {

}
