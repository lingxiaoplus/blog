package com.lingxiao.blog.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingxiao.blog.bean.FriendLink;
import com.lingxiao.blog.enums.ExceptionEnum;
import com.lingxiao.blog.exception.BlogException;
import com.lingxiao.blog.global.ContentValue;
import com.lingxiao.blog.global.api.PageResult;
import com.lingxiao.blog.mapper.FriendLinkMapper;
import com.lingxiao.blog.service.system.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public void addLink(FriendLink friendLink) {
        if (null == friendLink.getEnabled()) friendLink.setEnabled(ContentValue.EMAIL_ENABLE);
        int count = friendLinkMapper.insertSelective(friendLink);
        if (count != 1){
            throw new BlogException(ExceptionEnum.ADD_LINK_ERROR);
        }
    }

    @Override
    public void deleteLink(List<Long> ids) {
        int count = friendLinkMapper.deleteByIdList(ids);
        if (count != ids.size()) {
            throw new BlogException(ExceptionEnum.DELETE_LINK_ERROR);
        }
    }

    @Override
    public void updateLink(FriendLink friendLink) {
        int count = friendLinkMapper.updateByPrimaryKey(friendLink);
        if (count != 1) {
            throw new BlogException(ExceptionEnum.UPDATE_LINK_ERROR);
        }
    }

    @Override
    public PageResult<FriendLink> getLinks(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<FriendLink> emailList = friendLinkMapper.selectAll();
        PageInfo<FriendLink> pageInfo = PageInfo.of(emailList);
        return new PageResult<FriendLink>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }

    @Override
    public FriendLink getEnableLink() {
        FriendLink select = new FriendLink();
        select.setEnabled(ContentValue.EMAIL_ENABLE);
        FriendLink one = friendLinkMapper.selectOne(select);
        if (one == null) {
            throw new BlogException(ExceptionEnum.SELECT_LINK_ERROR);
        }
        return one;
    }
}
