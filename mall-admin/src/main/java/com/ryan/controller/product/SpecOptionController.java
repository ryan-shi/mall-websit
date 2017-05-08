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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ryan.dto.SpecDTO;
import com.ryan.dto.SpecOptionDTO;
import com.ryan.service.SpecOptionService;
import com.ryan.service.SpecService;

@Controller
@RequestMapping("/specOption")
public class SpecOptionController {
	
	private static final Logger log = LoggerFactory.getLogger(SpecOptionController.class);
	
	@Autowired
	SpecOptionService specOptionService;
	@Autowired
	SpecService specService;
	
	@RequestMapping("/index/{specId}")
	public String index(Model model,@PathVariable Long specId){
		model.addAttribute("specId", specId);
		return "specOption/index";
	}
	
	/**
	 * @param draw
	 * @param start
	 * @param length
	 * @param id spec's id
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getList(Integer draw, Integer start, Integer length, Long id) {
		log.info("draw: {},start: {},length: {}", draw, start, length);
		List<SpecOptionDTO> specOptionDTOs = specOptionService.findBySpecId(id);
		Map<String, Object> result = new HashMap<>();
		result.put("draw", draw);
		result.put("data", specOptionDTOs);
		result.put("recordsTotal", specOptionDTOs.size());
		result.put("recordsFiltered", specOptionDTOs.size());
		return result;
	}
	
	@RequestMapping(value = "/{id}")
	public String show(Model model, @PathVariable Long id) {
		SpecOptionDTO specOptionDTO = specOptionService.findByPrimaryKey(id);
		model.addAttribute("specOption", specOptionDTO);
		return "specOption/view";
	}

	@RequestMapping("/new")
	public String create() {
		return "specOption/new";
	}

	@PostMapping("/new")
	@ResponseBody
	public String save(SpecOptionDTO specOptionDTO,Long specId) {
		SpecDTO specDTO=specService.findByPrimaryKey(specId);
		specOptionDTO.setSpecDTO(specDTO);
		specOptionDTO.setCreateTime(new Date());
		specOptionService.save(specOptionDTO);
		log.info("新增id：{}", specOptionDTO.getId());
		return "1";
	}
	
	@RequestMapping(value = "/update/{id}")
	public String update(Model model, @PathVariable Long id) {
		SpecOptionDTO specOptionDTO = specOptionService.findByPrimaryKey(id);
		model.addAttribute("specOption", specOptionDTO);
		return "specOption/edit";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(SpecOptionDTO specOptionDTO,Long specId) {
		SpecDTO specDTO=specService.findByPrimaryKey(specId);
		specOptionDTO.setSpecDTO(specDTO);
		specOptionDTO.setUpdateTime(new Date());
		specOptionService.save(specOptionDTO);
		log.info("更新id：{}", specOptionDTO.getId());
		return "1";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		specOptionService.deleteByPrimaryKey(id);
		log.info("删除->ID=" + id);
		return "1";
	}
}
