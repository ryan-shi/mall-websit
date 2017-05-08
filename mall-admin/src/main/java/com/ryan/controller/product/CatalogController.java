package com.ryan.controller.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ryan.dto.CatalogDTO;
import com.ryan.service.CatalogService;
import com.ryan.vo.TreeNode;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

	private static final Logger log = LoggerFactory.getLogger(CatalogController.class);
	
	@Autowired
	private CatalogService catalogService;
	
	@RequestMapping("/index")
	public String index(){
		return "catalog/index";
	}
	
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getList(Integer draw, Integer start, Integer length, Long id) {
		log.info("draw: {},start: {},length: {}", draw, start, length);
		List<CatalogDTO> catalogDTOs = catalogService.findByParentId(id);
		Map<String, Object> result = new HashMap<>();
		result.put("draw", draw);
		result.put("data", catalogDTOs);
		result.put("recordsTotal", catalogDTOs.size());
		result.put("recordsFiltered", catalogDTOs.size());
		return result;
	}
	
	@RequestMapping("/new")
	public String create() {
		return "catalog/new";
	}
	
	@PostMapping("/new")
	@ResponseBody
	public String save(CatalogDTO catalogDTO) {
		catalogDTO.setCreateTime(new Date());
		CatalogDTO parentCataDTO=catalogService.findByPrimaryKey(catalogDTO.getParentId());
		parentCataDTO.setHasChildren(true);
		catalogService.save(parentCataDTO);
		catalogService.save(catalogDTO);
		log.info("新增id：{}", catalogDTO.getId());
		return "1";
	}
	
	@RequestMapping(value = "/{id}")
	public String show(Model model, @PathVariable Long id) {
		CatalogDTO catalogDTO = catalogService.findByPrimaryKey(id);
		model.addAttribute("catalog", catalogDTO);
		return "catalog/view";
	}
	
	@RequestMapping(value = "/update/{id}")
	public String update(Model model, @PathVariable Long id) {
		CatalogDTO catalogDTO = catalogService.findByPrimaryKey(id);
		model.addAttribute("catalog", catalogDTO);
		return "catalog/edit";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(CatalogDTO catalogDTO) {
		catalogDTO.setUpdateTime(new Date());
		catalogService.save(catalogDTO);
		log.info("更新id：{}", catalogDTO.getId());
		return "1";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		CatalogDTO catalogDTO=catalogService.findByPrimaryKey(id);
		CatalogDTO parentCataDTO=catalogService.findByPrimaryKey(catalogDTO.getParentId());
		parentCataDTO.setHasChildren(false);
		catalogService.save(parentCataDTO);
		catalogService.deleteByPrimaryKey(id);
		log.info("删除->ID=" + id);
		return "1";
	}
	
	@GetMapping("/treeChildren")
	@ResponseBody
	public List<TreeNode> tree(@RequestParam Long parent) {
		log.info("parent: {}", parent);
		Map<String, Boolean> state = new HashMap<>();
		state.put("opened", false);
		List<TreeNode> result = new ArrayList<>();
		List<CatalogDTO> childrenCatalogDTOs = catalogService.findByParentId(parent);
		for (CatalogDTO catalogDTO : childrenCatalogDTOs) {
			List<CatalogDTO> catalogDTOs = catalogService.findByParentId(catalogDTO.getId());
			result.add(new TreeNode(catalogDTO.getId(), catalogDTO.getParentId(), catalogDTO.getName(),
					CollectionUtils.isEmpty(catalogDTOs) ? "fa fa-file icon-state-warning icon-lg" : "fa fa-folder icon-state-warning icon-lg",
					CollectionUtils.isEmpty(catalogDTOs) ? false : true, state));
		}
		return result;
	}

	@GetMapping("/treeRoot")
	@ResponseBody
	public Map<String, Object> tree(@RequestParam String parent) {
		Map<String, Object> result = new HashMap<>();
		Long rootId = 1l;
		CatalogDTO rootCatalogDTO = catalogService.findByPrimaryKey(rootId);
		result.put("id", "1");
		result.put("parent", "#");
		result.put("text", rootCatalogDTO.getName());
		result.put("icon", "fa fa-home icon-state-warning icon-lg");
		result.put("children", true);
		Map<String, Boolean> state = new HashMap<>();
		state.put("opened", true);
		result.put("state", state);
		return result;
	}
}
