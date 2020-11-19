package com.lingxiao.blog.service.article.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.po.ArticleLabel;
import com.lingxiao.blog.bean.po.Label;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;
import com.lingxiao.blog.mapper.ArticleLabelMapper;
import com.lingxiao.blog.mapper.LabelMapper;
import com.lingxiao.blog.service.article.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private ArticleLabelMapper articleLabelMapper;

    @CacheEvict(value = "labels",allEntries = true)
    @Override
    public void addLabel(Label label){
        label.setCreateAt(new Date());
        int count = labelMapper.insertSelective(label);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.LABEL_INSERT_ERROR);
        }
    }

    @CacheEvict(value = "labels",allEntries = true)
    @Override
    public void deleteLabel(List<Long> ids){
        int count = labelMapper.deleteByIdList(ids);
        if (count != ids.size()) {
            throw new BlogException(ExceptionEnum.LABEL_DELETE_ERROR);
        }
    }

    @CacheEvict(value = "labels",allEntries = true)
    @Override
    public void updateLabel(Label label){
        label.setCreateAt(new Date());
        int count = labelMapper.updateByPrimaryKey(label);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.LABEL_UPDATE_ERROR);
        }
    }

    @Override
    public PageResult<Label> getLabels(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Example example = new Example(Label.class);
        example.createCriteria()
                .andLike("name","%"+keyword+"%");
        List<Label> articles = labelMapper.selectByExample(example);
        PageInfo<Label> pageInfo = PageInfo.of(articles);
        return new PageResult<Label>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Cacheable(value = "labels")
    @Override
    public ResponseResult<List<Label>> getAllLabels(){
        List<Label> labels = labelMapper.selectAll();
        ResponseResult<List<Label>> result = new ResponseResult<>();
        result.setData(labels);
        return result;
    }

    @Override
    public List<Label> getLabelByArticleId(long id){
        ArticleLabel articleLabel = new ArticleLabel();
        articleLabel.setArticleId(id);
        List<ArticleLabel> articleLabels = articleLabelMapper.select(articleLabel);
        List<Long> idList = articleLabels.stream().map(ArticleLabel::getLabelId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)){
            return null;
        }
        List<Label> labels = labelMapper.selectByIdList(idList);
        return labels;
    }

    @CacheEvict(value = "labels",allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleLabelByArticleId(long id, List<Long> labelIds){
        ArticleLabel deleteArticle = new ArticleLabel();
        deleteArticle.setArticleId(id);
        //int count = articleLabelMapper.selectCount(deleteArticle);
        //int deleteCount = articleLabelMapper.delete(deleteArticle);
        articleLabelMapper.delete(deleteArticle);
        List<ArticleLabel> insertList = labelIds.stream().map((labelId) -> {
            ArticleLabel articleLabel = new ArticleLabel();
            articleLabel.setArticleId(id);
            articleLabel.setLabelId(labelId);
            return articleLabel;
        }).collect(Collectors.toList());
        int insertCount = articleLabelMapper.insertList(insertList);
        if (insertCount != insertList.size()){
            throw new BlogException(ExceptionEnum.ILLEGA_ARGUMENT);
        }
    }

}
