package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.helpers.file.FileHelper;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.ProductService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    @Autowired
    private FileHelper fileHelper;

    private final ProductService productService;

    // Tất cả sản phẩm có phân trang (page, limit)
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @PathParam("page") int page,
            @PathParam("limit") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createAt").descending());
        int totalPage = productService.getAllProducts(pageRequest).getTotalPages();
        List<ProductResponse> products = productService.getAllProducts(pageRequest).getContent();

        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPage)
                .build());
    }

    // Sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable long id) {
        return ResponseEntity.ok("Get Product " + id);
    }


    // Thêm mới 1 sản phẩm
    @PostMapping("")
    public ResponseEntity<?> insertProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok().body(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upLoadProductImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ) {

        try {
            Product existingProduct = productService.getProductById(productId);
            List<ProductImage> productImages = new ArrayList<>();
            // Kiểm tra đầu vào không đc vượt quá 5 images
            if (files.size() > ProductImage.MAX_IMAGES_PER_PRODUCT) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("You can upload up to " + ProductImage.MAX_IMAGES_PER_PRODUCT + " images only !");
            }
            // Kiểm tra nếu không có file nào được upload
            if (files == null || files.isEmpty() || files.stream().allMatch(f -> f.isEmpty() || f.getSize() == 0)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("You must upload at least 1 image!");
            }
            for (MultipartFile file : files) {
                // Kiểm tra kích thước file và định dạng
                if (fileHelper.checkSize(file)) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large ! Max is 10MB");
                }
                if (fileHelper.checkContentType(file)) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be image !");
                }
                // Lưu File và cập nhật thumbnail trong DTO
                String fileName = fileHelper.storeFile(file);
                // Lưu vào đối tượng product trong DB => làm sau
                // Lưu vào bảng Product_Image
                ProductImage productImage = productService.createProductImage(productId,
                        ProductImageDTO
                                .builder()
                                .imageUrl(fileName)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable long id) {
        return ResponseEntity.ok("Update Product " + id);
    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        return ResponseEntity.ok("Delete Product " + id);
    }

}
