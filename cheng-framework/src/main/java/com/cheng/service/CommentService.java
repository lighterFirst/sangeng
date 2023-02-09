package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author cheng
 * @since 2022-12-12 11:44:22
 */
public interface CommentService extends IService<Comment> {

   ResponseResult commonList(Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

    ResponseResult linkCommentsList(Integer pageNum, Integer pageSize);
}

