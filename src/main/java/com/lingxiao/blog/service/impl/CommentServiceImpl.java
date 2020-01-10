package com.lingxiao.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.Article;
import com.lingxiao.blog.bean.Comment;
import com.lingxiao.blog.bean.User;
import com.lingxiao.blog.bean.vo.CommentVo;
import com.lingxiao.blog.enums.CommentState;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.LoginInterceptor;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.ArticleMapper;
import com.lingxiao.blog.mapper.CommentMapper;
import com.lingxiao.blog.mapper.UserMapper;
import com.lingxiao.blog.service.ArticleService;
import com.lingxiao.blog.service.CommentService;
import com.lingxiao.blog.utils.IPUtils;
import com.lingxiao.blog.utils.IdWorker;
import com.lingxiao.blog.utils.UIDUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int getCommentCount(long articleId){
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        return commentMapper.selectCount(comment);
    }

    @Override
    public void addComment(Comment comment) {
        comment.setId(UIDUtil.nextId());
        comment.setStatus(CommentState.UNDER_APPROVAL.getState());
        comment.setCreateAt(new Date());
        //comment.setUserId(LoginInterceptor.getUserInfo().getId());
        int count = commentMapper.insertSelective(comment);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.COMMENT_INSERT_ERROR);
        }
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
                .map((this::parseComment))
                .collect(Collectors.toList());
        return new PageResult<CommentVo>(pageInfo.getTotal(),pageInfo.getPages(),commentVoList);
    }

    @Override
    public PageResult<CommentVo> getCommentsByArticleId(int pageNum, int pageSize, long id) {
        PageHelper.startPage(pageNum,pageSize);
        Comment comment = new Comment();
        comment.setArticleId(id);
        List<Comment> comments = commentMapper.select(comment);
        PageInfo<Comment> pageInfo = PageInfo.of(comments);

        List<CommentVo> commentVoList = pageInfo.getList()
                .stream()
                .map(this::parseComment)
                .collect(Collectors.toList());

        return new PageResult<CommentVo>(pageInfo.getTotal(),pageInfo.getPages(),commentVoList);
    }

    private CommentVo parseComment(Comment item){



        CommentVo commentVo = new CommentVo();
        commentVo.setId(String.valueOf(item.getId()));
        commentVo.setUserId(String.valueOf(item.getUserId()));
        commentVo.setArticleId(String.valueOf(item.getArticleId()));
        commentVo.setLikeCount(String.valueOf(item.getLikeCount()));
        commentVo.setContent(item.getContent());


        Comment childrenComment = new Comment();
        childrenComment.setParentId(item.getId());
        List<Comment> children = commentMapper.selectByExample(childrenComment);
        if (!CollectionUtils.isEmpty(children)){
            //commentVo.setChildren(parseComment(children));
        }

        User user = userMapper.selectByPrimaryKey(item.getUserId());
        commentVo.setUsername(user.getUsername());
        commentVo.setUserEmail(user.getEmail());
        commentVo.setUserIP(IPUtils.numToIP(user.getUserIp()));
        commentVo.setNickname(user.getNickname());

        Article article = articleMapper.selectByPrimaryKey(item.getArticleId());
        commentVo.setArticleTitle(article.getTitle());
        commentVo.setStatus(item.getStatus());
        DateTime dateTime = new DateTime(item.getCreateAt());
        String dateString = dateTime.toString("yyyy-MM-dd");
        commentVo.setCreateAt(dateString);

        return commentVo;
    }
}
