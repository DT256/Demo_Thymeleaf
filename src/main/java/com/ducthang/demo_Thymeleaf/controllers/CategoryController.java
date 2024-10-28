package com.ducthang.demo_Thymeleaf.controllers;


import com.ducthang.demo_Thymeleaf.entity.CategoryEntity;
import com.ducthang.demo_Thymeleaf.services.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;
    @GetMapping
    public String getAll(ModelMap modelMap){
        List<CategoryEntity> list = categoryService.findAll();
        System.out.println(list.size());
        modelMap.addAttribute("list", list);
        return "list";
    }

    @GetMapping("/signup")
    public String signup(CategoryEntity category, ModelMap modelMap){
        modelMap.addAttribute("category", category);
        return "add";
    }

    @PostMapping("/add")
    public String add(@Valid CategoryEntity category, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "add";
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model){
        categoryService.deleteById(id);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model){
        CategoryEntity category = categoryService.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("category", category);
        return "update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid CategoryEntity category, BindingResult result,
                         Model model){
        if (result.hasErrors()){
            return "list";
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<CategoryEntity> list = null;
        if (StringUtils.hasText(name)) {
            list = categoryService.findByNameContaining(name);
        } else {
            list = categoryService.findAll();
        }
        model.addAttribute("list", list);
        return "search";
    }
    @RequestMapping("/searchpaginated")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name, @RequestParam("page") Optional < Integer > page, @RequestParam("size") Optional < Integer > size) {
        int count = (int) categoryService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
        Page< CategoryEntity > resultPage = null;
        if(StringUtils.hasText(name)) {
            resultPage = categoryService.findByNameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
        }
        int totalPages = resultPage.getTotalPages();
        if(totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            if(totalPages > count) {
                if(end == totalPages) start = end - count;
                else if(start == 1) end = start + count;
            }
            List < Integer > pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("categoryPage", resultPage);
        return "searchpaging";
    }
}
