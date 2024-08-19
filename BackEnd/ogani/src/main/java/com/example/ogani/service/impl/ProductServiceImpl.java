package com.example.ogani.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ogani.entity.Category;
import com.example.ogani.entity.Image;
import com.example.ogani.entity.Product;
import com.example.ogani.exception.NotFoundException;
import com.example.ogani.model.request.CreateProductRequest;
import com.example.ogani.repository.CategoryRepository;
import com.example.ogani.repository.ImageRepository;
import com.example.ogani.repository.ProductRepository;
import com.example.ogani.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<Product> getList() {
        return productRepository.findAll(Sort.by("id").descending());
    }

    @Override
    public Product getProduct(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not Found Product With Id: " + id));

        return product;
    }

    @Override
    public Product createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Not Found Category With Id: " + request.getCategoryId()));
        product.setCategory(category);

        Set<Image> images = new HashSet<>();
        for (long imageId : request.getImageIds()) {
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new NotFoundException("Not Found Image With Id: " + imageId));
            images.add(image);
        }
        product.setImages(images);
        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProduct(long id, CreateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not Found Product With Id: " + id));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Not Found Category With Id: " + request.getCategoryId()));
        product.setCategory(category);

        Set<Image> images = new HashSet<>();
        for (long imageId : request.getImageIds()) {
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new NotFoundException("Not Found Image With Id: " + imageId));
            images.add(image);
        }
        product.setImages(images);
        productRepository.save(product);

        return product;
    }

    @Override
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not Found Product With Id: " + id));
        product.getImages().remove(this);
        productRepository.delete(product);
    }

    @Override
    public List<Product> getListNewst(int number) {
        List<Product> list = productRepository.getListNewest(number);
        return list;
    }

    @Override
    public List<Product> getListByPrice() {
        return productRepository.getListByPrice();
    }

    @Override
    public List<Product> findRelatedProduct(long id) {
        List<Product> list = productRepository.findRelatedProduct(id);
        return list;

    }

    @Override
    public List<Product> getListProductByCategory(long id) {
        List<Product> list = productRepository.getListProductByCategory(id);
        System.out.println("sizeHaha." + list.size());
        for (Product p : list) {
            System.out.println(p.getName());
        }

        return list;
    }

    @Override
    public List<Product> getListByPriceRange(long id, int min, int max) {
        List<Product> list = productRepository.getListProductByPriceRange(id, min, max);
        return list;
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        List<Product> list = productRepository.searchProduct(keyword);
        return list;
    }

    @Override
    public void deleteProductImage(long productId, long imageId) {
        // Truy vấn sản phẩm từ cơ sở dữ liệu
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

        // Lấy danh sách ảnh của sản phẩm
        Set<Image> images = product.getImages();

        // Tìm kiếm ảnh cần xóa trong danh sách ảnh của sản phẩm
        Image imageToRemove = images.stream()
                .filter(image -> image.getId() == imageId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Image not found with id: " + imageId));

        // Xóa ảnh khỏi danh sách ảnh của sản phẩm
        images.remove(imageToRemove);

        // Lưu lại sản phẩm được cập nhật vào cơ sở dữ liệu
        productRepository.save(product);
    }

    @Override
    public void updateProductImages(long productId, List<Long> imageIds) {
        // Lấy sản phẩm từ cơ sở dữ liệu
        Product product = getProduct(productId);

        // Cập nhật danh sách hình ảnh cho sản phẩm
        product.setImageIds(imageIds);

        // Lưu sản phẩm đã cập nhật vào cơ sở dữ liệu
        productRepository.save(product);
    }

}
