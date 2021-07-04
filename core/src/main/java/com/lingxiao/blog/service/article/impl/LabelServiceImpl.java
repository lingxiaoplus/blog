package com.lingxiao.blog.service.article.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.lingxiao.blog.bean.po.ArticleLabel;
import com.lingxiao.blog.bean.po.Label;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.mapper.ArticleLabelMapper;
import com.lingxiao.blog.mapper.LabelMapper;
import com.lingxiao.blog.service.article.LabelService;
import com.lingxiao.blog.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private ArticleLabelMapper articleLabelMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addLabel(Label label){
        label.setCreateAt(new Date());
        int count = labelMapper.insertSelective(label);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.LABEL_INSERT_ERROR);
        }
        redisUtil.delRedis(RedisConstants.KEY_FRONT_LABEL_LIST);
    }

    @Override
    public void deleteLabel(List<Long> ids){
        int count = labelMapper.deleteByIdList(ids);
        if (count != ids.size()) {
            throw new BlogException(ExceptionEnum.LABEL_DELETE_ERROR);
        }
        redisUtil.delRedis(RedisConstants.KEY_FRONT_LABEL_LIST);
    }

    @Override
    public void updateLabel(Label label){
        label.setCreateAt(new Date());
        int count = labelMapper.updateByPrimaryKey(label);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.LABEL_UPDATE_ERROR);
        }
        redisUtil.delRedis(RedisConstants.KEY_FRONT_LABEL_LIST);
    }

    @Override
    public PageResult<Label> getLabels(String keyword, int pageNum, int pageSize) {
        PageMethod.startPage(pageNum,pageSize);
        Example example = new Example(Label.class);
        if (StringUtils.isNotBlank(keyword)){
            example.createCriteria()
                    .andLike("name","%"+keyword+"%");
        }
        List<Label> articles = labelMapper.selectByExample(example);
        PageInfo<Label> pageInfo = PageInfo.of(articles);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public ResponseResult<List<Label>> getAllLabels(){
        ResponseResult<List<Label>> result = new ResponseResult<>();
        List<Label>  labels = redisUtil.getListByKey(RedisConstants.KEY_FRONT_LABEL_LIST);
        if (!CollectionUtils.isEmpty(labels)){
            result.setData(labels);
            return result;
        }
        labels = labelMapper.selectAll();
        result.setData(labels);
        if (!CollectionUtils.isEmpty(labels)){
            redisUtil.rightPushAll(RedisConstants.KEY_FRONT_LABEL_LIST,labels);
        }
        return result;
    }

    @Override
    public List<Label> getLabelByArticleId(long id){
        List<Label> labels = redisUtil.getListByKey(String.format(RedisConstants.KEY_BACK_ARTICLE_LABELS,id));
        if (!CollectionUtils.isEmpty(labels)){
            return labels;
        }
        ArticleLabel articleLabel = new ArticleLabel();
        articleLabel.setArticleId(id);
        List<ArticleLabel> articleLabels = articleLabelMapper.select(articleLabel);
        List<Long> idList = articleLabels.stream().map(ArticleLabel::getLabelId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)){
            return Collections.emptyList();
        }
        labels = labelMapper.selectByIdList(idList);
        if (!CollectionUtils.isEmpty(labels)){
            redisUtil.rightPushAll(String.format(RedisConstants.KEY_BACK_ARTICLE_LABELS,id),labels);
        }
        return labels;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleLabelByArticleId(long id, List<Long> labelIds){
        ArticleLabel deleteArticle = new ArticleLabel();
        deleteArticle.setArticleId(id);
        articleLabelMapper.delete(deleteArticle);
        List<ArticleLabel> insertList = labelIds.stream().map(labelId -> {
            ArticleLabel articleLabel = new ArticleLabel();
            articleLabel.setArticleId(id);
            articleLabel.setLabelId(labelId);
            return articleLabel;
        }).collect(Collectors.toList());
        int insertCount = articleLabelMapper.insertList(insertList);
        if (insertCount != insertList.size()){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
        redisUtil.delRedis(String.format(RedisConstants.KEY_BACK_ARTICLE_LABELS,id));
    }

}
