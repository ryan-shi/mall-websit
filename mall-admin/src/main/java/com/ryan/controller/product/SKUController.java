package com.ryan.controller.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ryan.dto.ProductDTO;
import com.ryan.dto.SKUDTO;
import com.ryan.dto.SpecOptionDTO;
import com.ryan.service.ProductService;
import com.ryan.service.SKUService;
import com.ryan.service.SpecOptionService;
import com.ryan.utils.FastdfsClient;

@Controller
@RequestMapping("/sku")
public class SKUController {

	private static final Logger log = LoggerFactory.getLogger(SKUController.class);

	@Autowired
	SKUService skuService;
	@Autowired
	SpecOptionService specOptionService;
	@Autowired
	ProductService productService;
	@Autowired
	FastdfsClient fastdfsClient;
	@Autowired
	private Environment env;

	@RequestMapping("/makeSku")
	@ResponseBody
	public String makeSku(SKUDTO skuDTO, Long[] specOptionId, Long productId,
			@RequestParam("pictureFile") MultipartFile pictureFile) {
		log.info("productId:{}", productId);
		log.info("specOptionId:{}", Arrays.toString(specOptionId));
		Arrays.sort(specOptionId);
		SpecOptionDTO specOptionDTO = null;
		StringBuffer specOptionIds = new StringBuffer();
		ProductDTO productDTO = productService.findByPrimaryKey(productId);
		StringBuffer name = new StringBuffer(productDTO.getName());
		List<SpecOptionDTO> specOptions = new ArrayList<>();
		for (int i = 0; i < specOptionId.length; i++) {
			specOptionDTO = specOptionService.findByPrimaryKey(specOptionId[i]);
			specOptions.add(specOptionDTO);
			name.append(" " + specOptionDTO.getName());
			if (i + 1 == specOptionId.length) {
				specOptionIds.append(specOptionId[i]);
			} else {
				specOptionIds.append(specOptionId[i] + ":");
			}
		}

		if (CollectionUtils.isEmpty(skuService.findBySpecOptionIds(specOptionIds.toString()))) {
			if (!pictureFile.isEmpty()) {
				try {
					String fileName = fastdfsClient.uploadFile(pictureFile);
					log.info("fileName:{}", fileName);
					skuDTO.setPicture(fileName);
				} catch (Exception e) {
					e.printStackTrace();
					return "2";
				}
			}
			skuDTO.setProductDTO(productDTO);
			skuDTO.setCreateTime(new Date());
			log.info("sku name:{}", name.toString());
			skuDTO.setName(name.toString());
			skuDTO.setSpecOptionIds(specOptionIds.toString());
			skuDTO.setSpecOptionDTOs(specOptions);
			skuService.save(skuDTO);
			return "1";
		} else {
			return "2";
		}
	}

	@RequestMapping("/index")
	public String index() {
		return "sku/index";
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
			orderCol = "price";
			break;
		default:
			orderCol = "createTime";
			break;
		}
		int page = start / length;
		Map<String, Object> result = skuService.pageSKUList(page, length, searchVal, orderDir, orderCol);
		result.put("draw", draw);
		return result;
	}

	@RequestMapping(value = "/update/{id}")
	public String update(Model model, @PathVariable Long id) {
		SKUDTO skuDTO = skuService.findByPrimaryKey(id);
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		skuDTO.setPicture(fileUrlPrefix + skuDTO.getPicture());
		model.addAttribute("sku", skuDTO);
		String oldPicUrl = skuDTO.getPicture();
		model.addAttribute("oldPicture", oldPicUrl);
		return "sku/edit";
	}

	@PostMapping("/update")
	@ResponseBody
	public String update(SKUDTO skuDTO, @RequestParam("pictureFile") MultipartFile pictureFile, String oldPicture) {
		SKUDTO oldSku = skuService.findByPrimaryKey(skuDTO.getId());
		log.info("options: {}", oldSku.getSpecOptionDTOs());
		skuDTO.setSpecOptionDTOs(oldSku.getSpecOptionDTOs());
		skuDTO.setProductDTO(oldSku.getProductDTO());
		skuDTO.setUpdateTime(new Date());
		String picture = skuDTO.getPicture();
		log.info("pic: {}", picture);
		log.info("oldPicUrl:{}", oldPicture);
		if (picture.equals("")) {
			try {
				// fastdfsClient.deleteFile(oldPicture);
				String fileName = fastdfsClient.uploadFile(pictureFile);
				log.info("fileName:{}", fileName);
				skuDTO.setPicture(fileName);
				skuService.save(skuDTO);
				log.info("新增->ID=" + skuDTO.getId());
				return "1";
			} catch (Exception e) {
				return "2";
			}
		} else {
			skuDTO.setPicture(oldPicture);
			skuService.save(skuDTO);
			log.info("更新id：{}", skuDTO.getId());
			return "1";
		}
	}

	@RequestMapping(value = "/{id}")
	public String show(Model model, @PathVariable Long id) {
		SKUDTO skuDTO = skuService.findByPrimaryKey(id);
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		skuDTO.setPicture(fileUrlPrefix + skuDTO.getPicture());
		model.addAttribute("sku", skuDTO);
		return "sku/view";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@PathVariable Long id) throws Exception {
		skuService.deleteByPrimaryKey(id);
		log.info("删除->ID=" + id);
		return "1";
	}
}
