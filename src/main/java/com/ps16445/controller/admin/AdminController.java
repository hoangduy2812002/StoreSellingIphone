package com.ps16445.controller.admin;

import java.util.List;
import java.util.Optional;

import com.ps16445.domain.Account;
import com.ps16445.domain.Order;
import com.ps16445.domain.OrderDetail;
import com.ps16445.domain.Product;
import com.ps16445.model.AccountDto;
import com.ps16445.model.CategoryDto;
import com.ps16445.model.ProductDto;
import com.ps16445.service.AccountService;
import com.ps16445.service.CategoryService;
import com.ps16445.service.OrderDetailService;
import com.ps16445.service.OrderService;
import com.ps16445.service.ProductService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;

	@Autowired
	AccountService accountService;

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	OrderService orderService;

	@ModelAttribute("categories")
	public List<CategoryDto> getCategories() {
		return categoryService.findAll().stream().map(item -> {
			CategoryDto dto = new CategoryDto();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).toList();
	}

	@GetMapping("home")
	public String home(ModelMap modelMap) {
		modelMap.addAttribute("accounts", accountService.count());
		modelMap.addAttribute("categories", categoryService.count());
		modelMap.addAttribute("products", productService.count());
		modelMap.addAttribute("orders", orderService.count());
		return "admin/layout/home";
	}

	@GetMapping("account")
	public String Customer(ModelMap modelMap) {
		modelMap.addAttribute("accounts", new AccountDto());
		return "admin/layout/account";
	}

//	@GetMapping("listAccount")
//	public String listCustomer(ModelMap modelMap) {
//		List<Account> list = accountService.findAll();
//
//		modelMap.addAttribute("accounts", list);
//		return "admin/layout/list/listAccount";
//	}

	@GetMapping("product")
	public String product(ModelMap model) {
		model.addAttribute("product", new ProductDto());
		return "admin/layout/product";
	}

	@GetMapping("listProduct")
	public String listProduct(ModelMap modelMap) {
		List<Product> list = productService.findAll();

		modelMap.addAttribute("products", list);

		return "admin/layout/list/listProduct";
	}

	@GetMapping("categories")
	public String categories(ModelMap model) {
		model.addAttribute("category", new CategoryDto());
		return "admin/layout/category";
	}

	@RequestMapping("order")
	public String order(ModelMap model, @RequestParam("p") Optional<Integer> p,
			@RequestParam(name = "name", required = false) String name) {
		model.addAttribute("product", new ProductDto());

		Pageable pageable = PageRequest.of(p.orElse(0), 5);
		Page<Order> page = orderService.findAll(pageable);

		System.out.println("Tong so trang" + page.getTotalPages());
		model.addAttribute("page", page);
		return "admin/layout/list/listOrder";
	}

	@GetMapping("delete/{orderId}")
	public ModelAndView delete(ModelMap modelMap, @PathVariable("orderId") Long orderId) {

		orderService.deleteById(orderId);

		modelMap.addAttribute("message", "Successful delete !");

		return new ModelAndView("forward:/admin/order", modelMap);
	}

	Long orId;

	@RequestMapping("orderDetail/{orderId}")
	public String orderDetail(ModelMap modelMap, @RequestParam("p") Optional<Integer> p,
			@PathVariable("orderId") Long orderId) {

		Pageable pageable = PageRequest.of(p.orElse(0), 5);

		Page<OrderDetail> page = orderDetailService.findAll(pageable);

		orId = orderId;
		System.out.println("so" + orId);
		page = orderDetailService.findByorderIdContaining(orderId, pageable);

		modelMap.addAttribute("page", page);
		return "admin/layout/list/listOrderDetail";
	}

	@RequestMapping("listOrderDetail")
	public String orderDetail() {
		return "admin/layout/list/listOrderDetail";
	}

	@GetMapping("deleteOrderDetail/{orderDetailId}")
	public ModelAndView deleteOrderDetail(ModelMap modelMap, @PathVariable("orderDetailId") Long orderDetailId) {

		orderDetailService.deleteById(orderDetailId);

		modelMap.addAttribute("message", "Successful delete !");

		return new ModelAndView("forward:/admin/order");

	}
}
