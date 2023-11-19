package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloBootController {
	@GetMapping("/hello")
	public ModelAndView hello() {
		ModelAndView mav = new ModelAndView("helloboot");
		mav.addObject("name", "태현");
		return mav;
	}
}
