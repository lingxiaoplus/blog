package com.lingxiao.blog.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "menu_role")
public class MenuRole {
    @Id
    private Long id;
    private Long mid;
    private Long rid;
}
