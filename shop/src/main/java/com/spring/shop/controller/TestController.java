package com.spring.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	@GetMapping("/test2")
	public void test2(Model model) {
		model.addAttribute("msg", "String Message 한글");
		
	}
}
