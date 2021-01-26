package com.lingxiao.blog.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lingxiao.blog.bean.po.Dictionary;
import com.lingxiao.blog.bean.po.Label;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ArticleVo implements Serializable {
    private String id;
    private String title;
    private String author;
    private String categoryId;
    private String categoryName;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updateAt;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createAt;
    private String headImage;
    private Long watchCount;
    private String content;
    private Long likeCount;
    private List<Label> labels;
    private Dictionary status;
    private int commentCount;
}
