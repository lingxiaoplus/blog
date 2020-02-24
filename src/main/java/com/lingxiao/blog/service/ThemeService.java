package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.Theme;

public interface ThemeService {
    void saveTheme(Theme theme);
    Theme getThemeById(Long uid);
}
