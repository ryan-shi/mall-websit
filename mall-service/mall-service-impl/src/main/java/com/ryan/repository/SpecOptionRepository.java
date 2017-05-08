package com.ryan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryan.domain.SpecOption;

import java.util.List;

public interface SpecOptionRepository extends JpaRepository<SpecOption, Long> {
	List<SpecOption> findBySpecId(Long specId);
}
