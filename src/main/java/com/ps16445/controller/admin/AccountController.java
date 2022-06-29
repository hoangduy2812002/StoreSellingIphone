package com.ps16445.controller.admin;

import java.nio.file.Path;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import com.ps16445.domain.Account;
import com.ps16445.domain.Product;
import com.ps16445.model.AccountDto;
import com.ps16445.model.ProductDto;
import com.ps16445.service.AccountService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/accounts")
public class AccountController {

	@Autowired
	AccountService accountService;

	@Autowired
	ServletContext application;

	@PostMapping("saveAccountUpdate")
	public String save(ModelMap modelMap, 
			@Valid @ModelAttribute("accounts") AccountDto dto,
			BindingResult result) {

		if (result.hasErrors()) {
			if (!dto.getConfirmPassword().equals(dto.getPassword())) {
				modelMap.addAttribute("messagePassword", "Does not match the password");
				return "admin/layout/account";
			}
			return "admin/layout/account";
		}
		
//		
			Account entity = new Account();
			BeanUtils.copyProperties(dto, entity);

			entity.setAdmin(true);

			if (!dto.getImageFile().isEmpty()) {

				String path = application.getRealPath("/");
				System.out.println("Macos: " + path);

				try {

					entity.setImage(dto.getImageFile().getOriginalFilename());

					String filePath = path + "/images/accounts/" + entity.getImage();
					dto.getImageFile().transferTo(Path.of(filePath));
					dto.setImageFile(null);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			
			accountService.save(entity);
			modelMap.addAttribute("message", "Account is success!");
			
			try {
				Optional<Account> optional = accountService.findById(dto.getUserId());
				if(optional.isPresent()) {
					return "forward:/admin/accounts/edit/" + dto.getUserId();
				}else {
					return "admin/layout/account";
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return "admin/layout/account";
	}

	@RequestMapping("listAccount")
	public String listAccoount(ModelMap model, @RequestParam("p") Optional<Integer> p,
			@RequestParam(name = "username", required = false) String username) {

		Pageable pageable = PageRequest.of(p.orElse(0), 5);

		Page<Account> page = accountService.findAll(pageable);

		if (StringUtils.hasText(username)) {
			page = accountService.findByUsernameContaining(username, pageable);
		} else {
			page = accountService.findAll(pageable);
		}

		System.out.println("Tong so trang" + page.getTotalPages());
		model.addAttribute("page", page);
		return "admin/layout/list/listAccount";
	}

	@RequestMapping("edit/{userId}")
	public ModelAndView edit(ModelMap modelMap, @PathVariable("userId") Long userId) {

		Optional<Account> otp = accountService.findById(userId);

		AccountDto dto = new AccountDto();

		if (otp.isPresent()) {
			Account entity = otp.get();

			BeanUtils.copyProperties(entity, dto);

			dto.setCheckEdit(true);

			modelMap.addAttribute("accounts", dto);

			return new ModelAndView("admin/layout/account", modelMap);
		}

		modelMap.addAttribute("message", "Product is not existed");

		return new ModelAndView("admin/layout/account", modelMap);
	}
	
	@GetMapping("delete/{userId}")
	public ModelAndView delete(ModelMap modelMap, @PathVariable("userId") Long userId){

		accountService.deleteById(userId);

		modelMap.addAttribute("message", "Successful delete !");

		return new ModelAndView("forward:/admin/accounts/listAccount", modelMap);
	}
}
