package com.ryan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ryan.domain.Catalog;
import com.ryan.domain.Spec;
import com.ryan.dto.CatalogDTO;
import com.ryan.dto.SpecDTO;
import com.ryan.repository.CatalogRepository;
import com.ryan.service.CatalogService;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	CatalogRepository catalogRepository;
	
	@Override
	public List<CatalogDTO> findByParentId(Long parent) {
		List<Catalog> catalogs = catalogRepository.findByParentId(parent);
		List<CatalogDTO> catalogDTOs = new ArrayList<>();
		for (Catalog catalog : catalogs) {
			catalogDTOs.add(catalogToCatalogDTO(catalog));
		}
		return catalogDTOs;
	}

	@Override
	public CatalogDTO findByPrimaryKey(Long id) {
		Catalog catalog = catalogRepository.findOne(id);
		return catalogToCatalogDTO(catalog);
	}

	@Override
	public CatalogDTO save(CatalogDTO catalogDTO) {
		Catalog catalog = catalogDTOToCatalog(catalogDTO);
		Catalog savedCatalog = catalogRepository.save(catalog);
		return catalogToCatalogDTO(savedCatalog);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		catalogRepository.delete(id);
	}

	@Override
	public List<CatalogDTO> findByHasChildren(boolean haschildren) {
		List<Catalog> catalogs = catalogRepository.findByHasChildren(haschildren);
		List<CatalogDTO> catalogDTOs = new ArrayList<>();
		for (Catalog catalog : catalogs) {
			catalogDTOs.add(catalogToCatalogDTO(catalog));
		}
		return catalogDTOs;
	}

	public CatalogDTO catalogToCatalogDTO(Catalog catalog) {
		CatalogDTO catalogDTO = new CatalogDTO();
		BeanUtils.copyProperties(catalog, catalogDTO);

		List<Spec> specs = catalog.getSpecs();
		List<SpecDTO> specDTOs = new ArrayList<>();
		SpecDTO specDTO = null;
		for (Spec spec : specs) {
			specDTO = new SpecDTO();
			BeanUtils.copyProperties(spec, specDTO);
			specDTOs.add(specDTO);
		}
		catalogDTO.setSpecDTOs(specDTOs);

		return catalogDTO;
	}

	public Catalog catalogDTOToCatalog(CatalogDTO catalogDTO) {
		Catalog catalog = new Catalog();
		BeanUtils.copyProperties(catalogDTO, catalog);

		List<SpecDTO> specDTOs = catalogDTO.getSpecDTOs();
		List<Spec> specs = new ArrayList<>();
		Spec spec = null;
		if (!CollectionUtils.isEmpty(specDTOs)) {
			for (SpecDTO specDTO : specDTOs) {
				spec = new Spec();
				BeanUtils.copyProperties(specDTO, spec);
				specs.add(spec);
			}
		}
		catalog.setSpecs(specs);

		return catalog;
	}
}
