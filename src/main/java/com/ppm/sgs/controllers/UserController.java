package com.ppm.sgs.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ppm.sgs.dtos.UserDto;
import com.ppm.sgs.dtos.UserUpdateDto;
import com.ppm.sgs.models.AppUserDetails;
import com.ppm.sgs.models.Role;
import com.ppm.sgs.models.User;
import com.ppm.sgs.services.RoleService;
import com.ppm.sgs.services.UserService;

@Controller
@RequestMapping(path = "/users")
@SessionAttributes("userType")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/all")
	public String getAll(ModelMap map) {
		List<User> users = userService.getAllUsers();
		map.addAttribute("users", users);
		map.addAttribute("userType", "all");
		return "users";
	}

	@GetMapping("/pending")
	public String getPending(ModelMap map) {
		List<User> users = userService.getPendingUsers();
		map.addAttribute("users", users);
		map.addAttribute("userType", "pending");
		return "users";
	}

	@GetMapping("/verified")
	public String getVerified(ModelMap map) {
		List<User> users = userService.getVerifiedUsers();
		map.addAttribute("users", users);
		map.addAttribute("userType", "verified");
		return "users";
	}

	@GetMapping("/{id}")
	public String updateForm(@PathVariable("id") String id, ModelMap map) {
		User user = userService.getById(id);

		List<Integer> roleIds = user.getRoles().stream().map(role -> role.getId()).collect(Collectors.toList());
		UserUpdateDto updateDto = new UserUpdateDto(user.getId(), user.getName(), user.getUsername(), user.getEmail(),
				user.getPassword(), user.getPassword(), user.getEnabled(), roleIds);
		map.addAttribute("updateUser", updateDto);
		return "user-update";
	}

	@PostMapping("/update")
	public String update(Authentication authentication, ModelMap map,
			@Valid @ModelAttribute("updateUser") UserUpdateDto updateDto, BindingResult result) {
		if (result.hasErrors()) {
			return "user-update";
		}
		User user = new User(updateDto.getId(), updateDto.getName(), updateDto.getUsername(), updateDto.getEmail(),
				updateDto.getPassword(), updateDto.getEnabled());
		List<Role> roles = roleService.getRolesByIds(updateDto.getRoleIds());
		user.setRoles(roles);

		String msg = userService.update(user);
		if (msg != null) {
			// failed to update; back to user update
			map.addAttribute("error", msg);
			map.addAttribute("updateUser", updateDto);
			return "user-update";
		}

		// update current user
		AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
		if (userDetails.getId().equals(user.getId())) {
			userDetails.setFullName(user.getName());
		}

		String userType = (String) map.get("userType");
		if (userType != null) {
			return getRedirectLink(userType);
		}

		return "redirect:all";
	}

	@GetMapping("/delete")
	public String delete(Authentication authentication, @RequestParam("id") String id) {
		AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

		// Do not delete if the user selected is active
		if (userDetails.getId().equals(id)) {
			return "redirect:all";
		}
		userService.deleteById(id);
		return "redirect:all";
	}

	@GetMapping("/add")
	public String addUserForm(ModelMap map) {
		map.addAttribute("newUser", new UserDto());
		return "add-user";
	}

	@PostMapping("/add")
	public String add(ModelMap map, @Valid @ModelAttribute("newUser") UserDto newUserDto, BindingResult result) {
		if (result.hasErrors()) {
			return "add-user";
		}

		User user = new User(null, newUserDto.getName(), newUserDto.getUsername(), newUserDto.getEmail(),
				newUserDto.getPassword(), newUserDto.getEnabled());
		List<Role> roles = roleService.getRolesByIds(newUserDto.getRoleIds());
		user.setRoles(roles);

		String msg = userService.save(user);
		if (msg != null) {
			// failed to update; back to user update
			map.addAttribute("error", msg);
			return "add-user";
		}

		String userType = (String) map.get("userType");
		if (userType != null) {
			return getRedirectLink(userType);
		}

		return "redirect:all";
	}

	@GetMapping("/search")
	public String search(ModelMap map, @RequestParam("id") String id, @RequestParam("name") String name) {
		List<User> users = userService.searchByIdOrName(id, name);
		map.addAttribute("users", users);
		map.addAttribute("userType", "all");
		return "users";
	}

	@ModelAttribute("roles")
	public List<Role> getAllRoles() {
		return roleService.getAllRoles();
	}

	@ModelAttribute("userTypes")
	public Map<String, String> getUserTypes() {
		Map<String, String> userTypes = new HashMap<>();
		userTypes.put("all", "All");
		userTypes.put("pending", "Pending");
		userTypes.put("verified", "Verified");
		return userTypes;
	}

	private String getRedirectLink(String userType) {

		if (userType.equals("pending")) {
			return "redirect:pending";
		} else if (userType.equals("verified")) {
			return "redirect:verified";
		} else {
			return "redirect:all";
		}

	}

}
