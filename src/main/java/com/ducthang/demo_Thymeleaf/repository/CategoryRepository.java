package com.ducthang.demo_Thymeleaf.repository;

import com.ducthang.demo_Thymeleaf.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    // tim kiem noi dung theo ten
    List<CategoryEntity> findByNameContaining(String name);
    // tim kim va phan trang
    Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);

}
