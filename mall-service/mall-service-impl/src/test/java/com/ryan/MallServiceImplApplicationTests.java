package com.ryan;

import org.junit.Test;
import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ryan.repository.SpecRepository;
import com.ryan.service.SpecService;
import com.ryan.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallServiceImplApplicationTests {

//	private static final Logger log = LoggerFactory.getLogger(MallServiceImplApplicationTests.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	SpecService specService;
	
	@Autowired
	SpecRepository specRepository;
	
	@Test
	public void contextLoads() {
//		UserDTO userDTO=userService.findByUsername("admin");
//		log.info("-----user: {}",userDTO);
//		log.info("-----user roleDtos: {}",userDTO.getRoleDTOs());
//		List<SpecDTO> specDTOs=specService.findByCatalogOrderBySortAsc(new Long(2));
//		log.info("specDTOs:{}",specDTOs);
//		specService.deleteByPrimaryKey(new Long(15));
//		specRepository.delete(new Long(15));
//		Spec spec=specRepository.findOne(new Long(15));
//		specRepository.delete(spec);
	}

}
