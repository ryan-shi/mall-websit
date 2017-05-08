package com.ryan.controller.system;

import java.util.Arrays;
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

import com.ryan.dto.ResourceDTO;
import com.ryan.dto.RoleDTO;
import com.ryan.service.ResourceService;
import com.ryan.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController {
    
	private static final Logger log = LoggerFactory.getLogger(RoleController.class);
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourceService resourceService;
    
    @RequestMapping("/index")
    public String index() throws Exception{
        return "role/index";
    }

    @RequestMapping(value="/{id}")
    public String show(Model model,@PathVariable Long id) {
        RoleDTO roleDTO = roleService.findByPrimaryKey(id);
        model.addAttribute("role",roleDTO);
        return "role/view";
    }
    
    @RequestMapping(value="/roleResource/{id}")
    @ResponseBody
    public List<ResourceDTO> roleResource(@PathVariable Long id) {
        RoleDTO roleDTO = roleService.findByPrimaryKey(id);
        List<ResourceDTO> resourceDTOs=roleDTO.getResourceDTOs();
        return resourceDTOs;
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
		case "2":
			orderCol = "description";
			break;
		default:
			orderCol = "createTime";
			break;
		}
		int page = start / length;
		Map<String, Object> result = roleService.pageRoleList(page, length, searchVal, orderDir, orderCol);
		result.put("draw", draw);
		return result;
    }

    @RequestMapping("/new")
    public String create(){
        return "role/new";
    }

    @RequestMapping(value="/new", method = RequestMethod.POST)
    @ResponseBody
    public String save(RoleDTO roleDTO) throws Exception{
    	roleDTO.setCreateTime(new Date());
    	roleService.save(roleDTO);
        log.info("新增->ID="+roleDTO.getId());
        return "1";
    }

    @RequestMapping(value="/update/{id}")
    public String update(Model model,@PathVariable Long id){
        RoleDTO roleDTO = roleService.findByPrimaryKey(id);
        model.addAttribute("role",roleDTO);
        return "role/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public String update(RoleDTO roleDTO) throws Exception{
    	roleDTO.setUpdateTime(new Date());
    	roleService.save(roleDTO);
        log.info("修改->ID="+roleDTO.getId());
        return "1";
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable Long id) throws Exception{
    	roleService.deleteByPrimaryKey(id);
        log.info("删除->ID="+id);
        return "1";
    }

    @GetMapping("/assignPermission/{id}")
    public String assignPermission(Model model,@PathVariable Long id) throws Exception{
    	model.addAttribute("id",id);
    	RoleDTO roleDTO = roleService.findByPrimaryKey(id);
        model.addAttribute("role",roleDTO);
        return "role/assignPermission";
    }
    
    @PostMapping("/assignPermission")
    @ResponseBody
    public String assignPermission(Long roleId,Long[] resourceIds) throws Exception{
    	log.info("roleId: {}",roleId);
    	log.info("resourceIds: {}",Arrays.toString(resourceIds));
    	RoleDTO roleDTO=roleService.findByPrimaryKey(roleId);
    	List<ResourceDTO> resourceDTOs=resourceService.findByIdIn(resourceIds);
    	for (ResourceDTO resourceDTO : resourceDTOs) {
			log.info("----resourceDTO: {}",resourceDTO.getId());
		}
    	roleDTO.setResourceDTOs(resourceDTOs);
    	roleService.save(roleDTO);
    	return "1";
    }
}
