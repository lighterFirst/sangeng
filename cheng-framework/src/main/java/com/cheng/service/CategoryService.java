package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.ListCategoryDto;
import com.cheng.domain.entity.Category;
import com.cheng.vo.WriteCategoryVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 分类表(Category)表服务接口
 *
 * @author apple
 * @since 2022-12-05 11:21:15
 */
public interface CategoryService extends IService<Category> {


    ResponseResult<Category> getCategoryList();

    //写文章时，查询所有分类
    ResponseResult<WriteCategoryVo> listAllCategory();

    void getExcel(HttpServletResponse response);

    ResponseResult listCategory(long pageNum, long pageSize, ListCategoryDto listCategoryDto);

    ResponseResult addCategory(Category category);

    ResponseResult showCategory(Long id);

    ResponseResult updateCategory(Category category);

    ResponseResult deleteCategory(Long id);
}

