package com.lingxiao.blog.bean.vo;

import com.lingxiao.blog.bean.Article;
import com.lingxiao.blog.bean.Hitokoto;
import com.lingxiao.blog.global.api.PageResult;
import lombok.Data;

import java.util.List;

@Data
public class HomePageVo {
    private Hitokoto hitokoto;
    private List<ArticleVo>  banners;
}
