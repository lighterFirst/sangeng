package com.cheng.controller;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.Category;
import com.cheng.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类表查询
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult<Category> test(){
        return categoryService.getCategoryList();
    }

}
