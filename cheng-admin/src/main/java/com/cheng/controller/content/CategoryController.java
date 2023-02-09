package com.cheng.controller.content;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.ListCategoryDto;
import com.cheng.domain.entity.Category;
import com.cheng.enums.AppHttpCodeEnum;
import com.cheng.service.CategoryService;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.utils.WebUtils;
import com.cheng.vo.ExcelCategoryVo;
import com.cheng.vo.WriteCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/content/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 写博文展现分类的接口
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult<WriteCategoryVo> listAllCategory(){

        return categoryService.listAllCategory();

    }

    /**
     * 导出excel
     * @return
     */
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void getExcel(HttpServletResponse response){


       try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }


    }

    /**
     * 分页展示分类
     * @param pageNum
     * @param pageSize
     * @param listCategoryDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listCategory(long pageNum, long pageSize,ListCategoryDto listCategoryDto){
        return categoryService.listCategory(pageNum,pageSize,listCategoryDto);

    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @GetMapping("/{id}")
    public ResponseResult showCategory(@PathVariable Long id){
        return categoryService.showCategory(id);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

}
