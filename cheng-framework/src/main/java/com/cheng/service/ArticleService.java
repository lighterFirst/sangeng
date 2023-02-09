package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddArticleDto;
import com.cheng.domain.entity.Article;
import com.cheng.vo.ShowArticleVo;


public interface ArticleService extends IService<Article> {

    Article selectArticle(Long id);

    ResponseResult hotArticleList();

    ResponseResult queryArticleList(Integer pageNum, Integer pageSize,Long id);

    ResponseResult getArticleDetail(long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto addArticleDto);


    ResponseResult listArticle(Long pageNum, Long pageSize, String title, String summary);

    ResponseResult showArticle(Long id);

    ResponseResult<ShowArticleVo> updateArticle(ShowArticleVo showArticleVo);

    ResponseResult deleteArticle(Long id);
}
