package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDTO {
    @NotEmpty(message = "CategoryName cannot be empty")
    private String name;
}
