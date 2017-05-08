package com.ryan.controller.product;

import java.util.Date;
import java.util.List;
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

import com.ryan.dto.CatalogDTO;
import com.ryan.dto.ProductDTO;
import com.ryan.dto.SpecDTO;
import com.ryan.service.CatalogService;
import com.ryan.service.ProductService;
import com.ryan.service.SpecService;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductService productService;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private SpecService specService;

	@GetMapping("/index")
	public String index(){
		return "product/index";
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
			orderCol = "name";
			break;
		default:
			orderCol = "createTime";
			break;
		}
		int page = start / length;
		Map<String, Object> result = productService.pageProductList(page, length, searchVal, orderDir, orderCol);
		result.put("draw", draw);
		return result;
	}
	
	@RequestMapping("/new")
	public String create(Model model) {
		List<CatalogDTO> catalogDTOs=catalogService.findByHasChildren(false);
		model.addAttribute("catalogs", catalogDTOs);
		return "product/new";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	@ResponseBody
	public String save(ProductDTO productDTO,Long catalogId) throws Exception {
		CatalogDTO catalogDTO=catalogService.findByPrimaryKey(catalogId);
		productDTO.setCatalogDTO(catalogDTO);
		productDTO.setCreateTime(new Date());
		productService.save(productDTO);
		log.info("新增->ID=" + productDTO.getId());
		return "1";
	}
	
	@RequestMapping(value = "/{id}")
	public String show(Model model, @PathVariable Long id) {
		ProductDTO productDTO = productService.findByPrimaryKey(id);
		model.addAttribute("product", productDTO);
		return "product/view";
	}
	
	@RequestMapping(value = "/update/{id}")
	public String update(Model model, @PathVariable Long id) {
		List<CatalogDTO> catalogDTOs=catalogService.findByHasChildren(false);
		model.addAttribute("catalogs", catalogDTOs);
		ProductDTO productDTO = productService.findByPrimaryKey(id);
		model.addAttribute("product", productDTO);
		return "product/edit";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(ProductDTO productDTO,Long catalogId) {
		CatalogDTO catalogDTO=catalogService.findByPrimaryKey(catalogId);
		productDTO.setCatalogDTO(catalogDTO);
		productDTO.setUpdateTime(new Date());
		productService.save(productDTO);
		log.info("更新id：{}", productDTO.getId());
		return "1";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		productService.deleteByPrimaryKey(id);
		log.info("删除->ID=" + id);
		return "1";
	}
	
	@GetMapping("/specChoice/{id}")
	public String specChoice(@PathVariable Long id,Model model){
		model.addAttribute("productId", id);
		ProductDTO productDTO = productService.findByPrimaryKey(id);
		CatalogDTO secondCatalogDTO=productDTO.getCatalogDTO();
		CatalogDTO firstCatalogDTO=catalogService.findByPrimaryKey(secondCatalogDTO.getParentId());
		List<SpecDTO> firstCatalogSpecDTOs=specService.findByCatalogOrderBySortAsc(firstCatalogDTO.getId());
		List<SpecDTO> secondCatalogSpecDTOs=specService.findByCatalogOrderBySortAsc(secondCatalogDTO.getId());
		firstCatalogSpecDTOs.addAll(secondCatalogSpecDTOs);
		model.addAttribute("specs",firstCatalogSpecDTOs);
		return "product/specChoice";
	}
}
