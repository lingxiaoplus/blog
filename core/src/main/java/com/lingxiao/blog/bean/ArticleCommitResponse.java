package com.lingxiao.blog.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author renml
 * @date 2022/11/28 18:28
 */
@NoArgsConstructor
@Data
public class ArticleCommitResponse {

  @JsonProperty("remain")
  private Integer remain;
  @JsonProperty("success")
  private Integer success;
  @JsonProperty("not_same_site")
  private List<?> notSameSite;
  @JsonProperty("not_valid")
  private List<?> notValid;
}
