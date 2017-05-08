package com.ryan.controller.system;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ryan.dto.DepartmentDTO;
import com.ryan.service.DepartmentService;

@Controller
@RequestMapping("/department")
public class DepartmentController {
	private static Logger log = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;

	@RequestMapping("/index")
	public String index(Model model) throws Exception {
		return "department/index";
	}

	@RequestMapping(value = "/{id}")
	public String view(Model model, @PathVariable Long id) {
		DepartmentDTO departmentDTO = departmentService.findByPrimaryKey(id);
		model.addAttribute("department", departmentDTO);
		return "department/view";
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
		Map<String, Object> result = departmentService.pageDepartmentList(page, length, searchVal, orderDir, orderCol);
		result.put("draw", draw);
		return result;
	}

	@RequestMapping("/new")
	public String create() {
		return "department/new";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	@ResponseBody
	public String save(DepartmentDTO departmentDTO) throws Exception {
		departmentDTO.setCreateTime(new Date());
		departmentService.save(departmentDTO);
		log.info("新增->ID=" + departmentDTO.getId());
		return "1";
	}

	@RequestMapping(value = "/update/{id}")
	public String update(Model model, @PathVariable Long id) {
		DepartmentDTO departmentDTO = departmentService.findByPrimaryKey(id);
		model.addAttribute("department", departmentDTO);
		return "department/edit";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update")
	@ResponseBody
	public String update(DepartmentDTO departmentDTO) throws Exception {
		departmentDTO.setUpdateTime(new Date());
		departmentService.save(departmentDTO);
		log.info("修改->ID=" + departmentDTO.getId());
		return "1";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		departmentService.deleteByPrimaryKey(id);
		log.info("删除->ID=" + id);
		return "1";
	}

}
