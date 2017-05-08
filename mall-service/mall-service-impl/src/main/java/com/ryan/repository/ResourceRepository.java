package com.ryan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ryan.domain.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long>,JpaSpecificationExecutor<Resource> {
	public List<Resource> findByParentId(Long parentId);
	
	public List<Resource> findByIdIn(Long[] ids);
}
