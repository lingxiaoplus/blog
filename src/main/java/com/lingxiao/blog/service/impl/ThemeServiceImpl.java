package com.lingxiao.blog.service.impl;

import com.lingxiao.blog.bean.Theme;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.mapper.ThemeMapper;
import com.lingxiao.blog.service.ThemeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    private ThemeMapper themeMapper;

    @Override
    public void saveTheme(Theme theme) {
        Long uid = theme.getId();
        if (0L == uid){
            throw new BlogException(ExceptionEnum.SAVE_DEFAULT_THEME_ERROR);
        }
        Theme select = themeMapper.selectByPrimaryKey(uid);
        int count;
        if (select == null) {
            count = themeMapper.insertSelective(theme);
        }else {
            count = themeMapper.updateByPrimaryKeySelective(theme);
        }
        if (count != 1){
            throw new BlogException(ExceptionEnum.SAVE_THEME_ERROR);
        }
    }

    @Override
    public Theme getThemeById(Long uid) {
        Theme theme = themeMapper.selectByPrimaryKey(uid);
        if (theme == null) {
            //返回一个默认的主题
            theme = themeMapper.selectByPrimaryKey(0);
        }
        if (StringUtils.isBlank(theme.getMotto())){
            theme.setMotto("一言API");
        }
        return theme;
    }
}
