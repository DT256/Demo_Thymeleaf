package com.ducthang.demo_Thymeleaf.services;

import com.ducthang.demo_Thymeleaf.entity.CategoryEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    public <S extends CategoryEntity> S save (S entity);
    public List<CategoryEntity> findAll();
    public Page<CategoryEntity> findAll(Pageable pageable);
    public List<CategoryEntity> findAll(Sort sort);
    public List<CategoryEntity> findAllById(Iterable<Long> ids);
    public Optional<CategoryEntity> findById(Long id);
    public <S extends CategoryEntity> Optional<S> findOne(Example<S> example);
    public Page<CategoryEntity> findByNameContaining(String keyword, Pageable page);
    public List<CategoryEntity> findByNameContaining(String keyword);
    public long count();
    public void deleteById(Long id);
    public void insert(CategoryEntity entity);
    public void update(CategoryEntity entity);
}
