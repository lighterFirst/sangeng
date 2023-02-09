package com.cheng.controller.content;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddArticleDto;
import com.cheng.service.ArticleService;
import com.cheng.vo.ShowArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto addArticleDto){
        return articleService.addArticle(addArticleDto);
    }

    //查询文章列表
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize, String title,String summary){
        return articleService.listArticle(pageNum,pageSize,title,summary);
    }

    /**
     * 回显文章
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult showArticle(@PathVariable("id") Long id){
        return articleService.showArticle(id);
    }

    /**
     * 更新文章
     * @param showArticleVo
     * @return
     */
    @PutMapping()
    public ResponseResult<ShowArticleVo> updateArticle(@RequestBody ShowArticleVo showArticleVo){

        return articleService.updateArticle(showArticleVo);

    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }

}
