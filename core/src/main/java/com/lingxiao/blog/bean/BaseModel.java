package com.lingxiao.blog.bean;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author lingxiao
 */
public class BaseModel {
    @Id
    private Long id;
    private Date createAt;
    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
