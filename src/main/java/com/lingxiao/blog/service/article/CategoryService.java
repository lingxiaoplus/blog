package com.lingxiao.blog.service.article;

import com.lingxiao.blog.bean.po.Category;
import com.lingxiao.blog.global.api.PageResult;

import java.util.List;

public interface CategoryService {
    void addCategory(Category category);
    void deleteCategory(Long id);
    void updateCategory(Category category);
    Category selectById(Long id);
    List<Category> selectAll();

    PageResult<Category> getCategories(String keyword, int pageNum, int pageSize);
}
