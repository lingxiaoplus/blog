package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.Category;

import java.util.List;

public interface CategoryService {
    void addCategory(Category category);
    void deleteCategory(Long id);
    void updateCategory(Category category);
    Category selectById(Long id);
    List<Category> selectAll();
}
