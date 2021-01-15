package com.lingxiao.blog.service.system;

import com.lingxiao.blog.bean.po.FriendLink;
import com.lingxiao.blog.global.api.PageResult;

import java.util.List;

public interface FriendLinkService {
    void addLink(FriendLink friendLink);
    void deleteLink(List<Long> ids);
    void updateLink(FriendLink friendLink);
    PageResult<FriendLink> getLinks(int pageNum, int pageSize);

    FriendLink getEnableLink();
}
