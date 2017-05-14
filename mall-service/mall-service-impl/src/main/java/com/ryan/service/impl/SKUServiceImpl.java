package com.ryan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ryan.domain.Product;
import com.ryan.domain.SKU;
import com.ryan.domain.Spec;
import com.ryan.domain.SpecOption;
import com.ryan.dto.ProductDTO;
import com.ryan.dto.SKUDTO;
import com.ryan.dto.SpecDTO;
import com.ryan.dto.SpecOptionDTO;
import com.ryan.repository.SKURepository;
import com.ryan.service.SKUService;

@Service
public class SKUServiceImpl implements SKUService {

	private static final Logger log = LoggerFactory.getLogger(SKUServiceImpl.class);
	
	@Autowired
	SKURepository skuRepository;

	@Override
	public Map<String, Object> pageSKUList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol) {
		Specification<SKU> skuSp = new Specification<SKU>() {

			@Override
			public Predicate toPredicate(Root<SKU> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (!searchVal.equals("")) {
					Predicate idP = criteriaBuilder.like(root.get("id").as(String.class), "%" + searchVal + "%");
					Predicate priceP = criteriaBuilder.like(root.get("price").as(String.class), "%" + searchVal + "%");
					Predicate timeP = criteriaBuilder.like(root.get("createTime").as(String.class),
							"%" + searchVal + "%");
					predicates.add(criteriaBuilder.or(timeP, criteriaBuilder.or(idP, priceP)));
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
				return null;
			}
		};

		Pageable pageable = new PageRequest(page, length,
				new Sort(orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderCol));
		Page<SKU> skuPage = skuRepository.findAll(skuSp, pageable);
		List<SKU> skus = skuPage.getContent();
		List<SKUDTO> skuDTOs = new ArrayList<>();
		for (SKU sku : skus) {
			skuDTOs.add(SKUToSKUDTO(sku));
		}
		Map<String, Object> result = new HashMap<>();
		result.put("data", skuDTOs);
		result.put("recordsTotal", skuPage.getTotalElements());
		result.put("recordsFiltered", skuPage.getTotalElements());

		return result;
	}

	@Override
	public List<SKUDTO> findBySpecOptionIds(String specOptionIds) {
		List<SKU> skus = skuRepository.findBySpecOptionIds(specOptionIds);
		List<SKUDTO> skuDTOs = new ArrayList<>();
		for (SKU sku : skus) {
			skuDTOs.add(SKUToSKUDTO(sku));
		}
		return skuDTOs;
	}

	@Override
	public SKUDTO save(SKUDTO skuDTO) {
		SKU sku=SKUDTOToSKU(skuDTO);
		SKU savedSku = skuRepository.save(sku);
		log.info("saved pecOption:{}",savedSku.getSpecOptions().get(0).getName());
		log.info("saved spec:{}",savedSku.getSpecOptions().get(0).getSpec());
		return SKUToSKUDTO(savedSku);
	}

	@Override
	public SKUDTO findByPrimaryKey(Long id) {
		SKU sku = skuRepository.findOne(id);
		return SKUToSKUDTO(sku);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		skuRepository.delete(id);
	}
	
	@Override
	public List<SKUDTO> findByProductId(Long id) {
		List<SKU> skus=skuRepository.findByProductId(id);
		List<SKUDTO> skuDTOs = new ArrayList<>();
		for (SKU sku : skus) {
			skuDTOs.add(SKUToSKUDTO(sku));
		}
		return skuDTOs;
	}

	public SKUDTO SKUToSKUDTO(SKU sku) {
		SKUDTO skuDTO = new SKUDTO();
		BeanUtils.copyProperties(sku, skuDTO);

		List<SpecOption> specOptions = sku.getSpecOptions();
		List<SpecOptionDTO> specOptionDTOs = new ArrayList<>();
		if(specOptionDTOs!=null){
			for (SpecOption specOption : specOptions) {
				log.info("specOption:{}",specOption.getName());
				Spec spec=specOption.getSpec();
				SpecDTO specDTO=new SpecDTO();
				log.info("spec:{}",spec);
				BeanUtils.copyProperties(spec, specDTO);
				SpecOptionDTO specOptionDTO = new SpecOptionDTO();
				BeanUtils.copyProperties(specOption, specOptionDTO);
				specOptionDTO.setSpecDTO(specDTO);
				specOptionDTOs.add(specOptionDTO);
			}
		}
		skuDTO.setSpecOptionDTOs(specOptionDTOs);

		Product product = sku.getProduct();
		ProductDTO productDTO = new ProductDTO();
		if (product != null)
			BeanUtils.copyProperties(product, productDTO);
		skuDTO.setProductDTO(productDTO);
		return skuDTO;
	}

	public SKU SKUDTOToSKU(SKUDTO skuDTO) {
		SKU sku = new SKU();
		BeanUtils.copyProperties(skuDTO, sku);

		List<SpecOptionDTO> specOptionDTOs = skuDTO.getSpecOptionDTOs();
		List<SpecOption> specOptions = new ArrayList<>();
		SpecOption specOption = null;
		for (SpecOptionDTO specOptionDTO : specOptionDTOs) {
			specOption = new SpecOption();
			SpecDTO specDTO=specOptionDTO.getSpecDTO();
			Spec spec=new Spec();
			BeanUtils.copyProperties(specDTO, spec);
			BeanUtils.copyProperties(specOptionDTO, specOption);
			specOption.setSpec(spec);
			specOptions.add(specOption);
		}
		sku.setSpecOptions(specOptions);

		ProductDTO productDTO = skuDTO.getProductDTO();
		Product product = new Product();
		if (productDTO != null)
			BeanUtils.copyProperties(productDTO, product);
		sku.setProduct(product);
		return sku;
	}
}
