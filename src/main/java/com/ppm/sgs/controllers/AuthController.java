package com.ppm.sgs.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ppm.sgs.dtos.LoginDto;

@Controller
@RequestMapping("/")
public class AuthController {
    
    @GetMapping("/")
    public ModelAndView showMainMenu(ModelAndView mav, Principal p) {
        String name = p.getName();
        mav.addObject("name", name);
        mav.setViewName("main-menu");
        return mav;
    }
    
    @GetMapping("/signin")
    public String loginForm() {
        return "login";
    }
    
    @ModelAttribute("loginDto")
    public LoginDto loginDto() {
        return new LoginDto();
    }
}
