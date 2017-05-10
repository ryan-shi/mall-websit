package com.ryan.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ryan.dto.UserDTO;
import com.ryan.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	UserService userService;

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/register")
	public String toRegister() {
		return "user/register";
	}

	@PostMapping("/register")
	public String register(HttpServletRequest request, UserDTO userDTO) {
		log.info("userDTO: {}", userDTO);
		userDTO.setType(1);
		userDTO.setCreateTime(new Date());
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		String rawPass = userDTO.getPassword();
		userDTO.setPassword(bpe.encode(rawPass));
		try {
			UserDTO savedUserDTO = userService.save(userDTO);
			log.info("password:{}", rawPass);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDTO.getUsername(),
					rawPass);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authenticatedUser = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
			log.info("新增的用户ID=" + savedUserDTO.getId());
			return "redirect:/";
		} catch (Exception e) {
			e.printStackTrace();
			log.info("注册失败！");
			return "redirect:/user/register";
		}
	}
}
