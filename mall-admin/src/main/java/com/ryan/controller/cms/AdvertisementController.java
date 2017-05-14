package com.ryan.controller.cms;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ryan.dto.AdvertisementDTO;
import com.ryan.dto.ProductDTO;
import com.ryan.service.AdvertisementService;
import com.ryan.service.ProductService;
import com.ryan.utils.FastdfsClient;

@Controller
@RequestMapping("/advert")
public class AdvertisementController {

	private static final Logger log = LoggerFactory.getLogger(AdvertisementController.class);

	@Autowired
	private Environment env;
	@Autowired
	AdvertisementService advertisementService;
	@Autowired
	ProductService productService;
	@Autowired
	FastdfsClient fastdfsClient;

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
	public String save(AdvertisementDTO advertisementDTO, Long productId,@RequestParam("pictureFile") MultipartFile pictureFile) throws Exception {
		ProductDTO productDTO = productService.findByPrimaryKey(productId);
		if (productDTO == null) {
			log.info("输入的商品不存在，productId:{}", productId);
			return "2";// 商品找不到
		} else {
			advertisementDTO.setCreateTime(new Date());
			advertisementDTO.setProductDTO(productDTO);
			try {
				log.info("pictureFile:{}",pictureFile.isEmpty());
				if(!pictureFile.isEmpty()){
					String fileName = fastdfsClient.uploadFile(pictureFile);
					log.info("fileName:{}", fileName);
					advertisementDTO.setPicture(fileName);
				}
				advertisementService.save(advertisementDTO);
				log.info("新增->ID=" + productDTO.getId());
				return "1";
			} catch (Exception e) {
				log.info("上传出错！");
				e.printStackTrace();
				return "2";
			}
		}
	}

	@GetMapping("/update/{id}")
	public String update(@PathVariable Long id, Model model) {
		AdvertisementDTO advertisementDTO = advertisementService.findByPrimaryKey(id);
		String oldPicUrl=advertisementDTO.getPicture();
		model.addAttribute("oldPicture", oldPicUrl);
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		advertisementDTO.setPicture(fileUrlPrefix+oldPicUrl);
		model.addAttribute("advertisement", advertisementDTO);
		return "advert/edit";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(AdvertisementDTO advertisementDTO, Long productId,@RequestParam("pictureFile") MultipartFile pictureFile,String oldPicture) {
		ProductDTO productDTO = productService.findByPrimaryKey(productId);
		if (productDTO == null) {
			log.info("输入的商品不存在，productId:{}", productId);
			return "2";// 商品找不到
		} else {
			String picture=advertisementDTO.getPicture();
			log.info("pic: {}",picture);
			log.info("oldPicUrl:{}",oldPicture);
			if(picture.equals("")){
				try {
//					fastdfsClient.deleteFile(oldPicture);
					String fileName = fastdfsClient.uploadFile(pictureFile);
					log.info("fileName:{}", fileName);
					advertisementDTO.setPicture(fileName);
					advertisementDTO.setUpdateTime(new Date());
					advertisementDTO.setProductDTO(productDTO);
					advertisementService.save(advertisementDTO);
					log.info("新增->ID=" + advertisementDTO.getId());
					return "1";
				} catch (Exception e) {
					log.info("上传出错！");
					e.printStackTrace();
					return "2";
				}
			}else{
				advertisementDTO.setPicture(oldPicture);
				advertisementDTO.setUpdateTime(new Date());
				advertisementDTO.setProductDTO(productDTO);
				advertisementService.save(advertisementDTO);
				log.info("更新->ID=" + advertisementDTO.getId());
				return "1";
			}
		}
	}

	@GetMapping("/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id) {
		log.info("delete advert id:{}", id);
		advertisementService.deleteByPrimaryKey(id);
		return "1";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {
		AdvertisementDTO advertisementDTO = advertisementService.findByPrimaryKey(id);
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		advertisementDTO.setPicture(fileUrlPrefix+advertisementDTO.getPicture());
		model.addAttribute("advertisement", advertisementDTO);
		return "advert/view";
	}
}
