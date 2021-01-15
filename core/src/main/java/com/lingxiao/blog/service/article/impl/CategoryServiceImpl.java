package com.lingxiao.blog.service.article.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.po.Category;
import com.lingxiao.blog.bean.po.Label;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.RedisConstants;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.CategoryMapper;
import com.lingxiao.blog.service.article.CategoryService;
import com.lingxiao.blog.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addCategory(Category category) {
        category.setCreateAt(new Date());
        int count = categoryMapper.insertSelective(category);
        if (count != 1){
            throw new BlogException(ExceptionEnum.CATEGORY_INSERT_ERROR);
        }
        redisUtil.delRedis(RedisConstants.KEY_BACK_CATEGORY_LIST);
    }

    @Override
    public void deleteCategory(Long id) {
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.CATEGORY_DELETE_ERROR);
        }
        redisUtil.delRedis(RedisConstants.KEY_BACK_CATEGORY_LIST);
    }

    @Override
    public void updateCategory(Category category) {
        category.setCreateAt(new Date());
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.CATEGORY_UPDATE_ERROR);
        }
        redisUtil.delRedis(RedisConstants.KEY_BACK_CATEGORY_LIST);
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
        List<Category> cacheList = redisUtil.getListByKey(RedisConstants.KEY_BACK_CATEGORY_LIST);
        if (!CollectionUtils.isEmpty(cacheList)){
            return cacheList;
        }
        List<Category> categoryList = categoryMapper.selectAll();
        if (CollectionUtils.isEmpty(categoryList)){
            throw new BlogException(ExceptionEnum.CATEGORY_SELECT_ERROR);
        }
        if (!CollectionUtils.isEmpty(categoryList)){
            redisUtil.rightPushAll(RedisConstants.KEY_BACK_CATEGORY_LIST,categoryList, TimeUnit.HOURS.toMillis(1));
        }
        return categoryList;
    }

    @Override
    public PageResult<Category> getCategories(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Example example = new Example(Label.class);
        example.createCriteria()
                .andLike("name","%"+keyword+"%");
        List<Category> categories = categoryMapper.selectByExample(example);
        PageInfo<Category> pageInfo = PageInfo.of(categories);
        return new PageResult<Category>(pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }
}
