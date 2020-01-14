package com.lingxiao.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.Email;
import com.lingxiao.blog.bean.Label;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.EmailMapper;
import com.lingxiao.blog.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailMapper emailMapper;
    @Override
    public void addEmail(Email email) {
        List<Email> all = emailMapper.selectAll();
        if (all == null || all.size() == 0){
            email.setEnabled(1);
        }
        int count = emailMapper.insertSelective(email);
        if (count != 1){
            throw new BlogException(ExceptionEnum.ADD_EMAIL_ERROR);
        }
    }

    @Override
    public void deleteEmail(List<Long> ids) {
        int count = emailMapper.deleteByIdList(ids);
        if (count != ids.size()) {
            throw new BlogException(ExceptionEnum.DELETE_EMAIL_ERROR);
        }
    }

    @Override
    public void updateEmail(Email email) {
        int count = emailMapper.updateByPrimaryKey(email);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.UPDATE_EMAIL_ERROR);
        }
    }

    @Override
    public PageResult<Email> getEmails(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Email> emailList = emailMapper.selectAll();
        PageInfo<Email> pageInfo = PageInfo.of(emailList);
        return new PageResult<Email>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }
}
