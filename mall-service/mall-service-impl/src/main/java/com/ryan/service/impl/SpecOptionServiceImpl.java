package com.ryan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryan.domain.Spec;
import com.ryan.domain.SpecOption;
import com.ryan.dto.SpecDTO;
import com.ryan.dto.SpecOptionDTO;
import com.ryan.repository.SpecOptionRepository;
import com.ryan.service.SpecOptionService;

@Service
public class SpecOptionServiceImpl implements SpecOptionService {

	@Autowired
	SpecOptionRepository specOptionRepository;

	@Override
	public void deleteByPrimaryKey(Long id) {
		specOptionRepository.delete(id);
	}

	@Override
	public List<SpecOptionDTO> findBySpecId(Long specId) {
		List<SpecOption> specOptions = specOptionRepository.findBySpecId(specId);
		List<SpecOptionDTO> specOptionDTOs=new ArrayList<>();
		for (SpecOption specOption : specOptions) {
			specOptionDTOs.add(specOptionToSpecOptionDTO(specOption));
		}
		return specOptionDTOs;
	}

	@Override
	public SpecOptionDTO findByPrimaryKey(Long id) {
		SpecOption specOption=specOptionRepository.findOne(id);
		return specOptionToSpecOptionDTO(specOption);
	}

	@Override
	public SpecOptionDTO save(SpecOptionDTO specOptionDTO) {
		SpecOption specOption=specOptionDTOToSpecOption(specOptionDTO);
		SpecOption savedSpecOption=specOptionRepository.save(specOption);
		return specOptionToSpecOptionDTO(savedSpecOption);
	}

	public SpecOptionDTO specOptionToSpecOptionDTO(SpecOption specOption) {
		SpecOptionDTO specOptionDTO = new SpecOptionDTO();
		BeanUtils.copyProperties(specOption, specOptionDTO);
		Spec spec=specOption.getSpec();
		SpecDTO specDTO=new SpecDTO();
		if(spec!=null)
			BeanUtils.copyProperties(spec, specDTO);
		specOptionDTO.setSpecDTO(specDTO);
		return specOptionDTO;
	}
	
	public SpecOption specOptionDTOToSpecOption(SpecOptionDTO specOptionDTO){
		SpecOption specOption=new SpecOption();
		BeanUtils.copyProperties(specOptionDTO, specOption);
		SpecDTO specDTO=specOptionDTO.getSpecDTO();
		Spec spec=new Spec();
		if (specDTO!=null) {
			BeanUtils.copyProperties(specDTO, spec);
		}
		specOption.setSpec(spec);
		return specOption;
	}
}
