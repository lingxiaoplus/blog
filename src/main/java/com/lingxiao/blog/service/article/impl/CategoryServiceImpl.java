package com.lingxiao.blog.service.article.impl;

import com.lingxiao.blog.bean.Category;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.mapper.CategoryMapper;
import com.lingxiao.blog.service.article.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void addCategory(Category category) {
        category.setCreateAt(new Date());
        int count = categoryMapper.insertSelective(category);
        if (count != 1){
            throw new BlogException(ExceptionEnum.CATEGORY_INSERT_ERROR);
        }
    }

    @Override
    public void deleteCategory(Long id) {
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.CATEGORY_DELETE_ERROR);
        }
    }

    @Override
    public void updateCategory(Category category) {
        category.setCreateAt(new Date());
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.CATEGORY_UPDATE_ERROR);
        }
    }

    @Override
    public Category selectById(Long id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null){
            throw new BlogException(ExceptionEnum.CATEGORY_SELECT_ERROR);
        }
        return category;
    }

    @Override
    public List<Category> selectAll() {
        List<Category> categoryList = categoryMapper.selectAll();
        if (CollectionUtils.isEmpty(categoryList)){
            throw new BlogException(ExceptionEnum.CATEGORY_SELECT_ERROR);
        }
        return categoryList;
    }
}
