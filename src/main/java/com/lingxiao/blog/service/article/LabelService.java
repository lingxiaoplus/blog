package com.lingxiao.blog.service.article;

import com.lingxiao.blog.bean.Label;
import com.lingxiao.blog.global.api.PageResult;

import java.util.List;

public interface LabelService{
    void addLabel(Label label);
    void deleteLabel(List<Long> ids);
    void updateLabel(Label label);
    PageResult<Label> getLabels(String keyword, int pageNum, int pageSize);

    List<Label> getLabelByArticleId(long id);

    void updateArticleLabelByArticleId(long id);
}
