package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.Hitokoto;
import com.lingxiao.blog.bean.po.Theme;

public interface ThemeService {
    void saveTheme(Theme theme);
    Theme getThemeById(Long uid);

    Hitokoto getHitokoto();
}
