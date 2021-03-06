package com.lingxiao.blog.global;

/**
 * @author renml
 * @date 2020/12/4 16:51
 * redis常量类
 */
public class RedisConstants {
    private RedisConstants(){
    }
    public static final String KEY_FRONT_STATTICS_IP_COUNT = "blog:front:ip:count:%s";
    public static final String KEY_FRONT_LABEL_LIST = "blog:front:label:list";
    public static final String KEY_FRONT_ARTICLE_TIMELINE_YEAR = "blog:front:article:timeline:year:%s";
    /**
     * 访问的ip +今日日期
     */
    public static final String KEY_FRONT_STATTICS_IP_TODAY = "blog:front:ip:today:%s,%s";
    public static final String KEY_FRONT_STATTICS_SITE_COUNT = "blog:front:site:count:%s";
    public static final String KEY_FRONT_STATTICS_VISIT_ANALYSE = "blog:front:visit:analyse:%s";
    public static final String KEY_FRONT_STATTICS_ARTICLE_INCREASE = "blog:front:article:increase:%s";

    public static final String KEY_BACK_CATEGORY_LIST = "blog:back:category:list:%s";
    public static final String KEY_BACK_REGISTER_EMAIL_CODE = "blog:back:register:email:code:%s";

    public static final String KEY_BACK_BINGIMAGE_TASK_MAXPAGE = "blog:back:bingImage:task:maxPage";
    public static final String KEY_BACK_BINGIMAGE_TASK_CURRENTPAGE = "blog:back:bingImage:task:currentPage";
    /**
     * 文章
     */
    public static final String KEY_BACK_ARTICLE_PAGE = "blog:back:article:page:currentPage:%s:pageSize:%s";
    public static final String KEY_BACK_ARTICLE_DETAIL = "blog:back:article:detail:%s";
    public static final String KEY_BACK_ARTICLE_BANNER = "blog:back:article:banner:%s";
    public static final String KEY_BACK_ARTICLE_LABELS = "blog:back:article:labels:%s";


    public static final String KEY_BACK_ARTICLE_COMMENT_DETAIL = "blog:back:article:comment:%s";

    public static final String KEY_BACK_DICTIONARY_NAME_CODE = "blog:back:dictionary:name:%s:code:%s";

}
