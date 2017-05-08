package com.ryan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ryan.domain.Resource;
import com.ryan.dto.ResourceDTO;
import com.ryan.repository.ResourceRepository;
import com.ryan.service.ResourceService;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceRepository resourceRepository;

	@Override
	public List<ResourceDTO> findByParentId(Long id) {
		List<Resource> resources = resourceRepository.findByParentId(id);
		List<ResourceDTO> resourceDTOs = null;
		if (!CollectionUtils.isEmpty(resources)) {
			resourceDTOs = new ArrayList<>();
			for (Resource resource : resources) {
				resourceDTOs.add(resourceToResourceDTO(resource));
			}
		}
		return resourceDTOs;
	}

	@Override
	public ResourceDTO findByPrimaryKey(Long id) {
		Resource resource = resourceRepository.findOne(id);
		return resourceToResourceDTO(resource);
	}

	@Override
	public ResourceDTO save(ResourceDTO resourceDTO) {
		Resource resource = resourceDTOToResource(resourceDTO);
		Resource savedResource = resourceRepository.save(resource);
		return resourceToResourceDTO(savedResource);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		resourceRepository.delete(id);
	}

	@Override
	public List<ResourceDTO> findByIdIn(Long[] ids) {
		List<Resource> resources = resourceRepository.findByIdIn(ids);
		List<ResourceDTO> resourceDTOs = new ArrayList<>();
		for (Resource resource : resources) {
			resourceDTOs.add(resourceToResourceDTO(resource));
		}
		return resourceDTOs;
	}

	public ResourceDTO resourceToResourceDTO(Resource resource) {
		ResourceDTO resourceDTO = null;
		if (resource != null) {
			resourceDTO = new ResourceDTO();
			BeanUtils.copyProperties(resource, resourceDTO);
		}
		return resourceDTO;
	}

	public Resource resourceDTOToResource(ResourceDTO resourceDTO) {
		Resource resource = new Resource();
		BeanUtils.copyProperties(resourceDTO, resource);
		return resource;
	}
}
