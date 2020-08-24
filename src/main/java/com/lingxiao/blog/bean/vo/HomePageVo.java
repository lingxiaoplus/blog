package com.lingxiao.blog.bean.vo;

import com.lingxiao.blog.bean.Article;
import com.lingxiao.blog.bean.Hitokoto;
import com.lingxiao.blog.global.api.PageResult;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HomePageVo implements Serializable {
    private Hitokoto hitokoto;
    private List<ArticleVo>  banners;
}
