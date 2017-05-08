package com.ryan.controller.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ryan.dto.SpecDTO;
import com.ryan.service.CatalogService;
import com.ryan.service.SpecService;

@Controller
@RequestMapping("/spec")
public class SpecController {

	private static final Logger log = LoggerFactory.getLogger(SpecController.class);

	@Autowired
	private SpecService specService;
	@Autowired
	private CatalogService catalogService;

	@RequestMapping("/index")
	public String index() {
		return "spec/index";
	}

	@RequestMapping("/new")
	public String create() {
		return "spec/new";
	}

	@PostMapping("/new")
	@ResponseBody
	public String save(SpecDTO specDTO, Long catalogId) {
		specDTO.setCreateTime(new Date());
		CatalogDTO catalogDTO = catalogService.findByPrimaryKey(catalogId);
		specDTO.setCatalogDTO(catalogDTO);
		log.info("spec catalog:{}", catalogId);
		specService.save(specDTO);
		log.info("新增id：{}", specDTO.getId());
		return "1";
	}

	@GetMapping("/{id}")
	public String show(Model model, @PathVariable Long id) {
		SpecDTO specDTO = specService.findByPrimaryKey(id);
		model.addAttribute("spec", specDTO);
		return "spec/view";
	}

	@RequestMapping(value = "/update/{id}")
	public String update(Model model, @PathVariable Long id) {
		SpecDTO specDTO = specService.findByPrimaryKey(id);
		model.addAttribute("spec", specDTO);
		return "spec/edit";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(SpecDTO specDTO, Long catalogId) {
		CatalogDTO catalogDTO = catalogService.findByPrimaryKey(catalogId);
		specDTO.setUpdateTime(new Date());
		specDTO.setCatalogDTO(catalogDTO);
		specService.save(specDTO);
		log.info("更新id：{}", specDTO.getId());
		return "1";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		specService.deleteByPrimaryKey(id);
		log.info("删除->ID=" + id);
		return "1";
	}

	/**
	 * 
	 * @param draw
	 * @param start
	 * @param length
	 * @param id
	 *            catalog's is
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getList(Integer draw, Integer start, Integer length, Long id) {
		log.info("draw: {},start: {},length: {}", draw, start, length);
		List<SpecDTO> specDTOs = specService.findByCatalogOrderBySortAsc(id);
		Map<String, Object> result = new HashMap<>();
		result.put("draw", draw);
		result.put("data", specDTOs);
		result.put("recordsTotal", specDTOs.size());
		result.put("recordsFiltered", specDTOs.size());
		return result;
	}
}
