package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.logging.log4j.message.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductDTO {
    @NotBlank(message="Title is required")
    @Size(min = 3,max = 200, message = "Title must between 3 and 200 characters")
    private String name;
    @Min(value = 0,message = "Price must >= 0")
    @Max(value = 100000000,message = "Price must <= 100,000,000 vnd")
    private float price;
    private String description;
    private String thumbnail;
    @JsonProperty("category_id")
    private String categoryId;

    private List<MultipartFile> files;

}
