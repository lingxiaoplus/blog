package com.lingxiao.blog.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.Email;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.EmailMapper;
import com.lingxiao.blog.service.system.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailMapper emailMapper;
    @Override
    public void addEmail(Email email) {
        List<Email> all = emailMapper.selectAll();
        if (all == null || all.size() == 0){
            email.setEnabled(ContentValue.EMAIL_ENABLE);
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
        if(ContentValue.EMAIL_ENABLE == email.getEnabled()){
            //如果是启用了某个邮箱  则需要把之前启用的邮箱禁用
            Email select = new Email();
            select.setEnabled(ContentValue.EMAIL_ENABLE);
            Email one = emailMapper.selectOne(select);
            if (one != null) {
                one.setEnabled(ContentValue.EMAIL_DISABLE);
                int count = emailMapper.updateByPrimaryKey(one);
                if (count != 1) {
                    throw new BlogException(ExceptionEnum.UPDATE_EMAIL_ERROR);
                }
            }
        }
        int count = emailMapper.updateByPrimaryKey(email);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.UPDATE_EMAIL_ERROR);
        }
    }

    @Override
    public Email getEnableEmail() {
        Email select = new Email();
        select.setEnabled(ContentValue.EMAIL_ENABLE);
        Email one = emailMapper.selectOne(select);
        if (one == null) {
            throw new BlogException(ExceptionEnum.SELECT_EMAIL_ERROR);
        }
        return one;
    }

    @Override
    public PageResult<Email> getEmails(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Email> emailList = emailMapper.selectAll();
        PageInfo<Email> pageInfo = PageInfo.of(emailList);
        return new PageResult<Email>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }
}
