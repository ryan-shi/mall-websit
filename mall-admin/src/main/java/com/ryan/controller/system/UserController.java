package com.ryan.controller.system;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ryan.dto.DepartmentDTO;
import com.ryan.dto.RoleDTO;
import com.ryan.dto.UserDTO;
import com.ryan.security.SecurityUser;
import com.ryan.service.DepartmentService;
import com.ryan.service.RoleService;
import com.ryan.service.UserService;
import com.ryan.utils.FastdfsClient;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private Environment env;
	@Autowired
	RoleService roleService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	UserService userService;
	@Autowired
	FastdfsClient fastdfsClient;

	@RequestMapping("/index")
	public String index(Model model, Principal user, HttpSession session) throws Exception {
		return "user/index";
	}

	@RequestMapping(value = "/{id}")
	public String show(Model model, @PathVariable Long id) {
		UserDTO userDTO = userService.findByPrimaryKey(id);
		model.addAttribute("user", userDTO);
		return "user/view";
	}

	@RequestMapping(value = "/view/{id}")
	@ResponseBody
	public UserDTO show(@PathVariable Long id) {
		return userService.findByPrimaryKey(id);
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
			orderCol = "username";
			break;
		case "2":
			orderCol = "email";
			break;
		case "5":
			orderCol = "createTime";
			break;
		}
		int page = start / length;
		Map<String, Object> result = userService.pageUserList(page, length, searchVal, orderDir, orderCol);
		result.put("draw", draw);
		return result;
	}

	@RequestMapping("/new")
	public String create(Model model) {
		List<DepartmentDTO> departmentDTOs = departmentService.findAll();
		List<RoleDTO> roleDTOs = roleService.findAll();

		model.addAttribute("departments", departmentDTOs);
		model.addAttribute("roles", roleDTOs);
		return "user/new";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	@ResponseBody
	public String save(UserDTO userDTO, Long departmentDTOId, Long[] roleDTOIds) throws Exception {
		DepartmentDTO departmentDTO = departmentService.findByPrimaryKey(departmentDTOId);
		userDTO.setDepartmentDTO(departmentDTO);
		Set<RoleDTO> roleDTOs = roleService.findRoleIn(roleDTOIds);
		userDTO.setRoleDTOs(roleDTOs);
		userDTO.setCreateTime(new Date());
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		userDTO.setPassword(bpe.encode("123456"));
		userService.save(userDTO);
		log.info("新增的用户ID=" + userDTO.getId());
		return "1";
	}

	@RequestMapping(value = "/update/{id}")
	public String update(Model model, @PathVariable Long id) {
		UserDTO userDTO = userService.findByPrimaryKey(id);

		List<DepartmentDTO> departmentDTOs = departmentService.findAll();
		List<RoleDTO> roleDTOs = roleService.findAll();

		List<Long> rids = new ArrayList<>();
		for (RoleDTO roleDTO : userDTO.getRoleDTOs()) {
			rids.add(roleDTO.getId());
		}

		model.addAttribute("user", userDTO);
		model.addAttribute("departments", departmentDTOs);
		model.addAttribute("roles", roleDTOs);
		model.addAttribute("rids", rids);
		return "user/edit";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/update")
	@ResponseBody
	public String update(UserDTO userDTO, Long departmentDTOId, Long[] roleDTOIds) throws Exception {
		userDTO.setUpdateTime(new Date());
		DepartmentDTO departmentDTO = departmentService.findByPrimaryKey(departmentDTOId);
		userDTO.setDepartmentDTO(departmentDTO);
		Set<RoleDTO> roleDTOs = roleService.findRoleIn(roleDTOIds);
		userDTO.setRoleDTOs(roleDTOs);
		userService.save(userDTO);
		log.info("修改的ID=" + userDTO.getId());
		return "1";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		userService.deleteByPrimaryKey(id);
		log.info("删除的ID=" + id);
		return "1";
	}

	@RequestMapping("/account")
	public String toAccount(Model model, Principal principal) {
		log.info("principal: {}", principal);
		UserDTO userDTO = userService.findByUsername(principal.getName(), 0);// 0表示后台用户
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		userDTO.setAvator(fileUrlPrefix + userDTO.getAvator());
		model.addAttribute("user", userDTO);
		return "user/account";
	}

	@RequestMapping(value = "/uploadAvator", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile uploadfile,
			Long id) {
		log.info("user id: {}", id);
		UserDTO userDTO = userService.findByPrimaryKey(id);
		// String filename = uploadfile.getOriginalFilename();
		// log.info("文件原始名：{}", filename);
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		// int indexdot = filename.indexOf(".");
		// String suffix = filename.substring(indexdot);
		// String newFileName = userDTO.getUsername() + suffix;
		// log.info("new name: {}", newFileName);
		// String filepath = Paths.get(directory, newFileName).toString();
		// log.info("filepath: {}", filepath);
		// try {
		// BufferedOutputStream stream = new BufferedOutputStream(new
		// FileOutputStream(new File(filepath)));
		// stream.write(uploadfile.getBytes());
		// stream.close();
		//
		// userDTO.setAvator("/admin/upload/" + newFileName);
		// userService.save(userDTO);
		// } catch (Exception e) {
		// log.info("exception: {}", e.getMessage());
		// e.printStackTrace();
		// return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		// }
		try {
			String fileName = fastdfsClient.uploadFile(uploadfile);
			log.info("fileName:{}", fileName);
			userDTO.setAvator(fileName);
			userService.updateUser(userDTO);
			SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			userDetails.setAvator(fileUrlPrefix + fileName);

			return new ResponseEntity<>(fileUrlPrefix + fileName, HttpStatus.OK);
		} catch (Exception e) {
			log.info("上传出错！");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/updateAccount")
	@ResponseBody
	public ResponseEntity<?> updateAccount(Long id, String password, String newpassword, String newpasswdcf) {
		UserDTO userDTO = userService.findByPrimaryKey(id);
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		if (bpe.matches(password, userDTO.getPassword())) {
			if (newpasswdcf.equals(newpassword)) {
				userDTO.setPassword(bpe.encode(newpassword));
				userService.updateUser(userDTO);
				return new ResponseEntity<>("密码修改成功", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("新密码和确认密码不一致", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("原密码错误", HttpStatus.BAD_REQUEST);
		}
	}
}