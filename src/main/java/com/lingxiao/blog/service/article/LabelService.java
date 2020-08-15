package com.lingxiao.blog.service.article;

import com.lingxiao.blog.bean.Label;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.global.api.ResponseResult;

import java.util.List;

public interface LabelService{
    void addLabel(Label label);
    void deleteLabel(List<Long> ids);
    void updateLabel(Label label);
    PageResult<Label> getLabels(String keyword, int pageNum, int pageSize);

    ResponseResult<List<Label>> getAllLabels();

    List<Label> getLabelByArticleId(long id);

    void updateArticleLabelByArticleId(long id, List<Long> labelIds);
}
