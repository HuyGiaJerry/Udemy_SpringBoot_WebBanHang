package com.project.shopapp.models;

import com.project.shopapp.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false,length = 300)
    private String name;

    private Float price;

    @Column(name = "thumbnail",length = 300)
    private String thumbnail;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
