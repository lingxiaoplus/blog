package com.lingxiao.blog.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    public UserInfo(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
