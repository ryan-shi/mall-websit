package com.ryan.controller.cms;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ryan.dto.AdvertisementDTO;
import com.ryan.dto.ProductDTO;
import com.ryan.service.AdvertisementService;
import com.ryan.service.ProductService;

@Controller
@RequestMapping("/advert")
public class AdvertisementController {

	private static final Logger log = LoggerFactory.getLogger(AdvertisementController.class);

	@Autowired
	AdvertisementService advertisementService;
	@Autowired
	ProductService productService;

	@GetMapping("/index")
	public String index() {
		return "advert/index";
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getList(Integer draw, Integer start, Integer length, HttpServletRequest request) {

		log.info("draw: {},start: {},length: {}", draw, start, length);
		String searchVal = request.getParameter("search[value]");
		String orderCol = request.getParameter("order[0][column]");
		String orderDir = request.getParameter("order[0][dir]");
		log.info("search: {}", searchVal);
		log.info("orderCol: {}", orderCol);
		log.info("orderDir: {}", orderDir);
		switch (orderCol) {
		case "0":
			orderCol = "id";
			break;
		case "1":
			orderCol = "title";
			break;
		default:
			orderCol = "createTime";
			break;
		}
		int page = start / length;
		Map<String, Object> result = advertisementService.pageAdvertisementList(page, length, searchVal, orderDir,
				orderCol);
		result.put("draw", draw);
		return result;
	}

	@RequestMapping("/new")
	public String create(Model model) {
		return "advert/new";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	@ResponseBody
	public String save(AdvertisementDTO advertisementDTO,Long productId) throws Exception {
		ProductDTO productDTO=productService.findByPrimaryKey(productId);
		if(productDTO==null){			
			log.info("输入的商品不存在，productId:{}",productId);
			return "2";//商品找不到
		}
		else{
			advertisementDTO.setCreateTime(new Date());
			advertisementDTO.setProductDTO(productDTO);
			advertisementService.save(advertisementDTO);
			log.info("新增->ID=" + advertisementDTO.getId());
			return "1";
		}
		
	}
	
	@GetMapping("/update/{id}")
	public String update(@PathVariable Long id,Model model){
		AdvertisementDTO advertisementDTO=advertisementService.findByPrimaryKey(id);
		model.addAttribute("advertisement", advertisementDTO);
		return "advert/edit";
	}
	
	@PostMapping("/update")
	@ResponseBody
	public String update(AdvertisementDTO advertisementDTO,Long productId){
		ProductDTO productDTO=productService.findByPrimaryKey(productId);
		if(productDTO==null){			
			log.info("输入的商品不存在，productId:{}",productId);
			return "2";//商品找不到
		}
		else{
			advertisementDTO.setUpdateTime(new Date());
			advertisementDTO.setProductDTO(productDTO);
			advertisementService.save(advertisementDTO);
			log.info("新增->ID=" + advertisementDTO.getId());
			return "1";
		}
	}
	
	@PostMapping("/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id){
		advertisementService.deleteByPrimaryKey(id);
		return "1";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable Long id,Model model){
		AdvertisementDTO advertisementDTO=advertisementService.findByPrimaryKey(id);
		model.addAttribute("advertisement", advertisementDTO);
		return "advert/view";
	}
}
