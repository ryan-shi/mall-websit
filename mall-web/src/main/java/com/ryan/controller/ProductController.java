package com.ryan.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ryan.dto.CatalogDTO;
import com.ryan.dto.ProductDTO;
import com.ryan.dto.SKUDTO;
import com.ryan.dto.SpecDTO;
import com.ryan.dto.SpecOptionDTO;
import com.ryan.service.CatalogService;
import com.ryan.service.ProductService;
import com.ryan.service.SKUService;
import com.ryan.service.SpecService;

@Controller
@RequestMapping("/product")
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private Environment env;
	@Autowired
	ProductService productService;
	@Autowired
	SKUService skuService;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private SpecService specService;

	@GetMapping("/{id}")
	public String productDetail(@PathVariable Long id) {
		log.info("product id: {}", id);
		List<SKUDTO> skuDTOs = skuService.findByProductId(id);
		Long sku_id = skuDTOs.get(0).getId();
		log.info("sku id:{}", sku_id);
		return "redirect:/product/" + sku_id + ".html";
	}

	@GetMapping("/{id}.html")
	public String sku(@PathVariable Long id, Model model) {
		log.info("sku id: {}", id);
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		SKUDTO skuDTO = skuService.findByPrimaryKey(id);
		skuDTO.setPicture(fileUrlPrefix + skuDTO.getPicture());
		model.addAttribute("sku", skuDTO);

		ProductDTO productDTO = productService.findByPrimaryKey(skuDTO.getProductDTO().getId());
		productDTO.setPicture(fileUrlPrefix + productDTO.getPicture());
		model.addAttribute("product", productDTO);

		//当前sku的规格值
		List<SpecOptionDTO> specOptionDTOs = skuDTO.getSpecOptionDTOs();
		model.addAttribute("specOptions", specOptionDTOs);
		
		CatalogDTO secondCatalogDTO=productDTO.getCatalogDTO();
		CatalogDTO firstCatalogDTO=catalogService.findByPrimaryKey(secondCatalogDTO.getParentId());
		List<SpecDTO> firstCatalogSpecDTOs=specService.findByCatalogOrderBySortAsc(firstCatalogDTO.getId());
		List<SpecDTO> secondCatalogSpecDTOs=specService.findByCatalogOrderBySortAsc(secondCatalogDTO.getId());
		firstCatalogSpecDTOs.addAll(secondCatalogSpecDTOs);
		model.addAttribute("specs",firstCatalogSpecDTOs);
		return "product/index";
	}
	
	@GetMapping("/skuSpec")
	@ResponseBody
	public Long skuSpec(Long[] specOptionId){
		log.info("specOptionId: {}",Arrays.toString(specOptionId));
		Arrays.sort(specOptionId);
		StringBuffer specOptionIds = new StringBuffer();
		for (int i = 0; i < specOptionId.length; i++) {
			if (i + 1 == specOptionId.length) {
				specOptionIds.append(specOptionId[i]);
			} else {
				specOptionIds.append(specOptionId[i] + ":");
			}
		}
		List<SKUDTO> skuDTOs=skuService.findBySpecOptionIds(specOptionIds.toString());
		return skuDTOs.get(0).getId();
	}
}
