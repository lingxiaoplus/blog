package com.lingxiao.blog.enums;

public enum CommentState {
    UNDER_APPROVAL(-1,"评论待审核"),
    REJECT(0,"评论被驳回"),
    APPROVAL(1,"评论已通过"),
    DELETE(2,"评论已删除"),
    RUBBISH(3,"辣鸡评论"),
    ;
    private int state;
    private String message;

    CommentState(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public int getState() {
        return state;
    }
}
