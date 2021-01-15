package com.lingxiao.blog.bean.vo;

import com.lingxiao.blog.bean.po.Label;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ArticleDetailVo {
    private String id;
    private String userId;
    private String categoryId;
    private String categoryName;
    private String title;
    private String content;

    private String headImage;
    private Long watchCount;
    private Long commentCount;
    private String createAt;
    private String updateAt;
    private Long likeCount;
    private Integer status;
    private List<Label> labels;
}
