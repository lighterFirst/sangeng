package com.cheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.constants.SystemConstants;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.User;
import com.cheng.service.CommentService;
import com.cheng.domain.entity.Comment;
import com.cheng.mapper.CommentMapper;
import com.cheng.service.UserService;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.vo.CommentVo;
import com.cheng.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author cheng
 * @since 2022-12-12 11:44:22
 */
@Service("commentService")
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    /**
     * 查询子评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult commonList(Long articleId, Integer pageNum, Integer pageSize) {

        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper();
        //从数据库查找对应的articleId（文章评论）
        lambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        //RootIdd进行判断，为-1的是根评论
        lambdaQueryWrapper.eq(Comment::getRootId,-1);
        lambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        //分页查询
        Page<Comment> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        List<CommentVo> commentVos = getCommentVo(page.getRecords());

        //查询所有根评论的子评论集合，并且赋值给对应的属性。
        commentVos = commentVos.stream()
                .map(new Function<CommentVo, CommentVo>() {
                    @Override
                    public CommentVo apply(CommentVo commentVo) {
                        //查询子评论
                        List<CommentVo> childrenList = getChildren(commentVo.getId());
                        commentVo.setChildren(childrenList);
                        return commentVo;
                    }
                }).collect(Collectors.toList());


        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));
    }

    //将昵称赋值给CommentVo的一级评论用户和更高级别品论用户
    private List<CommentVo> getCommentVo(List<Comment> commentVoList){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(commentVoList,CommentVo.class);
        //遍历vo集合
        commentVos =  commentVos.stream()

                .map(new Function<CommentVo,CommentVo >() {
                    @Override
                    public CommentVo apply(CommentVo commentVo) {
                        //通过createBy查询用户昵称并赋值
                        User user = userService.getById(commentVo.getCreateBy());
                        commentVo.setUsername(user.getNickName());
                        //通过To_Comment_UserId(二级评论的userid)查询用户id并赋值,如果该评论不是根评论，才进行查询，将二级评论的昵称（NickName）赋值给 （ToCommentUserName）
                        if (commentVo.getToCommentUserId() != -1) {
                            String nickName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                            commentVo.setToCommentUserName(nickName);
                        }
                        return commentVo;
                    }
                }).collect(Collectors.toList());

        return commentVos;
    }

    /**
     *
     * @param id  主评论的id
     * @return   父评论下的子评论
     */

    //查询父品论下的子评论(CommentVo的全部属性)
    private List<CommentVo> getChildren(long id){
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getRootId,id);
        lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> commentList = list(lambdaQueryWrapper);

        List<CommentVo> commentVo = getCommentVo(commentList);
        return commentVo;

    }

    //保存文章评论
    @Override
    public ResponseResult addComment(Comment comment) {

        save(comment);
        //查询用户id

        return ResponseResult.okResult();
    }

    //查询友链评论
    @Override
    public ResponseResult linkCommentsList(Integer pageNum, Integer pageSize){
        Page<Comment> page =new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查找根评论 rootid = -1
        lambdaQueryWrapper.eq(Comment::getRootId,SystemConstants.ROOT_COMMENT);
        //查找根评论中的友链评论（type =1）
        lambdaQueryWrapper.eq(Comment::getType, SystemConstants.FRIEND_LIST_COMMENT);
        lambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        page(page, lambdaQueryWrapper);
        List<CommentVo> commentVos = getCommentVo(page.getRecords());
        log.info("野马是"+page.getTotal()+"");
        commentVos = commentVos.stream()
                .map(new Function<CommentVo, CommentVo>() {
                    @Override
                    public CommentVo apply(CommentVo commentVo) {
                        //查询子评论
                        List<CommentVo> childrenList = getChildren(commentVo.getId());
                        commentVo.setChildren(childrenList);
                        return commentVo;
                    }
                }).collect(Collectors.toList());
        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));

    }
}



















