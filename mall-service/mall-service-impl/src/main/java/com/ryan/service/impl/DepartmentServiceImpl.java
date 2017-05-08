package com.ryan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ryan.domain.Department;
import com.ryan.dto.DepartmentDTO;
import com.ryan.repository.DepartmentRepository;
import com.ryan.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	DepartmentRepository departmentRepository;

	@Override
	public Map<String, Object> pageDepartmentList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol) {

		Pageable pageable = new PageRequest(page, length,
				new Sort(orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderCol));

		Specification<Department> departmentSp = new Specification<Department>() {

			@Override
			public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (!searchVal.equals("")) {
					Predicate idP = criteriaBuilder.like(root.get("id").as(String.class), "%" + searchVal + "%");
					Predicate nameP = criteriaBuilder.like(root.get("name"), "%" + searchVal + "%");
					Predicate timeP = criteriaBuilder.like(root.get("createTime").as(String.class),
							"%" + searchVal + "%");
					predicates.add(criteriaBuilder.or(timeP, criteriaBuilder.or(idP, nameP)));
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
				return null;
			}
		};
		Page<Department> departmentPage = departmentRepository.findAll(departmentSp, pageable);

		Map<String, Object> result = new HashMap<>();
		List<Department> departments = departmentPage.getContent();
		List<DepartmentDTO> departmentDTOs = new ArrayList<>();
		for (Department department : departments) {
			departmentDTOs.add(departmentToDepartmentDTO(department));
		}
		result.put("data", departmentDTOs);
		result.put("recordsTotal", departmentPage.getTotalElements());
		result.put("recordsFiltered", departmentPage.getTotalElements());
		return result;
	}

	@Override
	public List<DepartmentDTO> findAll() {
		List<Department> departments = departmentRepository.findAll();
		List<DepartmentDTO> departmentDTOs = new ArrayList<>();
		for (Department department : departments) {
			departmentDTOs.add(departmentToDepartmentDTO(department));
		}
		return departmentDTOs;
	}

	@Override
	public DepartmentDTO findByPrimaryKey(Long id) {
		Department department = departmentRepository.findOne(id);
		return departmentToDepartmentDTO(department);
	}

	@Override
	public DepartmentDTO save(DepartmentDTO departmentDTO) {
		Department department = departmentDTOToDepartment(departmentDTO);
		Department savedDepartment = departmentRepository.save(department);
		return departmentToDepartmentDTO(savedDepartment);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		departmentRepository.delete(id);
	}

	public DepartmentDTO departmentToDepartmentDTO(Department department) {
		DepartmentDTO departmentDTO = new DepartmentDTO();
		BeanUtils.copyProperties(department, departmentDTO);
		return departmentDTO;
	}

	public Department departmentDTOToDepartment(DepartmentDTO departmentDTO) {
		Department department = new Department();
		BeanUtils.copyProperties(departmentDTO, department);
		return department;
	}
}
