package com.ryan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ryan.domain.Department;
import com.ryan.domain.Role;
import com.ryan.domain.User;
import com.ryan.dto.DepartmentDTO;
import com.ryan.dto.RoleDTO;
import com.ryan.dto.UserDTO;
import com.ryan.repository.UserRepository;
import com.ryan.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 后台分页
	 */
	@Override
	public Map<String, Object> pageUserList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol) {

		Pageable pageable = new PageRequest(page, length,
				new Sort(orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderCol));

		Specification<User> userSp = new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				Predicate backendTypeP = criteriaBuilder.equal(root.get("type"), 0);
				if (!searchVal.equals("")) {
					Predicate idP = criteriaBuilder.like(root.get("id").as(String.class), "%" + searchVal + "%");
					Predicate usernameP = criteriaBuilder.like(root.get("username"), "%" + searchVal + "%");
					Predicate emailP = criteriaBuilder.like(root.get("email"), "%" + searchVal + "%");
					Predicate timeP = criteriaBuilder.like(root.get("createTime").as(String.class),
							"%" + searchVal + "%");
					return criteriaBuilder.and(backendTypeP,
							criteriaBuilder.or(timeP, criteriaBuilder.or(emailP, criteriaBuilder.or(idP, usernameP))));
				}
				return criteriaBuilder.and(backendTypeP);
			}
		};
		Page<User> userPage = userRepository.findAll(userSp, pageable);
		Map<String, Object> result = new HashMap<>();
		List<User> users = userPage.getContent();
		List<UserDTO> userDTOs = new ArrayList<>();
		for (User user : users) {
			userDTOs.add(userToUserDTO(user));
		}
		result.put("data", userDTOs);
		result.put("recordsTotal", userPage.getTotalElements());
		result.put("recordsFiltered", userPage.getTotalElements());
		return result;
	}

	@Override
	public UserDTO findByUsername(String username, Integer type) {
		User user = userRepository.findByUsernameAndType(username, type);
		UserDTO userDTO = userToUserDTO(user);
		log.info("userDTO: {}", userDTO);
		return userDTO;
	}

	@Override
	public UserDTO findByPrimaryKey(Long id) {
		User user = userRepository.findOne(id);
		UserDTO userDTO = userToUserDTO(user);
		log.info("userDTO: {}", userDTO);
		log.info("userDTO roles: {}", userDTO.getRoleDTOs());
		return userDTO;
	}

	@Override
	public UserDTO save(UserDTO userDTO) {
		User user = userDTOtoUser(userDTO);
		User savedUser = userRepository.save(user);
		return userToUserDTO(savedUser);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		userRepository.delete(id);
	}

	public UserDTO userToUserDTO(User user) {
		UserDTO userDTO = null;
		if (user != null) {
			userDTO = new UserDTO();
			BeanUtils.copyProperties(user, userDTO);

			Set<Role> roles = user.getRoles();
			Set<RoleDTO> roleDTOs = new HashSet<>();
			for (Role role : roles) {
				RoleDTO roleDTO = new RoleDTO();
				BeanUtils.copyProperties(role, roleDTO);
				roleDTOs.add(roleDTO);
			}
			userDTO.setRoleDTOs(roleDTOs);

			Department department = user.getDepartment();
			DepartmentDTO departmentDTO = new DepartmentDTO();
			if (department != null)
				BeanUtils.copyProperties(department, departmentDTO);
			userDTO.setDepartmentDTO(departmentDTO);
		}
		return userDTO;
	}

	public User userDTOtoUser(UserDTO userDTO) {
		User user = new User();
		BeanUtils.copyProperties(userDTO, user);

		Set<RoleDTO> roleDTOs = userDTO.getRoleDTOs();
		Set<Role> roles = new HashSet<>();
		for (RoleDTO roleDTO : roleDTOs) {
			Role role = new Role();
			BeanUtils.copyProperties(roleDTO, role);
			roles.add(role);
		}
		user.setRoles(roles);

		DepartmentDTO departmentDTO = userDTO.getDepartmentDTO();
		Department department = new Department();
		BeanUtils.copyProperties(departmentDTO, department);
		user.setDepartment(department);

		return user;
	}

	@Override
	public boolean updateUser(UserDTO userDTO) {
		User user = userDTOtoUser(userDTO);
		User oldUser = userRepository.findOne(user.getId());
		Set<Role> roles = oldUser.getRoles();
		user.setRoles(roles);
		User savedUser = userRepository.save(user);
		return !(savedUser == null);
	}
}
