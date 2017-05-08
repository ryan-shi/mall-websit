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

import com.ryan.domain.Advertisement;
import com.ryan.domain.Product;
import com.ryan.dto.AdvertisementDTO;
import com.ryan.dto.ProductDTO;
import com.ryan.repository.AdvertisementRepository;
import com.ryan.service.AdvertisementService;

@Service("AdvertisementServiceImpl")
public class AdvertisementServiceImpl implements AdvertisementService {

	@Autowired
	AdvertisementRepository advertisementRepository;
	
	@Override
	public Map<String, Object> pageAdvertisementList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol) {
		Pageable pageable = new PageRequest(page, length,
				new Sort(orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderCol));

		Specification<Advertisement> advertisementSp = new Specification<Advertisement>() {

			@Override
			public Predicate toPredicate(Root<Advertisement> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				if (!searchVal.equals("")) {
					Predicate idP = criteriaBuilder.like(root.get("id").as(String.class), "%" + searchVal + "%");
					Predicate titleP = criteriaBuilder.like(root.get("title"), "%" + searchVal + "%");
					Predicate positionP = criteriaBuilder.like(root.get("position"), "%" + searchVal + "%");
					Predicate timeP = criteriaBuilder.like(root.get("createTime").as(String.class),
							"%" + searchVal + "%");
					return criteriaBuilder.and(timeP, criteriaBuilder.or(positionP, criteriaBuilder.or(idP, titleP)));
				}
				return null;
			}
		};
		Page<Advertisement> advertisementPage = advertisementRepository.findAll(advertisementSp, pageable);
		Map<String, Object> result = new HashMap<>();
		List<Advertisement> advertisements = advertisementPage.getContent();
		List<AdvertisementDTO> advertisementDTOs = new ArrayList<>();
		for (Advertisement advertisement : advertisements) {
			advertisementDTOs.add(advertisementToAdvertisementDTO(advertisement));
		}
		result.put("data", advertisementDTOs);
		result.put("recordsTotal", advertisementPage.getTotalElements());
		result.put("recordsFiltered", advertisementPage.getTotalElements());
		return result;
	}

	@Override
	public AdvertisementDTO findByPrimaryKey(Long id) {
		Advertisement advertisement=advertisementRepository.findOne(id);
		return advertisementToAdvertisementDTO(advertisement);
	}

	@Override
	public AdvertisementDTO save(AdvertisementDTO advertisementDTO) {
		Advertisement advertisement=advertisementDTOToAdvertisement(advertisementDTO);
		Advertisement savedAdvertisement=advertisementRepository.save(advertisement);
		return advertisementToAdvertisementDTO(savedAdvertisement);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		advertisementRepository.delete(id);
	}

	public AdvertisementDTO advertisementToAdvertisementDTO(Advertisement advertisement){
		AdvertisementDTO advertisementDTO=new AdvertisementDTO();
		BeanUtils.copyProperties(advertisement, advertisementDTO);
		Product product=advertisement.getProduct();
		ProductDTO productDTO=new ProductDTO();
		if(product!=null)
			BeanUtils.copyProperties(product, productDTO);
		advertisementDTO.setProductDTO(productDTO);
		
		return advertisementDTO;
	}
	
	public Advertisement advertisementDTOToAdvertisement(AdvertisementDTO advertisementDTO){
		Advertisement advertisement=new Advertisement();
		BeanUtils.copyProperties(advertisementDTO, advertisement);
		ProductDTO productDTO=advertisementDTO.getProductDTO();
		Product product=new Product();
		if(productDTO!=null)
			BeanUtils.copyProperties(productDTO, product);
		advertisement.setProduct(product);
		
		return advertisement;
	}

}
