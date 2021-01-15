package com.lingxiao.blog.bean.vo;

import com.lingxiao.blog.bean.Hitokoto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HomePageVo implements Serializable {
    private Hitokoto hitokoto;
    private List<ArticleVo>  banners;
}
