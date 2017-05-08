package com.ryan.service;

import java.util.Map;

import com.ryan.dto.UserDTO;

public interface UserService {
	public Map<String, Object> pageUserList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol);

	public UserDTO findByUsername(String username, Integer type);

	public UserDTO findByPrimaryKey(Long id);

	public UserDTO save(UserDTO userDTO);

	public boolean updateUser(UserDTO userDTO);

	public void deleteByPrimaryKey(Long id);
}
