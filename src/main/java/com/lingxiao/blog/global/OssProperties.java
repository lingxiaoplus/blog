package com.lingxiao.blog.global;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oss")
@Data
@NoArgsConstructor
public class OssProperties {
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String prefixImg;
    private String temporaryFolder;
}
