package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.po.Email;
import com.lingxiao.blog.global.api.PageResult;

import java.util.List;

public interface EmailService {
    void addEmail(Email email);
    void deleteEmail(List<Long> ids);
    void updateEmail(Email email);
    PageResult<Email> getEmails(int pageNum, int pageSize);

    Email getEnableEmail();
}
