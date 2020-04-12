package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.Theme;

public interface ThemeService {
    void saveTheme(Theme theme);
    Theme getThemeById(Long uid);
}
