package com.pproject.sStore.controler;

import com.pproject.sStore.model.*;
import com.pproject.sStore.service.CartService;
import com.pproject.sStore.service.OrderService;
import com.pproject.sStore.service.ProductService;
import com.pproject.sStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
		List<Product> featuredProducts = productService.getFeaturedProducts(8);
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
		List<Product> products = productService.getFeaturedProducts(4);
		model.addAttribute("product", product);
		model.addAttribute("rating", rating);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum.orElse(1));
		model.addAttribute("reviews", page.getContent());
		model.addAttribute("products", products);
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
			User user = (User) session.getAttribute("USER");
			if (user == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				model.addAttribute("user", user);
				return "account";
			}
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}
	}

	@GetMapping(value = "/orders")
	public String orders(Model model, HttpSession session) {
		try {
			User user = (User) session.getAttribute("USER");
			if (user == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				List<Order> orders = orderService.getUserOrders(user.getId());
				model.addAttribute("orders", orders);
				return "orders";
			}
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
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
			if (u.getType() == 1) {
				session.setAttribute("USER", u);
				return "redirect:/sStore/";
			} else if (u.getType() == 2) {
				session.setAttribute("ADMIN", u);
				return "redirect:/sStore/admin-dashboard";
			} else {
				session.setAttribute("SHIPPER", u);
				return "redirect:/sStore/shipper-available-orders";
			}
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

	@PostMapping(value = "/change-email")
	@ResponseBody
	public ResponseEntity<Object> changeEmail(int type, String email, HttpSession session) {
		try {
			User user = new User();
			User newUser = new User();
			if (type == 1) {
				user = (User) session.getAttribute("USER");
				newUser = userService.changeEmail(user, email);
				session.setAttribute("USER", newUser);
			} else if (type == 2) {
				user = (User) session.getAttribute("ADMIN");
				newUser = userService.changeEmail(user, email);
				session.setAttribute("ADMIN", newUser);
			} else {
				user = (User) session.getAttribute("SHIPPER");
				newUser = userService.changeEmail(user, email);
				session.setAttribute("SHIPPER", newUser);
			}
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
					"email", newUser.getEmail(),
					"message", "Your changes have been saved"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping(value = "/change-phone")
	@ResponseBody
	public ResponseEntity<Object> changePhone(int type, String phone, HttpSession session) {
		try {
			User user = new User();
			User newUser = new User();
			if (type == 1) {
				user = (User) session.getAttribute("USER");
				newUser = userService.changePhone(user, phone);
				session.setAttribute("USER", newUser);
			} else if (type == 2) {
				user = (User) session.getAttribute("ADMIN");
				newUser = userService.changePhone(user, phone);
				session.setAttribute("ADMIN", newUser);
			} else {
				user = (User) session.getAttribute("SHIPPER");
				newUser = userService.changePhone(user, phone);
				session.setAttribute("SHIPPER", newUser);
			}
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
					"phone", newUser.getPhoneNumber(),
					"message", "Your changes have been saved"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping(value = "/change-address")
	@ResponseBody
	public ResponseEntity<Object> changeAddress(int type, Address address, HttpSession session) {
		try {
			User user = new User();
			User newUser = new User();
			if (type == 1) {
				user = (User) session.getAttribute("USER");
				newUser = userService.changeAddress(user, address);
				session.setAttribute("USER", newUser);
			} else if (type == 2) {
				user = (User) session.getAttribute("ADMIN");
				newUser = userService.changeAddress(user, address);
				session.setAttribute("ADMIN", newUser);
			} else {
				user = (User) session.getAttribute("SHIPPER");
				newUser = userService.changeAddress(user, address);
				session.setAttribute("SHIPPER", newUser);
			}
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
					"address", newUser.getAddress(),
					"message", "Your Changes have been saved"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping(value = "/change-password")
	@ResponseBody
	public ResponseEntity<Object> changePassword(
			int type,
			String currentPass,
			String newPass,
			HttpSession session) {
		try {
			User user = new User();
			User newUser = new User();
			if (type == 1) {
				user = (User) session.getAttribute("USER");
				newUser = userService.changePassword(user, currentPass, newPass);
				session.setAttribute("USER", newUser);
			} else if (type == 2) {
				user = (User) session.getAttribute("ADMIN");
				newUser = userService.changePassword(user, currentPass, newPass);
				session.setAttribute("ADMIN", newUser);
			} else {
				user = (User) session.getAttribute("SHIPPER");
				newUser = userService.changePassword(user, currentPass, newPass);
				session.setAttribute("SHIPPER", newUser);
			}
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
					"message", "Your changes have been saved"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping(value = "/logout")
	public String logout(int type, HttpSession session) {
		if (type == 1) {
			session.removeAttribute("USER");
		} else if (type == 2) {
			session.removeAttribute("ADMIN");
		} else {
			session.removeAttribute("SHIPPER");
		}
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
	public String adminHome(Model model, HttpSession session) {
		try {
			User u = (User) session.getAttribute("ADMIN");
			if (u == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				List<Order> orders = orderService.getRecentOrders();
				int countOrders = orderService.countNewOrder();
				int countCustomers = userService.countCustomer();
				double totalSales = orderService.totalSalesByMonth();
				model.addAttribute("orders", orders);
				model.addAttribute("countOrders", countOrders);
				model.addAttribute("countCustomers", countCustomers);
				model.addAttribute("totalSales", totalSales);
				return "admin_dashboard";
			}
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}
	}

	@GetMapping(value = "/admin-store")
	public String adminStore(
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			@RequestParam(name = "filterMode", required = false, defaultValue = "0") Long filterMode,
			Model model,
			HttpSession session) {
		try {
			User user = (User) session.getAttribute("ADMIN");
			if (user == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				List<Product> products = productService.getListProduct(search, filterMode);
				model.addAttribute("products", products);
				return "admin_store";
			}
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}

	}

	@GetMapping(value = "/admin-orders")
	public String adminOrders(
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			@RequestParam(name = "filterMode", required = false, defaultValue = "0") Long filterMode,
			Model model,
			HttpSession session) {
		try {
			User user = (User) session.getAttribute("ADMIN");
			if (user == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				List<Order> orders = orderService.getListAdminOrder(search, filterMode);
				model.addAttribute("orders", orders);
				return "admin_orders";
			}
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}

	}

	@GetMapping(value = "/admin-customers")
	public String adminCustomers(
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			Model model,
			HttpSession session) {
		try {
			User user = (User) session.getAttribute("ADMIN");
			if (user == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				List<User> users = userService.getListUser(search);
				model.addAttribute("users", users);
				return "admin_customers";
			}
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}

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

	@PostMapping(value = "/admin-new-account")
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

	@GetMapping(value = "/shipper-available-orders")
	public String shipperOrders1(
			@RequestParam(name = "pageNum") Optional<Integer> pageNum,
			@RequestParam(name = "search") Optional<String> search,
			@RequestParam(name = "sort") Optional<Integer> sort,
			Model model,
			HttpSession session) {
		try {
			User shipper = (User) session.getAttribute("SHIPPER");
			if (shipper == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				Page<Order> page = orderService.getShipOrders1(
						pageNum.orElse(1),
						search.orElse(""),
						sort.orElse(0));
				model.addAttribute("totalPages", page.getTotalPages());
				model.addAttribute("currentPage", pageNum.orElse(1));
				model.addAttribute("search", search.orElse(""));
				model.addAttribute("sort", sort.orElse(0));
				model.addAttribute("orders", page.getContent());
				return "shipper_orders1";
			}
		} catch (Exception e) {
			model.addAttribute("EditProductMessage", e.getMessage());
			return "404";
		}
	}

	@GetMapping(value = "/shipper-my-orders")
	public String shipperOrders2(
			@RequestParam(name = "pageNum") Optional<Integer> pageNum,
			@RequestParam(name = "search") Optional<String> search,
			@RequestParam(name = "sort") Optional<Integer> sort,
			Model model,
			HttpSession session) {
		try {
			User shipper = (User) session.getAttribute("SHIPPER");
			if (shipper == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				Page<Order> page = orderService.getShipOrders2(
						shipper.getId(),
						pageNum.orElse(1),
						search.orElse(""),
						sort.orElse(0));
				model.addAttribute("totalPages", page.getTotalPages());
				model.addAttribute("currentPage", pageNum.orElse(1));
				model.addAttribute("search", search.orElse(""));
				model.addAttribute("sort", sort.orElse(0));
				model.addAttribute("orders", page.getContent());
				return "shipper_orders2";
			}
		} catch (Exception e) {
			model.addAttribute("EditProductMessage", e.getMessage());
			return "404";
		}
	}

	@GetMapping(value = "/shipper-account")
	public String shipperAccount(Model model, HttpSession session) {
		try {
			User shipper = (User) session.getAttribute("SHIPPER");
			if (shipper == null) {
				model.addAttribute("message", "You are not logged in yet!");
				return "404";
			} else {
				model.addAttribute("shipper", shipper);
				return "shipper_account";
			}
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}
	}

	@GetMapping(value = "/shipper-take-order")
	public String shipperTakeOrder(Long id, HttpSession session, Model model) {
		try {
			User shipper = (User) session.getAttribute("SHIPPER");
			orderService.shipperTakeOrder(shipper, id);
			return "redirect:/sStore/shipper-available-orders";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}
	}

	@GetMapping(value = "/shipper-delivered-order")
	public String shipperDeliveredOrder(Long id, HttpSession session, Model model) {
		try {
			User shipper = (User) session.getAttribute("SHIPPER");
			orderService.shipperDeliveredOrder(shipper, id);
			return "redirect:/sStore/shipper-my-orders";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "404";
		}
	}

}
