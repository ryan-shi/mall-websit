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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

import com.ryan.domain.Resource;
import com.ryan.domain.Role;
import com.ryan.dto.ResourceDTO;
import com.ryan.dto.RoleDTO;
import com.ryan.repository.RoleRepository;
import com.ryan.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Map<String, Object> pageRoleList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol) {
		Pageable pageable = new PageRequest(page, length,
				new Sort(orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderCol));

		Specification<Role> roleSp = new Specification<Role>() {

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (!searchVal.equals("")) {
					Predicate idP = criteriaBuilder.like(root.get("id").as(String.class), "%" + searchVal + "%");
					Predicate nameP = criteriaBuilder.like(root.get("name"), "%" + searchVal + "%");
					Predicate descriptionP = criteriaBuilder.like(root.get("description"), "%" + searchVal + "%");
					Predicate timeP = criteriaBuilder.like(root.get("createTime").as(String.class),
							"%" + searchVal + "%");
					predicates.add(criteriaBuilder.or(timeP,
							criteriaBuilder.or(descriptionP, criteriaBuilder.or(idP, nameP))));
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
				return null;
			}
		};

		Page<Role> rolePage = roleRepository.findAll(roleSp, pageable);
		List<Role> roles = rolePage.getContent();
		List<RoleDTO> roleDTOs = new ArrayList<>();
		for (Role role : roles) {
			roleDTOs.add(roleToRoleDTO(role));
		}

		Map<String, Object> result = new HashMap<>();
		result.put("data", roleDTOs);
		result.put("recordsTotal", rolePage.getTotalElements());
		result.put("recordsFiltered", rolePage.getTotalElements());
		return result;
	}

	@Override
	public Set<String> findDistinctNeedRoles(String url) {
		List<Role> roles = roleRepository.findAll();
		PathMatcher pathMatcher = new AntPathMatcher();
		// 匹配所有的url，并对角色去重
		Set<String> needRoles = new HashSet<String>();
		for (Role role : roles) {
			List<Resource> resources = role.getResources();
			for (Resource resource : resources) {
				if (resource.getValue() != null) {
					String resourceValue = resource.getValue();
					String[] pathUrls = resourceValue.split(":");
					if (pathUrls.length > 1) {
						for (int i = 0; i < pathUrls.length; i++) {
							if (pathMatcher.match(pathUrls[i], url)) {
								needRoles.add(role.getName());
							}
						}
					} else {
						if (pathMatcher.match(resourceValue, url)) {
							needRoles.add(role.getName());
						}
					}
				}
			}
		}
		return needRoles;
	}

	@Override
	public List<RoleDTO> findAll() {
		List<Role> roles = roleRepository.findAll();
		List<RoleDTO> roleDTOs = new ArrayList<>();
		for (Role role : roles) {
			roleDTOs.add(roleToRoleDTO(role));
		}
		return roleDTOs;
	}

	@Override
	public RoleDTO findByPrimaryKey(Long id) {
		Role role = roleRepository.findOne(id);
		return roleToRoleDTO(role);
	}

	@Override
	public RoleDTO save(RoleDTO roleDTO) {
		Role role = roleDTOToRole(roleDTO);
		Role savedRole = roleRepository.save(role);
		return roleToRoleDTO(savedRole);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		roleRepository.delete(id);
	}

	@Override
	public Set<RoleDTO> findRoleIn(Long[] ids) {
		Set<Role> roles = roleRepository.findByIdIn(ids);
		Set<RoleDTO> roleDTOs = new HashSet<>();
		for (Role role : roles) {
			roleDTOs.add(roleToRoleDTO(role));
		}
		return roleDTOs;
	}

	public RoleDTO roleToRoleDTO(Role role) {
		RoleDTO roleDTO = new RoleDTO();
		BeanUtils.copyProperties(role, roleDTO);

		List<Resource> resources = role.getResources();
		List<ResourceDTO> resourceDTOs = new ArrayList<>();
		ResourceDTO resourceDTO = null;
		// if (!CollectionUtils.isEmpty(resources)) {
		for (Resource resource : resources) {
			resourceDTO = new ResourceDTO();
			BeanUtils.copyProperties(resource, resourceDTO);
			resourceDTOs.add(resourceDTO);
		}
		// }
		roleDTO.setResourceDTOs(resourceDTOs);

		return roleDTO;
	}

	public Role roleDTOToRole(RoleDTO roleDTO) {
		Role role = new Role();
		BeanUtils.copyProperties(roleDTO, role);

		List<ResourceDTO> resourceDTOs = roleDTO.getResourceDTOs();
		List<Resource> resources = new ArrayList<>();
		Resource resource = null;
		if (!CollectionUtils.isEmpty(resourceDTOs)) {
			for (ResourceDTO resourceDTO : resourceDTOs) {
				resource = new Resource();
				BeanUtils.copyProperties(resourceDTO, resource);
				resources.add(resource);
			}
		}
		role.setResources(resources);

		return role;
	}
}
