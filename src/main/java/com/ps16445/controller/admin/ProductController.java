package com.ps16445.controller.admin;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import com.ps16445.domain.Category;
import com.ps16445.domain.Product;
import com.ps16445.model.CategoryDto;
import com.ps16445.model.ProductDto;
import com.ps16445.service.CategoryService;
import com.ps16445.service.ProductService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/products")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	ServletContext application;

	@Autowired
	CategoryService categoryService;

	@PostMapping("saveProductUpdate")
	public ModelAndView saveProductUpdate(ModelMap model, @Valid @ModelAttribute("product") ProductDto dto,
			BindingResult result) {
		if (result.hasErrors()) {

			return new ModelAndView("admin/layout/product");
		}

		Product entity = new Product();
		BeanUtils.copyProperties(dto, entity);

		Category category = new Category();
		category.setCategoryId(dto.getCategoryId());
		entity.setCategory(category);
		System.out.println("category:  "+category);

		System.out.println("day ne" + dto.getImageFile().getOriginalFilename());
		if (!dto.getImageFile().isEmpty()) {

			String path = application.getRealPath("/");
			System.out.println("Macos: " + path);

			try {

				entity.setImage(dto.getImageFile().getOriginalFilename());

				String filePath = path + "/images/products/" + entity.getImage();
				dto.getImageFile().transferTo(Path.of(filePath));
				dto.setImageFile(null);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		entity.setAvailable(true);
		entity.setEnteredDate(new Date());
		productService.save(entity);

		model.addAttribute("message", "Product is success!");
		
		try {
			Optional<Product> optional = productService.findById(dto.getProductId());
			if(optional.isPresent()) {
				return new ModelAndView("forward:/admin/products/edit/"+dto.getProductId());
			}else {
				return new ModelAndView("admin/layout/product");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return new ModelAndView("admin/layout/product");
	}

	@GetMapping("listProduct")
	public String lisategory(ModelMap model, @RequestParam("p") Optional<Integer> p,
			@RequestParam(name = "name", required = false) String name) {

		Pageable pageable = PageRequest.of(p.orElse(0), 5);

		Page<Product> page = productService.findAll(pageable);

		if (StringUtils.hasText(name)) {
			page = productService.findByNameContaining(name, pageable);
		} else {
			page = productService.findAll(pageable);
		}

		System.out.println("Tong so trang" + page.getTotalPages());
		model.addAttribute("pages", page);
		return "admin/layout/list/listProduct";
	}

	@GetMapping("listProductAll")
	public String lisategoryAll(ModelMap model, @RequestParam("p") Optional<Integer> p,
			@RequestParam(name = "name", required = false) String name) {

		Pageable pageable = PageRequest.of(p.orElse(0), 1000);

		Page<Product> page = productService.findAll(pageable);
		
		
		ProductDto dto = new ProductDto();
		dto.setCheckEdit(true);
		model.addAttribute("product", dto);

		if (StringUtils.hasText(name)) {
			page = productService.findByNameContaining(name, pageable);
		} else {
			page = productService.findAll(pageable);
		}

		System.out.println("Tong so trang" + page.getTotalPages());
		model.addAttribute("pages", page);
		return "admin/layout/list/listProductAll";
	}

	@ModelAttribute("categories")
	public List<CategoryDto> getCategories() {
		return categoryService.findAll().stream().map(item -> {
			CategoryDto dto = new CategoryDto();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).toList();
	}

	@RequestMapping("edit/{productId}")
	public ModelAndView edit(ModelMap modelMap, 
			@PathVariable("productId") Long productId) {

		Optional<Product> otp = productService.findById(productId);

		ProductDto dto = new ProductDto();
		
		if (otp.isPresent()) {
			Product entity = otp.get();
			

			BeanUtils.copyProperties(entity, dto);
			//

			//
			System.out.println("day ne: "+entity.getImage());
			
			dto.setCategoryId(entity.getCategory().getCategoryId());
			dto.setCheckEdit(true);
//			entity.setImage(dto.getImageFile().getOriginalFilename());
//			dto.setImageFile(dto.getImageFile().getOriginalFilename());
			
			
			modelMap.addAttribute("product", dto);

			return new ModelAndView("admin/layout/product", modelMap);
		}

		modelMap.addAttribute("message", "Product is not existed");

		return new ModelAndView("admin/layout/product", modelMap);
	}

	@GetMapping("delete/{productId}")
	public ModelAndView delete(ModelMap modelMap, @PathVariable("productId") Long productId) {

		productService.deleteById(productId);

		modelMap.addAttribute("message", "Successful delete !");

		return new ModelAndView("forward:/admin/products/listProduct", modelMap);
	}

	@RequestMapping("sort/giam")
	public String sortGiam(Model model, @RequestParam("field") Optional<String> field) {
		List<Product> itemCheck = productService.findAll(); // truy van tat ca
		model.addAttribute("pro", itemCheck);
		Sort sort = Sort.by(Direction.DESC, field.orElse("name"));
		model.addAttribute("field", field.orElse("name"));
		List<Product> items = productService.findAll(sort);
		model.addAttribute("pages", items);
		
		ProductDto dto = new ProductDto();
		dto.setCheckEdit(false);
		model.addAttribute("product", dto);
		
		return "admin/layout/list/listProductAll";
	}

	@RequestMapping("sort/tang")
	public String sortTang(Model model, @RequestParam("field") Optional<String> field) {
		List<Product> itemCheck = productService.findAll(); // truy van tat ca
		model.addAttribute("pro", itemCheck);
		Sort sort = Sort.by(Direction.ASC, field.orElse("name"));
		model.addAttribute("field", field.orElse("name"));
		List<Product> items = productService.findAll(sort);
		model.addAttribute("pages", items);
		
		ProductDto dto = new ProductDto();
		dto.setCheckEdit(true);
		model.addAttribute("product", dto);
		
		return "admin/layout/list/listProductAll";
	}

}
