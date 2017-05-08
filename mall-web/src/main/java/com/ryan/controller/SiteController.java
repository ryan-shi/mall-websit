package com.ryan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SiteController implements ErrorController {

	@RequestMapping("/")
	public String index(){
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