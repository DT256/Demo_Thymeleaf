package com.ducthang.demo_Thymeleaf.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long categoryId;
    private String name;
    private String image;
    private Boolean isEdit = false;

}
