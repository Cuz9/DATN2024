package com.example.ogani.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;


    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    private long price;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "product_image",joinColumns = @JoinColumn(name="product_id"),inverseJoinColumns = @JoinColumn(name="image_id"))
    private Set<Image> images = new HashSet<>();

    public void setImageIds(List<Long> imageIds) {
        // Xóa tất cả các hình ảnh hiện có để cập nhật danh sách mới
        this.images.clear();
        
        // Duyệt qua danh sách các id mới và thêm hình ảnh tương ứng vào tập hợp images
        for (Long imageId : imageIds) {
            Image image = new Image();
            image.setId(imageId);
            this.images.add(image);
        }
    }
}
