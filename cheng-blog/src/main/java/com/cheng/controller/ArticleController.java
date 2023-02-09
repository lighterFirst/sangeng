package com.cheng.controller;

import com.cheng.domain.ResponseResult;
import com.cheng.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章表查询
 */

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        //1-查询热门文章
       ResponseResult result = articleService.hotArticleList();
       return result;
    }

    /*2-分页查询文章列表*/
    @GetMapping("/articleList")
    //@RequestParam( defaultValue默认值，required：参数可以不传  )
    public ResponseResult queryArticleList(@RequestParam(defaultValue = "1",required = false) Integer pageNum,@RequestParam(defaultValue = "1",required = false) Integer pageSize, Long id){
        return articleService.queryArticleList(pageNum,pageSize,id);
    }

    /**
     * 3-查询文章
     */

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }

}











