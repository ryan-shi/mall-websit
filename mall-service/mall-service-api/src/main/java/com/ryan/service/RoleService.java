package com.ryan.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ryan.dto.RoleDTO;

public interface RoleService {
	public Map<String, Object> pageRoleList(Integer page,Integer length,String searchVal, String orderDir,String orderCol);
	
	public Set<String> findDistinctNeedRoles(String url);
	
	public List<RoleDTO> findAll();
	
	public Set<RoleDTO> findRoleIn(Long[] ids);
	
	public RoleDTO findByPrimaryKey(Long id);

	public RoleDTO save(RoleDTO roleDTO);
	
	public void deleteByPrimaryKey(Long id);
}
