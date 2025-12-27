package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.helpers.file.FileHelper;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.product.ProductService;
import com.project.shopapp.utils.LocalizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    @Autowired
    private FileHelper fileHelper;

    private final ProductService productService;

    // Message toàn cục
    private final LocalizationUtil localizationUtil;
    // Logger
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    // Tất cả sản phẩm có phân trang (page, limit)
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0",name= "category_id") Long categoryId ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {

        // Tạo pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        logger.info("Get products list : keyword = {}, categoryId = {}, page = {}, limit = {}", keyword, categoryId, page, limit);
        //PageRequest.of(page, limit, Sort.by("createAt").descending());
        int totalPage = productService.getAllProducts(keyword,categoryId,pageRequest).getTotalPages();
        List<ProductResponse> products = productService.getAllProducts(keyword,categoryId,pageRequest).getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPage)
                .build());
    }

    // Sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable long id) {
        try {

            return ResponseEntity.ok(productService.getProductById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
            Product existingProduct = productService.getProductEntityById(productId);
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

    @GetMapping("/images/{imgName}")
    public ResponseEntity<?> viewImages(
            @PathVariable String imgName
    ) {
        try {
            java.nio.file.Path imgPath = Paths.get("uploads/" + imgName);
            UrlResource resource = new UrlResource(imgPath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/not_found_img.jpg").toUri()));
                // return ResponseEntity.notFound().build();
            }


        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable long id,
            @RequestBody ProductDTO productDTO
    ) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Delete Product " + id + " successfully!");
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        try {
            // Tách chuỗi ids thành danh sách mảng số nguyên
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    // @PostMapping("/generateFakeDataProducts")
    private ResponseEntity<String> generateFakeDataProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(1000, 100000000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(2, 6))
                    .build();
            productService.createProduct(productDTO);
        }


        return ResponseEntity.ok("Generate fake data products successfully!");
    }


}
