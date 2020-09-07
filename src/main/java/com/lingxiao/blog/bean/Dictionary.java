package com.lingxiao.blog.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dictionary")
@Data
public class Dictionary {
    @Id
    private String id;
    private String name;
    private String code;
    private String parentId;
}
