package com.ryan.service;

import java.util.List;
import java.util.Map;

import com.ryan.dto.DepartmentDTO;

public interface DepartmentService {
	public Map<String, Object> pageDepartmentList(Integer page,Integer length,String searchVal, String orderDir,String orderCol);
	
	public List<DepartmentDTO> findAll();
	
	public DepartmentDTO findByPrimaryKey(Long id);
	
	public DepartmentDTO save(DepartmentDTO departmentDTO);
	
	public void deleteByPrimaryKey(Long id);
}
