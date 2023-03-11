package com.lingxiao.blog.bean;

import java.util.List;
import lombok.Data;

/**
 * @author renml
 * @date 2022/11/28 17:48
 */
@Data
public class ArticleCommitUrlVo {
  private String site;
  private String token;
  /**
   * 目前只支持百度
   */
  private String seoType;
  private List<String> urls;
}
