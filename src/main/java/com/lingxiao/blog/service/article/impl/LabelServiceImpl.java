package com.lingxiao.blog.service.article.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.Label;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.LabelMapper;
import com.lingxiao.blog.service.article.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelMapper labelMapper;
    @Override
    public void addLabel(Label label){
        label.setCreateAt(new Date());
        int count = labelMapper.insertSelective(label);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.LABEL_INSERT_ERROR);
        }
    }
    @Override
    public void deleteLabel(List<Long> ids){
        int count = labelMapper.deleteByIdList(ids);
        if (count != ids.size()) {
            throw new BlogException(ExceptionEnum.LABEL_DELETE_ERROR);
        }
    }
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
}
