package com.ryan.controller.system;

import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ryan.security.SecurityUser;

@Controller
public class SiteController implements ErrorController {

	private static final Logger log = LoggerFactory.getLogger(SiteController.class);

	@Autowired
	private Environment env;
	
	@RequestMapping("/")
	public String index(){
		String fileUrlPrefix = env.getProperty("file.path.prefix");
		SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String avator=userDetails.getAvator();
		if(!avator.startsWith(fileUrlPrefix)){
			userDetails.setAvator(fileUrlPrefix + userDetails.getAvator());
		}
		log.info("index head: {}",userDetails.getAvator());
		return "index";
	}
	
	@RequestMapping(value="/error")
    public String handleError(){
        return "404";
    }
	
	@Override
	public String getErrorPath() {
		return "404";
	}

	@RequestMapping(value="/deny")
    public String deny(){
        return "deny";
    }
}