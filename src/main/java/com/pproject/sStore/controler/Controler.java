package com.pproject.sStore.controler;

import com.pproject.sStore.model.*;
import com.pproject.sStore.service.CartService;
import com.pproject.sStore.service.OrderService;
import com.pproject.sStore.service.ProductService;
import com.pproject.sStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/sStore")
public class Controler {

	private final UserService userService;
	private final ProductService productService;
	private final CartService cartService;
	private final OrderService orderService;

	@Autowired
	public Controler(UserService userService, ProductService productService, CartService cartService,
			OrderService orderService) {
		this.userService = userService;
		this.productService = productService;
		this.cartService = cartService;
		this.orderService = orderService;
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
	public String shop(
			@RequestParam(name = "pageNum") Optional<Integer> pageNum,
			@RequestParam(name = "search") Optional<String> search,
			@RequestParam(name = "gender") Optional<Integer> gender,
			@RequestParam(name = "type") Optional<String> type,
			Model model) {
		Page<Product> page = productService.getPageProduct(
				pageNum.orElse(1),
				search.orElse(""),
				gender.orElse(-1),
				type.orElse(""));
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum.orElse(1));
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("products", page.getContent());
		model.addAttribute("search", search.orElse(""));
		model.addAttribute("gender", gender.orElse(-1));
		model.addAttribute("type", type.orElse(""));
		return "shop";
	}

	@GetMapping(value = "/sproduct")
	public String sproduct(
			Model model,
			@RequestParam(name = "id") Long id,
			@RequestParam(name = "pageNum") Optional<Integer> pageNum) {
		Product product = productService.getProductById(id);
		double rating = productService.getProductRating(id);
		Page<ProductReview> page = productService.getPageProductReview(id, pageNum.orElse(1));
		model.addAttribute("product", product);
		model.addAttribute("rating", rating);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum.orElse(1));
		model.addAttribute("reviews", page.getContent());
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

	@GetMapping(value = "/account")
	public String account(Model model, HttpSession session) {
		try {
			User user = userService.getUserById(((User) session.getAttribute("USER")).getId());
			List<Order> orders = orderService.getUserOrders(user.getId());
			model.addAttribute("user", user);
			model.addAttribute("orders", orders);
			return "account";
		} catch (Exception e) {
			model.addAttribute("message", "You are not login in");
			return "404";
		}
	}

	@PostMapping(value = "/review-product")
	@ResponseBody
	public String reviewProduct(
			ProductReview review,
			@RequestParam(name = "piid") Long piid,
			HttpSession session) {
		try {
			User user = (User) session.getAttribute("USER");
			productService.reviewProduct(review, piid, user);
			return "Thanks for rating us!";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@GetMapping(value = "/cart")
	public String cart(Model model, HttpSession session) {
		try {
			User user = (User) session.getAttribute("USER");
			List<ProductItem> cartItems = cartService.getCartItems(user.getId());
			Long subTotal = cartService.getSubTotal(user.getId());
			model.addAttribute("cartItems", cartItems);
			model.addAttribute("subTotal", subTotal);
			return "cart";
		} catch (Exception e) {
			model.addAttribute("message", "You are not login in");
			return "404";
		}
	}

	@PostMapping("/register")
	public String register(User user, Address address, HttpSession session, Model model) {
		try {
			User u = userService.register(user, address);
			session.setAttribute("USER", u);
			return "redirect:/sStore/";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Email or phone number taken");
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
			model.addAttribute("message", "Email or password incorrect");
			return "404";
		}
	}

	@PostMapping(value = "/edit-user")
	@ResponseBody
	public String editUser(User user, Address address, HttpSession session) {
		try {
			User u = userService.editUser(user, address, ((User) session.getAttribute("USER")).getId());
			session.setAttribute("USER", u);
			return "Edit user successfully!";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
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
			@RequestParam(name = "productId") Long productId,
			HttpSession session,
			Model model) {
		try {
			User user = (User) session.getAttribute("USER");
			return cartService.addToCart(productItem, user.getId(), productId);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@GetMapping(value = "/del-cart-item")
	public String delCartItem(
			@RequestParam(name = "itemId") Long itemId,
			HttpSession session,
			Model model) {
		try {
			cartService.delCartItem(itemId);
			return "redirect:cart";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}
	}

	@GetMapping(value = "/newOrder")
	@ResponseBody
	public String newOrder(HttpSession session) {
		try {
			User user = (User) session.getAttribute("USER");
			orderService.newOrder(user.getId());
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "failed";
	}

	/* ADMIN */
	@GetMapping(value = "/admin-dashboard")
	public String adminHome() {
		return "adminDashboard";
	}

	@GetMapping(value = "/admin-store")
	public String adminStore(
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			@RequestParam(name = "filterMode", required = false, defaultValue = "0") Long filterMode,
			Model model) {
		List<Product> products = productService.getListProduct(search, filterMode);
		model.addAttribute("products", products);
		return "adminMyStore";
	}

	@GetMapping(value = "/admin-orders")
	public String adminOrders(
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			@RequestParam(name = "filterMode", required = false, defaultValue = "0") Long filterMode,
			Model model) {
		List<Order> orders = orderService.getListAdminOrder(search, filterMode);
		model.addAttribute("orders", orders);
		return "adminOrders";
	}

	@GetMapping(value = "/admin-customers")
	public String adminCustomers(
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			Model model) {
		List<User> users = userService.getListUser(search);
		model.addAttribute("users", users);
		return "adminCustomers";
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
		return "redirect:../admin-store";
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
		return "redirect:../admin-store";
	}

	@GetMapping(value = "/admin/delProduct/{pid}")
	public String delProduct(@PathVariable("pid") Long pid) {
		productService.delProduct(pid);
		return "redirect:../../admin-store";
	}

	@GetMapping(value = "/view-order")
	@ResponseBody
	public Order viewOrder(Long id) {
		return orderService.getOrderById(id);
	}

	@GetMapping(value = "/admin/confirmOrder")
	public String confirmOrder(Long id) {
		orderService.confirmOrder(id);
		return "redirect:../admin-orders";
	}

	@GetMapping(value = "/admin/delOrder/{oid}")
	public String adminDelOrder(@PathVariable("oid") Long oid) {
		orderService.adminDelOrder(oid);
		return "redirect:../../admin-orders";
	}

	@GetMapping(value = "/view-user")
	@ResponseBody
	public User viewUser(Long id) {
		return userService.getUserById(id);
	}

	@PostMapping("/admin-new-account")
	public String newAccount(User user, Address address, Model model) {
		try {
			User u = userService.register(user, address);
			return "redirect:/sStore/admin-customers";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			return "404";
		}
	}

}
