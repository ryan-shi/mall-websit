package com.ryan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ryan.dto.AdvertisementDTO;
import com.ryan.dto.CatalogDTO;
import com.ryan.dto.ProductDTO;
import com.ryan.service.AdvertisementService;
import com.ryan.service.CatalogService;
import com.ryan.service.ProductService;

@Controller
public class SiteController implements ErrorController {

	private static final Logger log = LoggerFactory.getLogger(SiteController.class);

	@Autowired
	private Environment env;
	@Autowired
	AdvertisementService advertisementService;
	@Autowired
	CatalogService catalogService;
	@Autowired
	ProductService productService;

	@RequestMapping("/")
	public String index(Model model) {
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		List<AdvertisementDTO> bigAdvertisementDTOs = advertisementService.findAdvertisementListByPosition(1, 5);
		for (AdvertisementDTO advertisementDTO : bigAdvertisementDTOs) {
			String picUrl = advertisementDTO.getPicture();
			advertisementDTO.setPicture(fileUrlPrefix + picUrl);
			log.info("product pic: {}", advertisementDTO.getPicture());
		}
		log.info("bigAdvertisementDTOs size:{}", bigAdvertisementDTOs.size());
		model.addAttribute("bigAdvertisements", bigAdvertisementDTOs);

		List<AdvertisementDTO> searchAdvertisementDTOs = advertisementService.findAdvertisementListByPosition(2, 5);
		model.addAttribute("searchAdvertisements", searchAdvertisementDTOs);

		List<CatalogDTO> firstLevelCatalogDTOs = catalogService.findFirstLevelCatalog();
		List<Map<String, Object>> firstSeconds = new ArrayList<>();
		for (CatalogDTO catalogDTO : firstLevelCatalogDTOs) {
			Map<String, Object> firstSecond = new HashMap<>();
			List<CatalogDTO> secondLevelCatalogDTOs = catalogService.findByParentId(catalogDTO.getId());
			firstSecond.put("firstLevelCata", catalogDTO);
			firstSecond.put("secondLevelCatas", secondLevelCatalogDTOs);
			firstSeconds.add(firstSecond);
		}
		model.addAttribute("firstSeconds", firstSeconds);

		List<AdvertisementDTO> popularAdvertisementDTOs = advertisementService.findAdvertisementListByPosition(3, 10);
		for (AdvertisementDTO advertisementDTO : popularAdvertisementDTOs) {
			String picUrl = advertisementDTO.getPicture();
			advertisementDTO.setPicture(fileUrlPrefix + picUrl);
			log.info("product pic: {}", advertisementDTO.getPicture());
		}
		log.info("popularProducts real size:{}", popularAdvertisementDTOs.size());
		model.addAttribute("popularAdvertisements", popularAdvertisementDTOs);

		List<ProductDTO> newProductDTOs = productService.findTopNProductByStatusAndNewOrSale(new Short("2"), 1, 10);
		for (ProductDTO productDTO : newProductDTOs) {
			productDTO.setPicture(fileUrlPrefix + productDTO.getPicture());
		}
		log.info("newProductDTOs size:{}", newProductDTOs.size());
		model.addAttribute("newProducts", newProductDTOs);
		
		List<ProductDTO> saleProductDTOs = productService.findTopNProductByStatusAndNewOrSale(new Short("2"), 2, 10);
		for (ProductDTO productDTO : saleProductDTOs) {
			productDTO.setPicture(fileUrlPrefix + productDTO.getPicture());
		}
		log.info("saleProductDTOs size:{}", saleProductDTOs.size());
		model.addAttribute("saleProducts", saleProductDTOs);

		List<AdvertisementDTO> forYouAdvertisementDTOs = advertisementService.findAdvertisementListByPosition(4, 5);
		for (AdvertisementDTO advertisementDTO : forYouAdvertisementDTOs) {
			String picUrl = advertisementDTO.getProductDTO().getPicture();
			advertisementDTO.setPicture(fileUrlPrefix + picUrl);
			log.info("product pic: {}", advertisementDTO.getPicture());
		}
		log.info("forYouAdvertisementDTOs real size:{}", forYouAdvertisementDTOs.size());
		model.addAttribute("forYouAdvertisements", forYouAdvertisementDTOs);
		
		return "index";
	}

	@RequestMapping(value = "/error")
	public String handleError() {
		return "404";
	}

	@Override
	public String getErrorPath() {
		return "404";
	}

	@RequestMapping(value = "/deny")
	public String deny() {
		return "deny";
	}
}