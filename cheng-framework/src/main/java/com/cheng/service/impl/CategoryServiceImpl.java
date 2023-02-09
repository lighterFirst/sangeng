package com.cheng.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.constants.SystemConstants;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.ListCategoryDto;
import com.cheng.domain.entity.Article;
import com.cheng.mapper.CategoryMapper;
import com.cheng.service.ArticleService;
import com.cheng.service.CategoryService;
import com.cheng.domain.entity.Category;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.vo.CategoryVo;
import com.cheng.vo.ExcelCategoryVo;
import com.cheng.vo.PageVo;
import com.cheng.vo.WriteCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author apple
 * @since 2022-12-05 11:24:53
 */
//该类起别名
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;


    /*要求只展示有发布正式文章的分类 必须是正常状态的分类*/

    @Override
    public ResponseResult<Category> getCategoryList() {

        //查询文章表，状态为已发布的文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);

        List<Article> articleList = articleService.list(articleLambdaQueryWrapper);

        //获取文章的分类id，去重(使用stream流去重)
        Set<Long> set = articleList.stream()
                .map((article -> article.getCategoryId()))
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categoriesList = listByIds(set);

        //过滤掉不正常的
       categoriesList = categoriesList.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());


        //封装vo

        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoriesList, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult<WriteCategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Category::getStatus,SystemConstants.STATUS_NORMAL);
        List<Category> list = list(lambdaQueryWrapper);
        List<WriteCategoryVo> writeCategoryVos = BeanCopyUtils.copyBeanList(list, WriteCategoryVo.class);
        return ResponseResult.okResult(writeCategoryVos);
    }

    @Override
    public void getExcel(HttpServletResponse response) {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类表","UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition","attachment; filename="+fileName+".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(data());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

    private List<ExcelCategoryVo> data() {
        List<Category> list = list();
        return BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
    }

    /**
     * 分页展示分类
     * @param pageNum
     * @param pageSize
     * @param listCategoryDto
     * @return
     */
    @Override
    public ResponseResult listCategory(long pageNum, long pageSize, ListCategoryDto listCategoryDto) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String name = listCategoryDto.getName();
       lambdaQueryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        String status = listCategoryDto.getStatus();
        lambdaQueryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        Page<Category> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(),page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(Category category) {
        save(category);
        return ResponseResult.okResult(category);
    }

    @Override
    public ResponseResult showCategory(Long id) {
        Category category = getById(id);
        return ResponseResult.okResult(category);
    }

    @Override
    public ResponseResult updateCategory(Category category) {
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}

