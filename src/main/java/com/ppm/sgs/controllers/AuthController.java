package com.ppm.sgs.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ppm.sgs.dtos.LoginDto;

@Controller
@RequestMapping("/")
public class AuthController {
    
    @GetMapping("/")
    public String showMainMenu(Model model, Principal p) {
        String name = p.getName();
        model.addAttribute("name", name);
        return "main-menu";
    }
    
    @GetMapping("/signin")
    public String loginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

}
