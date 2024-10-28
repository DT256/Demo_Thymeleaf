package com.ducthang.demo_Thymeleaf.controllers;


import com.ducthang.demo_Thymeleaf.entity.CategoryEntity;
import com.ducthang.demo_Thymeleaf.models.CategoryModel;
import com.ducthang.demo_Thymeleaf.services.ICategoryService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping("add")
    public String addCategory(Model model) {
        CategoryModel category = new CategoryModel();
        category.setIsEdit(false);
        model.addAttribute("category", category);
        return "admin/categories/addOrEdit";
    }

    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model,
                                     @Valid @ModelAttribute("category") CategoryModel cateModel, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        BeanUtils.copyProperties(cateModel, categoryEntity);
        categoryService.save(categoryEntity);
        String msg = "";
        if(cateModel.getIsEdit() == true) {
            msg = "Category is edit";
        }
        else {
            msg = "Category is save";
        }
        model.addAttribute("msg", msg);
        return new ModelAndView("foward:/admin/categories/searchpage",model);
        //return new ModelAndView("foward:/admin/categories/search", model);// xem laij
    }

    @RequestMapping("")
    public String list(ModelMap model) {
        List<CategoryEntity> list = categoryService.findAll();
        model.addAttribute("categories", list);
        return "list";
    }

    @GetMapping("edit/categoryId")
    public ModelAndView editCategory(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        Optional<CategoryEntity> optionalCategory = categoryService.findById(categoryId);
        CategoryModel categoryModel = new CategoryModel();
        if(optionalCategory.isPresent()) {
            CategoryEntity category = optionalCategory.get();
            BeanUtils.copyProperties(category, categoryModel);
            categoryModel.setIsEdit(true);
            model.addAttribute("category", categoryModel);
            return new ModelAndView("admin/categories/addOrEdit", model);

        }
        model.addAttribute("msg", "Category not found");
        return new ModelAndView("admin/categories/addOrEdit", model);
    }

    @GetMapping("delete/categoryId")
    public ModelAndView delete(ModelMap model,@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        model.addAttribute("msg", "Category deleted successfully");
        return new ModelAndView("foward:/admin/categories/searchpage",model);
    }

    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name",required = false) String name) {
        List<CategoryEntity> list = null;
        if(StringUtils.hasText(name)){
            list = categoryService.findByNameContaining(name);
        }
        else
            list = categoryService.findAll();

        model.addAttribute("categories", list);
        return "admin/categories/search";
    }

    @RequestMapping("searchpage")
    public String search(ModelMap model,
                        @RequestParam(name = "name",required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        int count = (int)categoryService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
        Page<CategoryEntity> respage = null;
        if(StringUtils.hasText(name)){
            respage = categoryService.findByNameContaining(name, pageable);
            model.addAttribute("name", name);
        }
        else {
            respage = categoryService.findAll(pageable);
        }
        int toalPages = respage.getTotalPages();
        if(toalPages > 0){
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(toalPages, currentPage + 2);
            if(toalPages > count){
                if(end == toalPages) start = end - count;
                else if (start == 1) end = start + count;
            }
            List<Integer> pageNum = IntStream.rangeClosed(start,end)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("catePage", respage);
        }
        return "admin/categories/searchpage";
    }

}
