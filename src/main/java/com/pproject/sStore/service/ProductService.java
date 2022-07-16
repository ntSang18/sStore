package com.pproject.sStore.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pproject.sStore.model.Product;
import com.pproject.sStore.model.ProductColor;
import com.pproject.sStore.model.ProductImage;
import com.pproject.sStore.model.ProductSize;
import com.pproject.sStore.repository.ProductColorRepository;
import com.pproject.sStore.repository.ProductImageRepository;
import com.pproject.sStore.repository.ProductRepository;
import com.pproject.sStore.repository.ProductSizeRepository;

@Service
public class ProductService {

	private static final String LOCATION = "src/main/resources/static/uploads/product_images/";
	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	private final ProductSizeRepository productSizeRepository;
	private final ProductColorRepository productColorRepository;

	@Autowired
	public ProductService(ProductRepository productRepository,
			ProductImageRepository productImageRepository,
			ProductSizeRepository productSizeRepository,
			ProductColorRepository productColorRepository) {
		super();
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
		this.productSizeRepository = productSizeRepository;
		this.productColorRepository = productColorRepository;
	}

	public List<Product> getAllProduct() {
		return productRepository.findAll();
	}

	public List<Product> getListProduct(String search, Long filterMode) {
		List<Product> products = new ArrayList<Product>();
		if (search.trim() == "") {
			products = getAllProduct();
		} else {
			for (Product p : getAllProduct()) {
				if (p.getProductName().toLowerCase().contains(search.toLowerCase()) ||
						p.getBranch().toLowerCase().contains(search.toLowerCase())) {
					products.add(p);
				}
			}
		}
		if (filterMode == 0) {
			products.sort((Product p1, Product p2) -> {
				return p1.getId().compareTo(p2.getId());
			});
		} else if (filterMode == 1) {
			products.sort((Product p1, Product p2) -> {
				return p1.getPrice().compareTo(p2.getPrice());
			});
		} else if (filterMode == 2) {
			products.sort((Product p1, Product p2) -> {
				if (p1.getPrice() < p2.getPrice())
					return 1;
				else
					return -1;
			});
		} else if (filterMode == 3) {
			products.sort((Product p1, Product p2) -> {
				return p1.getCreateAt().compareTo(p2.getCreateAt());
			});
		} else if (filterMode == 4) {
			products.sort((Product p1, Product p2) -> {
				if (p1.getCreateAt().compareTo(p2.getCreateAt()) < 0)
					return 1;
				else
					return -1;
			});
		}
		return products;
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("Product by id " + id + "does not exists"));
	}

	public Product addProduct(Product product, MultipartFile[] files, List<String> sizes, List<String> colors)
			throws IOException {
		product.setCreateAt(LocalDate.now());
		Product saveProduct = productRepository.save(product);
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			ProductImage saveImg = new ProductImage(fileName, saveProduct);
			productImageRepository.save(saveImg);
			String uploadDir = LOCATION + saveProduct.getId();
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			InputStream inputStream = file.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			// System.out.println(filePath.toString());
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}
		for (String size : sizes) {
			ProductSize saveSize = new ProductSize(size, saveProduct);
			productSizeRepository.save(saveSize);
		}
		for (String color : colors) {
			ProductColor saveColor = new ProductColor(color, saveProduct);
			productColorRepository.save(saveColor);
		}
		return saveProduct;
	}

	public Product editProduct(Product product, MultipartFile[] files, List<String> sizes, List<String> colors)
			throws IOException {
		Product editProduct = getProductById(product.getId());
		editProduct.setProductName(product.getProductName());
		editProduct.setPrice(product.getPrice());
		editProduct.setDescription(product.getDescription());
		editProduct.setBranch(product.getBranch());
		editProduct.setAvailable(product.getAvailable());
		editProduct.setGender(product.isGender());
		editProduct.setType(product.getType());

		editProduct.getSizes().clear();
		editProduct.getColors().clear();
		if (!isFilesEmpty(files)) {
			for (ProductImage image : editProduct.getImages()) {
				File delFile = new File(LOCATION + "/" + editProduct.getId() + "/" + image.getImageName());
				delFile.delete();
			}
			editProduct.getImages().clear();

			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename();
				ProductImage editImg = new ProductImage(fileName, editProduct);
				productImageRepository.save(editImg);
				String uploadDir = LOCATION + product.getId();
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				InputStream inputStream = file.getInputStream();
				Path filePath = uploadPath.resolve(fileName);
				// System.out.println(filePath.toString());
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		for (String size : sizes) {
			ProductSize editSize = new ProductSize(size, editProduct);
			productSizeRepository.save(editSize);
		}
		for (String color : colors) {
			ProductColor editColor = new ProductColor(color, editProduct);
			productColorRepository.save(editColor);
		}
		return productRepository.save(editProduct);
	}

	public void delProduct(Long pid) {
		Product delProduct = getProductById(pid);
		for (ProductImage image : delProduct.getImages()) {
			File delFile = new File(LOCATION + "/" + delProduct.getId() + "/" + image.getImageName());
			delFile.delete();
		}
		File delFolder = new File(LOCATION + "/" + delProduct.getId());
		delFolder.delete();
		productRepository.deleteById(pid);
	}

	private boolean isFilesEmpty(MultipartFile[] files) {
		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				return true;
			}
		}
		return false;
	}

}
