package com.ppm.sgs.controllers;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ppm.sgs.dtos.LoginDto;
import com.ppm.sgs.dtos.UserDto;
import com.ppm.sgs.models.Role;
import com.ppm.sgs.models.User;
import com.ppm.sgs.services.RoleService;
import com.ppm.sgs.services.UserService;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/")
    public String showMainMenu(Model model, Principal p) {
        String name = p.getName();
        model.addAttribute("name", name);
        return "main-menu";
    }

    @GetMapping("/signin")
    public String loginForm(Model model, Principal p) {
        if(p != null) {
            return "redirect:/";
        }

        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(ModelMap map, Principal p) {
        if(p != null) {
            return "redirect:/";
        }

        map.addAttribute("newUser", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String add(ModelMap map,
            @Valid @ModelAttribute("newUser") UserDto newUserDto, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        User user = new User(null, newUserDto.getName(), newUserDto.getUsername(), newUserDto.getEmail(),
                newUserDto.getPassword(), false);
        List<Role> roles = roleService.getRolesByIds(newUserDto.getRoleIds());
        user.setRoles(roles);

        String msg = userService.save(user);
        if (msg != null) {
            // failed to update; back to user update
            map.addAttribute("error", msg);
            return "register";
        }

        return "redirect:signin";
    }

    @ModelAttribute("roles")
    public List<Role> roles() {
        return roleService.getAllRoles();
    }

}
