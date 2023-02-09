package com.cheng.controller;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddCommentDto;
import com.cheng.domain.entity.Comment;
import com.cheng.service.CommentService;
import com.cheng.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;


    /**
     * 查询评论以及子评论
     * @param articleId  文章id
     * @param pageNum  分页大小
     * @param pageSize  分页页码
     * @return
     */
    @GetMapping("/commentList")
    public ResponseResult commonList(Long articleId,Integer pageNum,Integer pageSize){

        return commentService.commonList(articleId,pageNum,pageSize);

    }

    /**
     * 发表文章评论和//发表友链评论是统一接口
     * @param addCommentDto
     * @return
     */
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);

    }

    //查询友链评论
    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表",notes = "获取一页友链品论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "页数")
    }
    )
    public ResponseResult linkCommentsList(Integer pageNum,Integer pageSize){

        return commentService.linkCommentsList(pageNum,pageSize);

    }






}
