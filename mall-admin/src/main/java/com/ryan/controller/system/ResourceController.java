package com.ryan.controller.system;

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

import com.ryan.dto.ResourceDTO;
import com.ryan.service.ResourceService;
import com.ryan.vo.TreeNode;

@Controller
@RequestMapping("/resource")
public class ResourceController {

	private static final Logger log = LoggerFactory.getLogger(ResourceController.class);

	@Autowired
	private ResourceService resourceService;

	@RequestMapping("/index")
	public String index() {
		return "resource/index";
	}
	
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getList(Integer draw, Integer start, Integer length, Long id) {
		log.info("draw: {},start: {},length: {}", draw, start, length);
		List<ResourceDTO> resourceDTOs = resourceService.findByParentId(id);
		Map<String, Object> result = new HashMap<>();
		result.put("draw", draw);
		result.put("data", resourceDTOs);
		result.put("recordsTotal", resourceDTOs.size());
		result.put("recordsFiltered", resourceDTOs.size());
		return result;
	}

	@RequestMapping(value = "/{id}")
	public String show(Model model, @PathVariable Long id) {
		ResourceDTO resourceDTO = resourceService.findByPrimaryKey(id);
		model.addAttribute("resource", resourceDTO);
		return "resource/view";
	}

	@RequestMapping("/new")
	public String create() {
		return "resource/new";
	}

	@PostMapping("/new")
	@ResponseBody
	public String save(ResourceDTO resourceDTO) {
		resourceDTO.setCreateTime(new Date());
		resourceService.save(resourceDTO);
		log.info("新增id：{}", resourceDTO.getId());
		return "1";
	}

	@RequestMapping(value = "/update/{id}")
	public String update(Model model, @PathVariable Long id) {
		ResourceDTO resourceDTO = resourceService.findByPrimaryKey(id);
		model.addAttribute("resource", resourceDTO);
		return "resource/edit";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(ResourceDTO resourceDTO) {
		resourceDTO.setUpdateTime(new Date());
		resourceService.save(resourceDTO);
		log.info("更新id：{}", resourceDTO.getId());
		return "1";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		resourceService.deleteByPrimaryKey(id);
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
		List<ResourceDTO> childrenResourceDTOs = resourceService.findByParentId(parent);
		for (ResourceDTO resourceDTO : childrenResourceDTOs) {
			List<ResourceDTO> resources = resourceService.findByParentId(resourceDTO.getId());
			result.add(new TreeNode(resourceDTO.getId(), resourceDTO.getParentId(), resourceDTO.getName(),
					CollectionUtils.isEmpty(resources) ? "fa fa-file icon-state-warning icon-lg" : "fa fa-folder icon-state-warning icon-lg",
					CollectionUtils.isEmpty(resources) ? false : true, state));
		}
		return result;
	}

	@GetMapping("/treeRoot")
	@ResponseBody
	public Map<String, Object> tree(@RequestParam String parent) {
		Map<String, Object> result = new HashMap<>();
		Long rootId = 1l;
		ResourceDTO rootResourceDTO = resourceService.findByPrimaryKey(rootId);
		result.put("id", "1");
		result.put("parent", "#");
		result.put("text", rootResourceDTO.getName());
		result.put("icon", "fa fa-home icon-state-warning icon-lg");
		result.put("children", true);
		Map<String, Boolean> state = new HashMap<>();
		state.put("opened", true);
		result.put("state", state);
		return result;
	}
}
