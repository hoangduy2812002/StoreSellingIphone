package com.ps16445.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import com.ps16445.domain.Category;
import com.ps16445.model.CategoryDto;
import com.ps16445.service.CategoryService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequestMapping("admin/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@PostMapping("saveUpdateCategories")
	public ModelAndView saveOrderUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryDto dto,
			BindingResult result) {
		if (result.hasErrors()) {

			return new ModelAndView("admin/layout/category");
		}
		Category entity = new Category();
		BeanUtils.copyProperties(dto, entity);

		categoryService.save(entity);

		model.addAttribute("message", "Category success!");
		return new ModelAndView("admin/layout/category", model);
	}

	@GetMapping("edit/{categoryId}")
	public ModelAndView edit(ModelMap modelMap, @PathVariable("categoryId") Long categoryId) {

		Optional<Category> otp = categoryService.findById(categoryId);

		CategoryDto dto = new CategoryDto();

		if (otp.isPresent()) {
			Category entity = otp.get();

			BeanUtils.copyProperties(entity, dto);
		
			dto.setCheckEdit(true);

			modelMap.addAttribute("category", dto);	

			return new ModelAndView("admin/layout/category", modelMap);
		}

		modelMap.addAttribute("message", "Category is not existed");

		return new ModelAndView("admin/layout/category", modelMap);
	}

	@GetMapping("delete/{categoryId}")
	public ModelAndView delete(ModelMap modelMap, @PathVariable("categoryId") Long categoryId) {

		categoryService.deleteById(categoryId);

		modelMap.addAttribute("message", "Successful delete !");

		return new ModelAndView("forward:/admin/categories/listCategories", modelMap);
	}

	@GetMapping("listCategories")
	public String listPage(ModelMap modelMap, 
			@RequestParam("p") Optional<Integer> p,
			@RequestParam(name = "name", required = false) String name) {
		
		Pageable pageable = PageRequest.of(p.orElse(0), 5);
		
		Page<Category> page = categoryService.findAll(pageable);
		
		if(StringUtils.hasText(name)) {
			page = categoryService.findByNameContaining(name, pageable);
		}else {
			page = categoryService.findAll(pageable);
		}

		
		modelMap.addAttribute("page", page);
		return "admin/layout/list/listCategory";
	}
	
}
