package com.lingxiao.blog.service.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.po.Article;
import com.lingxiao.blog.bean.po.Comment;
import com.lingxiao.blog.bean.vo.ArticleVo;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.enums.CommentState;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.CommentMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.article.ArticleService;
import com.lingxiao.blog.service.user.CommentService;
import com.lingxiao.blog.service.user.UserService;
import com.lingxiao.blog.utils.UIDUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int getCommentCount(long articleId){
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        return commentMapper.selectCount(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addComment(Comment comment) {
        comment.setId(UIDUtil.nextId());
        comment.setCreateAt(new Date());
        comment.setStatus(CommentState.UNDER_APPROVAL.getState());

        if(null != comment.getParentId() && 0 != comment.getParentId()){
            Comment parent = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (CommentState.UNDER_APPROVAL.getState() == parent.getStatus()){
                //当前这条父评论还是待审核的 但是能收到子评论 说明是管理员回复的
                comment.setStatus(CommentState.APPROVAL.getState());
                parent.setStatus(CommentState.APPROVAL.getState());
                int count = commentMapper.updateByPrimaryKeySelective(parent);
                if (count != 1) {
                    throw new BlogException(ExceptionEnum.COMMENT_INSERT_ERROR);
                }
            }
        }
        int count = commentMapper.insertSelective(comment);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.COMMENT_INSERT_ERROR);
        }
        int commentCount = getCommentCount(comment.getArticleId());
        Article article = new Article();
        article.setId(comment.getArticleId());
        article.setCommentCount(commentCount+1L);
        articleService.updateArticle(article);
    }

    @Override
    public PageResult<CommentVo> getComments(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Example example = new Example(Comment.class);
        example.createCriteria()
                .andLike("content","%"+keyword+"%");
        List<Comment> comments = commentMapper.selectByExample(example);
        PageInfo<Comment> pageInfo = PageInfo.of(comments);

        List<CommentVo> commentVoList = pageInfo.getList()
                .stream()
                //去掉楼中楼
                .filter(item-> item.getParentId() == 0)
                .map((this::parseComment))
                .collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),commentVoList);
    }

    @Override
    public PageResult<CommentVo> getCommentsByArticleId(int pageNum, int pageSize, long id) {
        PageHelper.startPage(pageNum,pageSize);
        Comment comment = new Comment();
        //已通过的评论
        comment.setStatus(CommentState.APPROVAL.getState());
        comment.setArticleId(id);
        List<Comment> comments = commentMapper.select(comment);
        PageInfo<Comment> pageInfo = PageInfo.of(comments);

        List<CommentVo> commentVoList = pageInfo.getList()
                .stream()
                //去掉楼中楼
                .filter(item-> item.getParentId() == 0)
                .map(this::parseComment)
                .collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),commentVoList);
    }


    @Override
    public void deleteComments(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        int count = commentMapper.deleteByIdList(ids);
        if(count != ids.size()){
            throw new BlogException(ExceptionEnum.COMMENT_DELETE_ERROR);
        }
    }

    @Override
    public void setCommentStatus(List<Long> ids, Integer status) {
        if (CollectionUtils.isEmpty(ids)){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        List<Comment> comments = commentMapper.selectByIdList(ids);
        comments.stream().forEach(item->{
            item.setStatus(status);
            int i = commentMapper.updateByPrimaryKeySelective(item);
            if(i != 1){
                throw new BlogException(ExceptionEnum.COMMENT_UPDATE_ERROR);
            }
        });
    }

    private CommentVo parseComment(Comment item){
        CommentVo commentVo = new CommentVo();
        commentVo.setId(String.valueOf(item.getId()));
        commentVo.setContent(item.getContent());
        commentVo.setParentId(String.valueOf(item.getParentId()));
        commentVo.setStatus(item.getStatus());
        commentVo.setUsername(item.getUsername());
        commentVo.setEmail(item.getEmail());
        commentVo.setWebsite(item.getWebsite());
        DateTime createDate = new DateTime(item.getCreateAt());
        String createString = createDate.toString("yyyy-MM-dd");
        commentVo.setCreateAt(createString);

        ArticleVo articleContent = articleService.getArticleContent(item.getArticleId());
        articleContent.setContent(null);
        commentVo.setArticle(articleContent);

        Comment childrenComment = new Comment();
        childrenComment.setParentId(item.getId());
        List<Comment> children = commentMapper.select(childrenComment);
        if (CollectionUtils.isEmpty(children)){
            //递归结束条件
            return commentVo;
        }
        //这个地方要做一个优化   楼中楼分页
        List<CommentVo> collect = children
                .stream()
                .map(this::parseComment)
                .collect(Collectors.toList());
        commentVo.setReplies(collect);
        return commentVo;
    }
}
