package com.lingxiao.blog.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "dictionary")
@Data
public class Dictionary implements Serializable {
    @Id
    private String id;
    private String name;
    private String code;
    private String parentId;
}
