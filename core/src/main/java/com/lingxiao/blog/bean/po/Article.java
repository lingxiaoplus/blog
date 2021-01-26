package com.lingxiao.blog.bean.po;

import com.lingxiao.blog.bean.BaseModel;
import lombok.Data;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Admin
 */
@Table(name = "articles")
@Data
public class Article extends BaseModel {
    private Long userId;
    @NotNull(message = "分类id不能为空")
    private Long categoryId;
    @NotBlank(message = "文章标题不能为空")
    private String title;
    @NotBlank(message = "文章内容不能为空")
    private String content;

    private String headImage;
    private Long watchCount;
    private Long commentCount;
    private Long likeCount;
    private Integer status;
    private String author;
    private String categoryName;

    @Transient
    private List<Long> labelIds;

    private List<Label> labels;
}

