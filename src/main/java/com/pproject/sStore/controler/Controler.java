package com.pproject.sStore.controler;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pproject.sStore.model.Address;
import com.pproject.sStore.model.Product;
import com.pproject.sStore.model.ProductItem;
import com.pproject.sStore.model.User;
import com.pproject.sStore.repository.CartRepository;
import com.pproject.sStore.service.CartService;
import com.pproject.sStore.service.ProductService;
import com.pproject.sStore.service.UserService;

@Controller
@RequestMapping(path = "/sStore")
public class Controler {

	private final UserService userService;
	private final ProductService productService;
	private final CartService cartService;

	@Autowired
	public Controler(UserService userService, ProductService productService, CartService cartService) {
		this.userService = userService;
		this.productService = productService;
		this.cartService = cartService;
	}

	@GetMapping
	public String index(Model model) {
		List<Product> featuredProducts = productService.getFeaturedProducts();
		List<Product> newProducts = productService.getNewProducts();
		model.addAttribute("featuredProducts", featuredProducts);
		model.addAttribute("newProducts", newProducts);
		return "index";
	}

	@GetMapping(value = "/shop")
	public String shop(Model model) {
		return "shop";
	}

	@GetMapping(value = "/sproduct")
	public String sproduct(Model model, Long id) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		return "sproduct";
	}

	@GetMapping(value = "/about")
	public String about(Model model) {
		return "about";
	}

	@GetMapping(value = "/blog")
	public String blog(Model model) {
		return "blog";
	}

	@GetMapping(value = "/contact")
	public String contact(Model model) {
		return "contact";
	}

	@GetMapping(value = "/cart")
	public String cart(Model model) {
		return "cart";
	}

	@PostMapping("/register")
	public String register(User user, Address address, HttpSession session, Model model) {
		try {
			User u = userService.register(user, address);
			session.setAttribute("USER", u);
			return "redirect:/sStore/";
		} catch (Exception e) {
			model.addAttribute("registerMessage", "Email or phone number taken");
			return "404";
		}

	}

	@PostMapping(value = "/login")
	public String login(String email, String password, HttpSession session, Model model) {
		try {
			User u = userService.login(email, password);
			session.setAttribute("USER", u);
			return "redirect:/sStore/";
		} catch (Exception e) {
			model.addAttribute("loginMessage", "Email or password incorrect");
			return "404";
		}
	}

	@GetMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("USER");
		return "redirect:/sStore/";
	}

	@PostMapping(value = "/add-to-cart")
	@ResponseBody
	public String addToCart(
			ProductItem productItem,
			@RequestParam(name = "userId") Long userId,
			@RequestParam(name = "productId") Long productId) {
		return cartService.addToCart(productItem, userId, productId);
	}

	/* ADMIN */
	@GetMapping(value = "/admin")
	public String adminHome() {
		return "adminDashboard";
	}

	@GetMapping(value = "/admin/store")
	public String adminStore(
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			@RequestParam(name = "filterMode", required = false, defaultValue = "0") Long filterMode,
			Model model) {
		List<Product> products = productService.getListProduct(search, filterMode);
		model.addAttribute("products", products);
		return "adminMyStore";
	}

	@GetMapping(value = "/admin/viewProduct")
	@ResponseBody
	public Product viewProduct(Long id) {
		return productService.getProductById(id);
	}

	@PostMapping(value = "/admin/newProduct")
	public String newProduct(Product product,
			@RequestParam("files") MultipartFile[] files,
			@RequestParam("new_sizes") List<String> sizes,
			@RequestParam("new_colors") List<String> colors,
			Model model) {
		try {
			productService.addProduct(product, files, sizes, colors);
		} catch (IOException e) {
			model.addAttribute("NewProductMessage", e);
			return "404";
		}
		// System.out.println("add Successful");
		return "redirect:../admin/store";
	}

	@PostMapping(value = "/admin/editProduct")
	public String editProduct(Product product,
			@RequestParam("edit_files") MultipartFile[] files,
			@RequestParam("edit_sizes") List<String> sizes,
			@RequestParam("edit_colors") List<String> colors,
			Model model) {
		try {
			productService.editProduct(product, files, sizes, colors);
		} catch (IOException e) {
			model.addAttribute("EditProductMessage", e);
			return "404";
		}
		return "redirect:../admin/store";
	}

	@GetMapping(value = "/admin/delProduct/{pid}")
	public String delProduct(@PathVariable("pid") Long pid) {
		productService.delProduct(pid);
		return "redirect:../../admin/store";
	}
}
