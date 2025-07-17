package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.helpers.services.BaseServiceImpl;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;


    @Override
    public Product createProduct(ProductDTO productDTO) {
        try {
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Category not found with id: " + productDTO.getCategoryId()));

            Product newProduct = Product.builder()
                    .name(productDTO.getName())
                    .description(productDTO.getDescription())
                    .thumpnail(productDTO.getThumbnail())
                    .price(productDTO.getPrice())
                    .category(existingCategory)
                    .build();

            return productRepository.save(newProduct);

        } catch (IllegalArgumentException | DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProductById(Long id) {
        try {
            return productRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // Lấy ra danh sách sản phẩm với phân trang(page , limit)

        return productRepository
                .findAll(pageRequest)
                .map(ProductResponse::convertProductToResponse);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) {
        try {
            Product existingProduct = getProductById(id);
            if (existingProduct != null) {
                // Copy các trường từ productDTO vào existingProduct

                Category existingCategory = categoryRepository
                        .findById(productDTO.getCategoryId())
                        .orElseThrow(() -> new DataNotFoundException("Category not found with id: " + productDTO.getCategoryId()));

                existingProduct.setName(productDTO.getName());
                existingProduct.setDescription(productDTO.getDescription());
                existingProduct.setPrice(productDTO.getPrice());
                existingProduct.setCategory(existingCategory);
                existingProduct.setThumpnail(productDTO.getThumbnail());
                return productRepository.save(existingProduct);
            } else {
                throw new DataNotFoundException("Product not found with id: " + id);
            }
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId,
                                           ProductImageDTO productImageDTO) {

        try {
            Product existingProduct = productRepository
                    .findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));
            ProductImage newProductImage = ProductImage.builder()
                    .product(existingProduct)
                    .imageUrl(productImageDTO.getImageUrl())
                    .build();
            // Kiểm tra số lượng hình ảnh mà sản phẩm đã có
            int size = productImageRepository.findByProductId(productId).size();
            if (size >= ProductImage.MAX_IMAGES_PER_PRODUCT) {
                throw new InvalidParamException("Cannot add more than " + ProductImage.MAX_IMAGES_PER_PRODUCT + " images for a product");
            }
            return productImageRepository.save(newProductImage);

        } catch (DataNotFoundException | InvalidParamException e) {
            throw new RuntimeException(e);
        }
    }

}
