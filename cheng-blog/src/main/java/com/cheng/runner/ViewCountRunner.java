package com.cheng.runner;

import com.cheng.constants.SystemConstants;
import com.cheng.domain.entity.Article;
import com.cheng.mapper.ArticleMapper;
import com.cheng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //从myslq查询博客信息的浏览数，并保存到redis中
        List<Article> articleList = articleMapper.selectList(null);

        //保存到map中，key为文章id，value为浏览数
        Map<String, Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(new Function<Article, String>() {
                    @Override
                    public String apply(Article article) {
                        return article.getId().toString();
                    }
                }, new Function<Article, Integer>() {
                    @Override
                    public Integer apply(Article article) {
                        return article.getViewCount().intValue();
                    }
                }));
            redisCache.setCacheMap(SystemConstants.VIEW_COUNT,viewCountMap);

    }
}













