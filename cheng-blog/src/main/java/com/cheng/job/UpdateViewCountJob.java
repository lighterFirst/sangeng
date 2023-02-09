package com.cheng.job;

import com.cheng.constants.SystemConstants;
import com.cheng.domain.entity.Article;
import com.cheng.service.ArticleService;
import com.cheng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;


    //每十分钟执行一次

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void test(){

        //读取redis的viewcount，并将值保存到redis中。
        Map<String, Integer> cacheMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT);
        //读取map的key（文章id）和value（浏览量）
        List<Article> artices = cacheMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库
        articleService.updateBatchById(artices);

    }


}
