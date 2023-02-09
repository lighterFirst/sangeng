package com.cheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddArticleDto;
import com.cheng.domain.entity.Article;
import com.cheng.service.ArticleTagService;
import com.cheng.domain.entity.ArticleTag;
import com.cheng.mapper.ArticleTagMapper;
import com.cheng.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author cheng
 * @since 2022-12-27 14:37:51
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Override
    public List<Long> getTagId(Long id) {


        QueryWrapper queryWrapper = new QueryWrapper();
        //select只是将该列的值设置为null了。
        queryWrapper.select("tag_id").eq("article_id", id);

        List<Long> list = list(queryWrapper);
        System.out.println(list);
        return list;
    }

}