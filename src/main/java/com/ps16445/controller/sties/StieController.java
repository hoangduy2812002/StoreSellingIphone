package com.ps16445.controller.sties;

import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.ps16445.domain.Account;
import com.ps16445.domain.Order;
import com.ps16445.domain.OrderDetail;
import com.ps16445.domain.Product;
import com.ps16445.model.AccountDto;
import com.ps16445.model.CategoryDto;
import com.ps16445.model.OrderDetailDto;
import com.ps16445.model.ProductDto;
import com.ps16445.service.AccountService;
import com.ps16445.service.CategoryService;
import com.ps16445.service.CookieService;
import com.ps16445.service.OrderDetailService;
import com.ps16445.service.OrderService;
import com.ps16445.service.ProductService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("sties")
public class StieController {

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ServletContext application;

	@Autowired
	AccountService accountService;

	@Autowired
	private HttpSession session;

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	ServletContext context;

	@Autowired
	CookieService cookieService;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderDetailService orderDetailService;

	@ModelAttribute("categories")
	public List<CategoryDto> getCategories() {
		return categoryService.findAll().stream().map(item -> {
			CategoryDto dto = new CategoryDto();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).toList();
	}

	@GetMapping("home")
	public String index(ModelMap modelMap, @RequestParam("p") Optional<Integer> p,
			@RequestParam(name = "name", required = false) String name) {

		Pageable pageable = PageRequest.of(p.orElse(0), 6);

		Page<Product> page = productService.findAll(pageable);

		modelMap.addAttribute("product", new ProductDto());
		//

		if (StringUtils.hasText(name)) {
			page = productService.findByNameContaining(name, pageable);
		} else {
			page = productService.findAll(pageable);
		}

		modelMap.addAttribute("page", page);
		return "sties/layout/homeStie";
	}

	@GetMapping("forgotUsername")
	public String forgotUsername(ModelMap modelMap) {
		modelMap.addAttribute("accounts", new AccountDto());
		return "sties/layout/forgotUsername";
	}

	String email = "";
	String username = "";

	@PostMapping("forgotUsername")
	public ModelAndView forgotUsername(ModelMap modelMap, @Valid @ModelAttribute("accounts") AccountDto dto,
			BindingResult result) {
		Account account = accountService.forgotUsername(dto.getUsername());
		System.out.println("day nef: " + account);

		if (result.hasErrors()) {
			System.out.println("con loi");
			return new ModelAndView("sties/layout/forgotUsername");
		}

		if (account == null) {
			modelMap.addAttribute("message", "Username does not exist!");
			return new ModelAndView("sties/layout/forgotUsername", modelMap);
		}
		dto.setCheckEdit(true);
		email = account.getEmail();
		username = account.getUsername();
		modelMap.addAttribute("accounts", account);
		return new ModelAndView("redirect:/sties/getforgotEmail");
	}

	@RequestMapping("getforgotEmail")
	public String forgotEmail(ModelMap modelMap, @ModelAttribute("accounts") AccountDto dto) {
		System.out.println(email);
		modelMap.addAttribute("email", email);
		return "sties/layout/forgotEmail";
	}

	List<String> list = new ArrayList<>();

	// random so ngau nhien
	public String generationCode() {
		DecimalFormat df = new DecimalFormat("000000");
		Random rand = new Random();
		String code = df.format(rand.nextInt(1000000));
		list.add(code);
		return code;
	}

	@PostMapping("forgotEmail")
	public ModelAndView forgotEmail(ModelMap modelMap, @Valid @ModelAttribute("accounts") AccountDto dto,
			BindingResult result) {
		System.out.println("email ne: " + email);
		try {
			// Tạo mail
			MimeMessage mail = javaMailSender.createMimeMessage();
			// Sử dụng lớp trợ giúp
			MimeMessageHelper helper = new MimeMessageHelper(mail, true, "utf-8");
			helper.setFrom("khongcanten411@gmail.com", "khongcanten411@gmail.com");
			helper.setTo(email);
			helper.setReplyTo("khongcanten411@gmail.com", "khongcanten411@gmail.com");
			helper.setSubject("Mã xác nhận");
			helper.setText(generationCode());

			// send mail
			javaMailSender.send(mail);
			modelMap.addAttribute("message", "Send gmail successfully");

		} catch (Exception e) {
			modelMap.addAttribute("message", "Send gmail fail!");
			e.printStackTrace();
		}

		return new ModelAndView("redirect:/sties/forgotgetCode");

	}

	@GetMapping("forgotgetCode")
	public String forgotgetCode(ModelMap modelMap) {
		modelMap.addAttribute("accounts", new AccountDto());
		return "sties/layout/forgotCode";
	}

	@PostMapping("forgotCode")
	public String forgotCode(ModelMap modelMap, @RequestParam("code") String code) {
		if (code.equals(list.get(0))) {
			System.out.println("dung roi");
			return ("redirect:/sties/forgotgetPassword");
		} else {
			System.out.println("sai roi");
			modelMap.addAttribute("message", "The code is not correct");
			return ("sties/layout/forgotCode");
		}
	}

	@GetMapping("forgotgetPassword")
	public String forgotgetPassword(ModelMap modelMap) {
		modelMap.addAttribute("accounts", new AccountDto());
		return ("sties/layout/forgotPassword");
	}

	@PostMapping("forgotPassword")
	public String forgotPassword(ModelMap modelMap, @ModelAttribute("accounts") AccountDto dto,
			@RequestParam("confirmPassword") String confirmPassword) {
		if (!confirmPassword.equals(dto.getPassword())) {
			modelMap.addAttribute("message", "Enter the wrong password to confirm!");
			return ("sties/layout/forgotPassword");
		} else {
			System.out.println("password: " + dto.getPassword());
			System.out.println("username: " + username);
			accountService.forGotPassword(dto.getPassword(), username);

			return "redirect:/sties/login";
		}

	}

	@GetMapping("searchId")
	public String search(ModelMap modelMap, @RequestParam("p") Optional<Integer> p,
			@RequestParam(name = "categoryId", required = false) Long categoryId) {

		Pageable pageable = PageRequest.of(p.orElse(0), 6);

		Page<Product> page = productService.findAll(pageable);

		modelMap.addAttribute("product", new ProductDto());

		if (StringUtils.hasText(categoryId.toString())) {
			page = productService.findByCategoryIdContaining(categoryId, pageable);
		} else {
			page = productService.findAll(pageable);
		}

		modelMap.addAttribute("page", page);
		return "sties/layout/homeStie";
	}

	@GetMapping("searchPrice")
	public String searchPrice(ModelMap modelMap, @RequestParam("p") Optional<Integer> p,
			@RequestParam(name = "range_1") Double min, @RequestParam(name = "range_2") Double max) {

		Pageable pageable = PageRequest.of(p.orElse(0), 6);

		Page<Product> page = productService.findAll(pageable);

		modelMap.addAttribute("product", new ProductDto());

//		if(StringUtils.hasText(categoryId.toString())) {
//			page = productService.findByCategoryIdContaining(categoryId, pageable);
//		}else {
		page = productService.findByPriceContaining(min, max, pageable);
//		}

		modelMap.addAttribute("page", page);
		return "sties/layout/homeStie";
	}

	@GetMapping("home/page/{pageNumber}")
	public String paginate(ModelMap model, @RequestParam("name") Optional<Integer> p) {

		return "sties/layout/homeStie";
	}

	

//	Long userId;
	@RequestMapping("login")
	public String login(ModelMap modelMap) {
		modelMap.addAttribute("accounts", new AccountDto());
		return "sties/layout/login";
	}

	@PostMapping("checklogin")
	public ModelAndView checklogin(ModelMap modelMap, @Valid @ModelAttribute("accounts") AccountDto dto,
			BindingResult result) {
		if (result.hasErrors()) {
			System.out.println("con loi");
			return new ModelAndView("sties/layout/login");
		}
		Account account = accountService.login(dto.getUsername(), dto.getPassword());
		if (account == null) {
			cookieService.remove("user");
			modelMap.addAttribute("message", "Wrong username or password!");
			return new ModelAndView("forward:/sties/login", modelMap);
		}
//		userId=account.getUserId();
//		cookieService.add("user", account.getUsername(), 10);
		session.setAttribute("users", account.getUserId());
		 System.out.println("se: =>>"+session.getAttribute("users"));

		return new ModelAndView("redirect:/sties/home");
	}

	@GetMapping("register")
	public String register(ModelMap modelMap) {
		modelMap.addAttribute("accounts", new AccountDto());
		return "sties/layout/register";
	}

	@PostMapping("saveAccountUpdate")
	public String save(ModelMap modelMap, @Valid @ModelAttribute("accounts") AccountDto dto, BindingResult result) {

		if (result.hasErrors()) {
			return "sties/layout/register";
		}
		if (!dto.getConfirmPassword().equals(dto.getPassword())) {
			modelMap.addAttribute("messagePassword", "confirm password does not match password!");
			return "sties/layout/register";
		} else {
			Account entity = new Account();
			BeanUtils.copyProperties(dto, entity);

			entity.setAdmin(false);

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
		}

		modelMap.addAttribute("message", "Account is success!");

		return "sties/layout/register";
	}

	@GetMapping("update")
	public String update() {
		return "sties/layout/update";
	}

	
	
	@RequestMapping("cart")
	public String cart(Model model) {
		Collection<OrderDetailDto> cartItems = map.values();
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("NoItems", getCount());

		model.addAttribute("total", getAmount());

		return "sties/layout/cartShop";
	}
	
	public double getAmount() {
		return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
	}

	public int getCount() {
		if (map.isEmpty())
			return 0;
		return map.values().size();
	}

	@GetMapping("add/{productId}")
	public String add(@PathVariable("productId") Long productId, Model model) {
		Product product = productService.cart(productId);

		System.out.println("id nef: " + productId);
		if (product != null) {
			OrderDetailDto item = new OrderDetailDto();

			BeanUtils.copyProperties(product, item);
			item.setQuantity(1);
			addOD(item);

		}
		return "redirect:/sties/cart";
	}

	@GetMapping("tang/{productId}")
	public String tang(@PathVariable("productId") Long productId, Model model) {
		Product product = productService.cart(productId);
		if (product != null) {
			OrderDetailDto item = new OrderDetailDto();

			BeanUtils.copyProperties(product, item);
			fctang(item);

		}
		return "redirect:/sties/cart";
	}
	
	public void fctang(OrderDetailDto item) {
		OrderDetailDto exisCartItem = map.get(item.getProductId());

		if (exisCartItem != null) {
			exisCartItem.setQuantity(1 + exisCartItem.getQuantity());
		} else {
			map.put(item.getProductId(), item);
		}

	}

	@GetMapping("giam/{productId}")
	public String giam(@PathVariable("productId") Long productId, Model model) {
		Product product = productService.cart(productId);
		if (product != null) {
			OrderDetailDto item = new OrderDetailDto();
			BeanUtils.copyProperties(product, item);
			fcgiam(item);

		}
		return "redirect:/sties/cart";
	}
	public void fcgiam(OrderDetailDto item) {
		OrderDetailDto exisCartItem = map.get(item.getProductId());

		if (exisCartItem != null) {
			exisCartItem.setQuantity(exisCartItem.getQuantity() - 1);
		} else {
			map.put(item.getProductId(), item);
		}

	}

	@GetMapping("remove/{productId}")
	public String remove(@PathVariable("productId") Long productId) {
		map.remove(productId);

		return "redirect:/sties/cart";
	}

	@GetMapping("clear")
	public String clear() {
		map.clear();
		return "redirect:/sties/cart";
	}

	// code tiep
	@PostMapping("checkout")
	public String checkout(ModelMap model, @RequestParam("address") String address) {
		try {
			if (session.getAttribute("users") == null) {
				model.addAttribute("message", "Please login!");
				System.out.println("Vui long dang nhap");
				return "forward:/sties/cart";
			}
			Long userId =(Long) session.getAttribute("users");
			Account account = new Account();
			account.setUserId(userId);
			Order order = new Order();
			order.setEnteredDate(new Date());
			order.setAccount(account);
			order.setQuantity(getCount());
			order.setTotal(getAmount());
			
			Order orderId = orderService.save(order);
			//
			for(Long i : map.keySet()) {		
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setOrder(orderId);
				Product product = new Product();
				product.setProductId(map.get(i).getProductId());
				OrderDetailDto exisCartItem = map.get(map.get(i).getProductId());
				orderDetail.setQuantity(exisCartItem.getQuantity());
				orderDetail.setUnitPrice(map.get(i).getUnitPrice());
				orderDetail.setProduct(product);
				orderDetail.setAddress(address);
				if(address.equalsIgnoreCase("")) {
					model.addAttribute("messageAddress", "Please enter the address !");
					return "forward:/sties/cart";
				}
				orderDetailService.save(orderDetail);
			}
			System.out.println("Mua hang thanh cong");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/sties/clear";
	}
	
	private Map<Long, OrderDetailDto> map = new HashMap<Long, OrderDetailDto>();
	
	public void addOD(OrderDetailDto item) {
		OrderDetailDto exisCartItem = map.get(item.getProductId());

		if (exisCartItem != null) {
			exisCartItem.setQuantity(item.getQuantity() + exisCartItem.getQuantity());
		} else {
			map.put(item.getProductId(), item);
		}

	}

}
