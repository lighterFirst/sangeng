package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddArticleDto;
import com.cheng.domain.entity.ArticleTag;

import java.util.List;

/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author cheng
 * @since 2022-12-27 14:37:51
 */
public interface ArticleTagService extends IService<ArticleTag> {

    List<Long> getTagId(Long id);

}

