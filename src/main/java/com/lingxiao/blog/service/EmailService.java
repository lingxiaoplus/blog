package com.lingxiao.blog.service;

import com.lingxiao.blog.bean.Email;
import com.lingxiao.blog.bean.Label;
import com.lingxiao.blog.global.api.PageResult;

import java.util.List;

public interface EmailService {
    void addEmail(Email email);
    void deleteEmail(List<Long> ids);
    void updateEmail(Email email);
    PageResult<Email> getEmails(int pageNum, int pageSize);
}
