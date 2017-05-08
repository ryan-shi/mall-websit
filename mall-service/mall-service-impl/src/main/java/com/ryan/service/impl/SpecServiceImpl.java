package com.ryan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ryan.domain.Catalog;
import com.ryan.domain.Spec;
import com.ryan.domain.SpecOption;
import com.ryan.dto.CatalogDTO;
import com.ryan.dto.SpecDTO;
import com.ryan.dto.SpecOptionDTO;
import com.ryan.repository.SpecRepository;
import com.ryan.service.SpecService;

@Service
public class SpecServiceImpl implements SpecService {

	@Autowired
	SpecRepository specRepository; 

	@Override
	public SpecDTO save(SpecDTO specDTO) {
		Spec spec = specDTOToSpec(specDTO);
		Spec savedSpec = specRepository.save(spec);
		return specToSpecDTO(savedSpec);
	}

	@Override
	public SpecDTO findByPrimaryKey(Long id) {
		Spec spec = specRepository.findOne(id);
		return specToSpecDTO(spec);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		specRepository.delete(id);
	}

	@Override
	public List<SpecDTO> findByCatalogOrderBySortAsc(Long catalogId) {
		List<Spec> specs = specRepository.findByCatalogIdOrderBySortAsc(catalogId);
		List<SpecDTO> specDTOs=new ArrayList<>();
		for (Spec spec : specs) {
			specDTOs.add(specToSpecDTO(spec));
		}
		return specDTOs;
	}

	public Spec specDTOToSpec(SpecDTO specDTO) {
		Spec spec = new Spec();
		BeanUtils.copyProperties(specDTO, spec);
		CatalogDTO catalogDTO = specDTO.getCatalogDTO();
		Catalog catalog = new Catalog();
		BeanUtils.copyProperties(catalogDTO, catalog);
		spec.setCatalog(catalog);

		List<SpecOptionDTO> specOptionDTOs = specDTO.getSpecOptionDTOs();
		List<SpecOption> specOptions = new ArrayList<>();
		SpecOption specOption = null;
		if (!CollectionUtils.isEmpty(specOptionDTOs)) {
			for (SpecOptionDTO specOptionDTO : specOptionDTOs) {
				specOption = new SpecOption();
				BeanUtils.copyProperties(specOptionDTO, specOption);
				specOptions.add(specOption);
			}
		}
		spec.setSpecOptions(specOptions);
		return spec;
	}

	public SpecDTO specToSpecDTO(Spec spec) {
		SpecDTO specDTO = new SpecDTO();
		BeanUtils.copyProperties(spec, specDTO);
		Catalog catalog = spec.getCatalog();
		CatalogDTO catalogDTO = new CatalogDTO();
		BeanUtils.copyProperties(catalog, catalogDTO);
		specDTO.setCatalogDTO(catalogDTO);

		List<SpecOption> specOptions = spec.getSpecOptions();
		List<SpecOptionDTO> specOptionDTOs = new ArrayList<>();
		SpecOptionDTO specOptionDTO = null;
		if (!CollectionUtils.isEmpty(specOptions)) {
			for (SpecOption specOption : specOptions) {
				specOptionDTO = new SpecOptionDTO();
				BeanUtils.copyProperties(specOption, specOptionDTO);
				specOptionDTOs.add(specOptionDTO);
			}
		}
		specDTO.setSpecOptionDTOs(specOptionDTOs);
		return specDTO;
	}
}
