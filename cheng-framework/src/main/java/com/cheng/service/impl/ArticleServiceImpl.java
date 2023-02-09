package com.cheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.constants.SystemConstants;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddArticleDto;
import com.cheng.domain.entity.Article;
import com.cheng.domain.entity.ArticleTag;
import com.cheng.domain.entity.Category;
import com.cheng.mapper.ArticleMapper;
import com.cheng.service.ArticleService;
import com.cheng.service.ArticleTagService;
import com.cheng.service.CategoryService;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.utils.RedisCache;
import com.cheng.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    //1：测试用的
    @Override
    public Article selectArticle(Long id) {
        return articleMapper.selectById(id);
    }

    //2：查询java，php等分类信息
    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        //必须是正式文章，state为1表示正式文章，0表示草稿
        wrapper.eq(Article::getStatus,0);
        //按照浏览量排序
        wrapper.orderByDesc(Article::getViewCount);
        //最多查询十条
        Page<Article> page = new Page<>(1,10);
        page(page,wrapper);
        List<Article> articles = page.getRecords();

        List<HotArticleVo> list = new ArrayList<>();

        //Bean拷贝
       /* for(Article article : articles){
            HotArticleVo vo =new HotArticleVo();
            BeanUtils.copyProperties(article,vo);
            list.add(vo);
        }*/

        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        log.info(list.toString());
        return ResponseResult.okResult(hotArticleVos);
    }

    //3:查询首页分页列表，置顶的文章要实现在最前面，只能查询正式分布的文章,id为分类id
    @Override
    public ResponseResult queryArticleList(Integer pageNum, Integer pageSize, Long id) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        //如果有分类id,动态sql，如果传入了id（分类参数）则执行
        queryWrapper.eq(Objects.nonNull(id)&&id>0,Article::getCategoryId,id);
        //正式分布的文章
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);

        page(page,queryWrapper);

        /*page.getRecords()将page<Article>转为List<Article>.*/
        List<Article> records = page.getRecords();
        //因为前端要求的分类id是categoryName，后端数据库提供的是categoryId，所以给端要求的分类idcategoryName，set值
        List<Article> articleList  = records.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //从分类表查出


        //bean拷贝到前端要求的对象articleListVos
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, ArticleListVo.class);

        //将封装好的articleListVos传给分页对象（前端要求的）PageVo
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    //4:查询文章，在首页，当你点击阅读全文时：可以跳转到该文章。
    @Override
    public ResponseResult getArticleDetail(long id) {
        //根据id查询文章：
        Article article = getById(id);
        //这里的浏览量不从数据库中获取，从redis中获取
        String k = String.valueOf(id);
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.VIEW_COUNT,k);
        article.setViewCount((long)(viewCount));
        //转化为vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名称(article表格没有该属性，分类表有，结合两张表查出该属性)
        Category category = categoryService.getById(article.getCategoryId());
        if(category.getName() != null){
            articleDetailVo.setCategoryName(category.getName());
        }

        return ResponseResult.okResult(articleDetailVo);
    }

    //5:更新redis的文章浏览量
    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(SystemConstants.VIEW_COUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    //6:后台管理系统，添加博客

    @Override
    @Transactional
    public ResponseResult addArticle(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto,Article.class);
        save(article);
        List<ArticleTag> articleTags = addArticleDto.getTags().stream()
                .map(new Function<Long,ArticleTag>() {
                    @Override
                    public ArticleTag apply(Long tagId) {
                        return new ArticleTag(article.getId(),tagId);
                    }
                })
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    //7: 后台管理系统，模糊查询博客
    @Override
    public ResponseResult listArticle(Long pageNum, Long pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        lambdaQueryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        Page<Article> page = new Page<>(pageNum,pageSize);
        Page<Article> articlePage = page(page, lambdaQueryWrapper);
        return ResponseResult.okResult(new PageVo(articlePage.getRecords(),page.getTotal()));
    }

    /**
     * 回显文章
     * @param id
     * @return
     */
    @Override
    public ResponseResult showArticle(Long id) {
        Article article = getById(id);
        ShowArticleVo showArticleVo = BeanCopyUtils.copyBean(article, ShowArticleVo.class);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, showArticleVo.getId());
        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        List<Long> tags = articleTags.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
        showArticleVo.setTags(tags);
        return ResponseResult.okResult(showArticleVo);
    }

    @Override
    @Transactional
    public ResponseResult<ShowArticleVo> updateArticle(ShowArticleVo showArticleVo) {
        Article article = BeanCopyUtils.copyBean(showArticleVo, Article.class);
        updateById(article);
        List<Long> tags = showArticleVo.getTags();
        List<ArticleTag> articleTagList = tags.stream()
                .map(tag -> {
                    ArticleTag articleTag = new ArticleTag();
                    articleTag.setArticleId(article.getId());
                    articleTag.setTagId(tag);
                    return articleTag;
                })
                .collect(Collectors.toList());
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(lambdaQueryWrapper);
        //因为该表没有设置主键，不能使用  updateBatchById 批量更新方法
        articleTagService.saveBatch(articleTagList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        removeById(id);
        return ResponseResult.okResult(id);
    }
}









