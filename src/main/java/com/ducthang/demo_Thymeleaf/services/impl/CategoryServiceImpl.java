package com.ducthang.demo_Thymeleaf.services.impl;

import com.ducthang.demo_Thymeleaf.entity.CategoryEntity;
import com.ducthang.demo_Thymeleaf.repository.CategoryRepository;
import com.ducthang.demo_Thymeleaf.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl  implements ICategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public <S extends CategoryEntity> S save (S entity){
        if(entity.getCategoryId() == null){
            return categoryRepository.save(entity);
        }
        else{
            Optional<CategoryEntity> opt = categoryRepository.findById(entity.getCategoryId());
            if(opt.isPresent()){
                if(StringUtils.isEmpty(entity.getName())){
                    entity.setName(opt.get().getName());
                }
                else{
                    entity.setName(entity.getName());
                }
            }
            return categoryRepository.save(entity);
        }
    }
    @Override
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<CategoryEntity> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<CategoryEntity> findAll(Sort sort) {
        return categoryRepository.findAll(sort);
    }

    @Override
    public List<CategoryEntity> findAllById(Iterable<Long> ids) {
        return categoryRepository.findAllById(ids);
    }

    @Override
    public Optional<CategoryEntity> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public <S extends CategoryEntity> Optional<S> findOne(Example<S> example) {
        return categoryRepository.findOne(example);
    }

    @Override
    public Page<CategoryEntity> findByNameContaining(String keyword, Pageable page) {
        return categoryRepository.findByNameContaining(keyword, page);
    }

    @Override
    public List<CategoryEntity> findByNameContaining(String keyword) {
        return categoryRepository.findByNameContaining(keyword);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void insert(CategoryEntity entity) {

    }

    @Override
    public void update(CategoryEntity entity) {

    }
}
