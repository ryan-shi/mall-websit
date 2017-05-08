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

import com.ryan.domain.Catalog;
import com.ryan.domain.Product;
import com.ryan.dto.CatalogDTO;
import com.ryan.dto.ProductDTO;
import com.ryan.repository.ProductRepository;
import com.ryan.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Map<String, Object> pageProductList(Integer page, Integer length, String searchVal, String orderDir,
			String orderCol) {

		Pageable pageable = new PageRequest(page, length,
				new Sort(orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderCol));

		Specification<Product> productSp = new Specification<Product>() {

			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery,
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
		Page<Product> productPage = productRepository.findAll(productSp, pageable);
		List<Product> products = productPage.getContent();
		List<ProductDTO> productDTOs = new ArrayList<>();
		for (Product product : products) {
			productDTOs.add(productToProductDTO(product));
		}
		Map<String, Object> result = new HashMap<>();
		result.put("data", productDTOs);
		result.put("recordsTotal", productPage.getTotalElements());
		result.put("recordsFiltered", productPage.getTotalElements());
		return result;
	}

	@Override
	public ProductDTO save(ProductDTO productDTO) {
		Product product = productDTOToProduct(productDTO);
		Product savedProduct = productRepository.save(product);
		return productToProductDTO(savedProduct);
	}

	@Override
	public ProductDTO findByPrimaryKey(Long id) {
		Product product = productRepository.findOne(id);
		return productToProductDTO(product);
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		productRepository.delete(id);
	}

	public ProductDTO productToProductDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		BeanUtils.copyProperties(product, productDTO);
		Catalog catalog = product.getCatalog();
		CatalogDTO catalogDTO = new CatalogDTO();
		if (catalog != null) {
			BeanUtils.copyProperties(catalog, catalogDTO);
		}
		productDTO.setCatalogDTO(catalogDTO);
		return productDTO;
	}

	public Product productDTOToProduct(ProductDTO productDTO) {
		Product product = new Product();
		BeanUtils.copyProperties(productDTO, product);
		CatalogDTO catalogDTO = productDTO.getCatalogDTO();
		Catalog catalog = new Catalog();
		if (catalogDTO != null)
			BeanUtils.copyProperties(catalogDTO, catalog);
		product.setCatalog(catalog);
		return product;
	}
}
