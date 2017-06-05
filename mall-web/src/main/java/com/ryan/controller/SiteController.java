package com.ryan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
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
	private RedisTemplate<String, String> stringTemplate;
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
		ValueOperations<String, String> valueOperation = stringTemplate.opsForValue();

		String fileUrlPrefix = env.getProperty("file.path.prefix");

		String bigAdvertisementDTOsJson = valueOperation.get("advert_index_bigAdvertisementDTOs");
		List<AdvertisementDTO> bigAdvertisementDTOs = null;
		if (bigAdvertisementDTOsJson == null) {
			log.info("缓存中没有 bigAdvertisementDTOs！从数据库取，并存入缓存！");
			bigAdvertisementDTOs = advertisementService.findAdvertisementListByPosition(1, 5);
			for (AdvertisementDTO advertisementDTO : bigAdvertisementDTOs) {
				String picUrl = advertisementDTO.getPicture();
				advertisementDTO.setPicture(fileUrlPrefix + picUrl);
				// log.info("product pic: {}", advertisementDTO.getPicture());
			}
			valueOperation.set("advert_index_bigAdvertisementDTOs", JSON.toJSONString(bigAdvertisementDTOs), 60,
					TimeUnit.MINUTES);
			log.info("jsonStr:{}", JSON.toJSONString(bigAdvertisementDTOs));
		} else {
			log.info("bigAdvertisementDTOs 从缓存中得到！");
			bigAdvertisementDTOs = JSON.parseArray(bigAdvertisementDTOsJson, AdvertisementDTO.class);
		}
		log.info("bigAdvertisementDTOs size:{}", bigAdvertisementDTOs.size());
		model.addAttribute("bigAdvertisements", bigAdvertisementDTOs);

		String searchAdvertisementDTOsJson = valueOperation.get("advert_index_searchAdvertisementDTOs");
		List<AdvertisementDTO> searchAdvertisementDTOs = null;
		if (searchAdvertisementDTOsJson == null) {
			log.info("searchAdvertisementDTOs 存入缓存！");
			searchAdvertisementDTOs = advertisementService.findAdvertisementListByPosition(2, 5);
			valueOperation.set("advert_index_searchAdvertisementDTOs", JSON.toJSONString(searchAdvertisementDTOs), 60,
					TimeUnit.MINUTES);
		} else {
			log.info("searchAdvertisementDTOs 从缓存中得到！");
			searchAdvertisementDTOs = JSON.parseArray(searchAdvertisementDTOsJson, AdvertisementDTO.class);
		}
		log.info("searchAdvertisementDTOsJson size:{}", searchAdvertisementDTOs.size());
		model.addAttribute("searchAdvertisements", searchAdvertisementDTOs);

		String firstSecondsJson = valueOperation.get("cata_index_firstSeconds");
		List<Map<String, Object>> firstSeconds = null;
		if (firstSecondsJson == null) {
			log.info("firstSeconds 存入缓存");
			List<CatalogDTO> firstLevelCatalogDTOs = catalogService.findFirstLevelCatalog();
			firstSeconds = new ArrayList<>();
			for (CatalogDTO catalogDTO : firstLevelCatalogDTOs) {
				Map<String, Object> firstSecond = new HashMap<>();
				List<CatalogDTO> secondLevelCatalogDTOs = catalogService.findByParentId(catalogDTO.getId());
				firstSecond.put("firstLevelCata", catalogDTO);
				firstSecond.put("secondLevelCatas", secondLevelCatalogDTOs);
				firstSeconds.add(firstSecond);
			}
			valueOperation.set("cata_index_firstSeconds", JSON.toJSONString(firstSeconds), 60,
					TimeUnit.MINUTES);
		} else {
			log.info("firstSeconds 从缓存中取");
			firstSeconds = (List<Map<String, Object>>)JSON.parse(firstSecondsJson);
		}
		model.addAttribute("firstSeconds", firstSeconds);

		String popularAdvertisementDTOsJson = valueOperation.get("advert_index_popularAdvertisementDTOs");
		List<AdvertisementDTO> popularAdvertisementDTOs = null;
		if (popularAdvertisementDTOsJson == null) {
			log.info("popularAdvertisementDTOs 存入缓存！");
			popularAdvertisementDTOs = advertisementService.findAdvertisementListByPosition(3, 10);
			for (AdvertisementDTO advertisementDTO : popularAdvertisementDTOs) {
				String picUrl = advertisementDTO.getPicture();
				advertisementDTO.setPicture(fileUrlPrefix + picUrl);
				// log.info("product pic: {}", advertisementDTO.getPicture());
			}
			valueOperation.set("advert_index_popularAdvertisementDTOs", JSON.toJSONString(popularAdvertisementDTOs), 60,
					TimeUnit.MINUTES);
		} else {
			log.info("popularAdvertisementDTOs 从缓存中得到！");
			popularAdvertisementDTOs = JSON.parseArray(popularAdvertisementDTOsJson, AdvertisementDTO.class);
		}
		log.info("popularProducts real size:{}", popularAdvertisementDTOs.size());
		model.addAttribute("popularAdvertisements", popularAdvertisementDTOs);

		String newProductDTOsJson = valueOperation.get("prod_index_newProductDTOs");
		List<ProductDTO> newProductDTOs = null;
		if (newProductDTOsJson == null) {
			log.info("newProductDTOs 存入缓存！");
			newProductDTOs = productService.findTopNProductByStatusAndNewOrSale(new Short("2"), 1, 10);
			for (ProductDTO productDTO : newProductDTOs) {
				productDTO.setPicture(fileUrlPrefix + productDTO.getPicture());
			}
			valueOperation.set("prod_index_newProductDTOs", JSON.toJSONString(newProductDTOs), 60, TimeUnit.MINUTES);
		} else {
			log.info("newProductDTOs 从缓存中得到！");
			newProductDTOs = JSON.parseArray(newProductDTOsJson, ProductDTO.class);
		}
		log.info("newProductDTOs size:{}", newProductDTOs.size());
		model.addAttribute("newProducts", newProductDTOs);

		String saleProductDTOsJson = valueOperation.get("prod_index_saleProductDTOs");
		List<ProductDTO> saleProductDTOs = null;
		if (saleProductDTOsJson == null) {
			log.info("saleProductDTOs 存入缓存！");
			saleProductDTOs = productService.findTopNProductByStatusAndNewOrSale(new Short("2"), 2, 10);
			for (ProductDTO productDTO : saleProductDTOs) {
				productDTO.setPicture(fileUrlPrefix + productDTO.getPicture());
			}
			valueOperation.set("prod_index_saleProductDTOs", JSON.toJSONString(saleProductDTOs), 60, TimeUnit.MINUTES);
		} else {
			log.info("saleProductDTOs 从缓存中得到！");
			saleProductDTOs = JSON.parseArray(saleProductDTOsJson, ProductDTO.class);
		}
		log.info("saleProductDTOs size:{}", saleProductDTOs.size());
		model.addAttribute("saleProducts", saleProductDTOs);

		String forYouAdvertisementDTOsJson = valueOperation.get("advert_index_forYouAdvertisementDTOs");
		List<AdvertisementDTO> forYouAdvertisementDTOs = null;
		if (forYouAdvertisementDTOsJson == null) {
			log.info("forYouAdvertisementDTOs 存入缓存！");
			forYouAdvertisementDTOs = advertisementService.findAdvertisementListByPosition(4, 5);
			for (AdvertisementDTO advertisementDTO : forYouAdvertisementDTOs) {
				String picUrl = advertisementDTO.getProductDTO().getPicture();
				advertisementDTO.setPicture(fileUrlPrefix + picUrl);
				log.info("product pic: {}", advertisementDTO.getPicture());
			}
			valueOperation.set("advert_index_forYouAdvertisementDTOs", JSON.toJSONString(forYouAdvertisementDTOs), 60,
					TimeUnit.MINUTES);
		} else {
			log.info("forYouAdvertisementDTOs 从缓存中得到！");
			forYouAdvertisementDTOs = JSON.parseArray(forYouAdvertisementDTOsJson, AdvertisementDTO.class);
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