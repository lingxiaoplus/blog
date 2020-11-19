package com.lingxiao.blog.bean.po;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Table(name = "friend_link")
@Data
public class FriendLink {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @NotBlank(message = "名字不能为空")
    private String name;
    @NotBlank(message = "链接不能为空")
    private String link;
    private Integer enabled;
    private String description;
}
