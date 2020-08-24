package com.lingxiao.blog.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Hitokoto implements Serializable {

    /**
     * id : 5950
     * uuid : 9685ca1d-7815-4f96-9ac7-f70f6cff0f2e
     * hitokoto : 多少事，从来急，天地转，光阴迫，一万年太久，只争朝夕。
     * type : k
     * from : 毛泽东语录
     * from_who : 毛泽东
     * creator : 微笑阿猪
     * creator_uid : 5861
     * reviewer : 4756
     * commit_from : web
     * created_at : 1586611930
     * length : 27
     */

    private int id;
    private String uuid;
    private String hitokoto;
    private String type;
    private String from;
    private String from_who;
    private String creator;
    private int creator_uid;
    private int reviewer;
    private String commit_from;
    private String created_at;
    private int length;
}
