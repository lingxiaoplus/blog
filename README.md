# blog后端
![](https://img.shields.io/badge/springboot-2.2.1.RELEASE-green) ![](https://img.shields.io/badge/tk.mybatis-2.1.5-green) ![](https://img.shields.io/badge/docker-19.03-blue) ![](https://img.shields.io/badge/java-1.8-green) ![](https://img.shields.io/badge/spring--security-2.2.1.RELEASE-orange) ![](https://img.shields.io/badge/qiniu--oss-7.4.0-red) ![](https://img.shields.io/badge/pagehelper-1.2.12-%2364B5F6)

基于springboot+mybatis的个人博客系统后台

演示地址：[http://blog.lingxiaomz.top](http://blog.lingxiaomz.top)

后端主要使用到的技术有：

1. SpringBoot
2. SpringSecurity
3. mybatis和tkmapper以及pagehelper
4. ehcache

项目使用docker部署在linux服务器上

### 配置

需要修改几个地方，一个是跨域的配置，在application.yml中：

```yaml
cors:
  allowedOrigins:
    - "http://blog.lingxiaomz.top"
    - "http://www.lingxiaomz.top"
    - "http://api.lingxiaomz.top"
    - "http://blog.test.lingxiaomz.top"
    - "https://blog.lingxiaomz.top"
    - "https://www.lingxiaomz.top"
    - "https://api.lingxiaomz.top"
```

api开头的为后端服务器绑定的域名，blog开头的为前端绑定的域名。如果没用https可以不用配置https的部分。

其次就是数据库的配置了，我用的flyway，需要预先自己建一个数据库，第一次运行程序会自动检测数据库表，如果没有会执行初始化数据库的脚本：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog?characterEncoding=UTF-8&useSSL=false
    username: root
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver
  flyway:
    # 到新的环境中数据库中有数据，且没有flyway_schema_history表时，是否执行迁移操作,如果设置为false，在启动时会报错，并停止迁移;
    baseline-on-migrate: false
    # 是否启用flyway
    enabled: true
    #  检测迁移脚本的路径是否存在，如不存在，则抛出异常
    check-location: true
    locations: classpath:db
    clean-disabled: true
```



文件存储模块抽离出来作为一个单独的模块：oss-spring-boot-starter，目前使用的是七牛云存储(支持minio/ftp，可以自己配)，要在application中将配置修改为自己的配置：

```yaml
oss:
  type: qiniu
  accessKey: qiniu_key # 设置好账号的ACCESS_KEY和SECRET_KEY
  secretKey: qiniu_secret
  bucketName: function  # 要上传的空间
  prefixDomain: http://img.lingxiao.top/
  temporaryFolder: /bingImage/  # 临时文件目录
  rootPath: /image  # oss空间的根目录
```



### 部署

目前还是只有后端服务会使用Dockerfile部署，后续会使用docker-compose部署。Dockerfile文件在core项目下：

```dockerfile
FROM java:latest
MAINTAINER "lingxiao"
ADD target/core-0.0.1-SNAPSHOT.jar blog.jar
ADD src/main/resources/ip2region.db /blog/ip2region.db
#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
#RUN mvn package -P pro -Dmaven.test.skip=true
VOLUME /tmp
VOLUME /blog/log /blog/log
ENV JAVA_OPTS="\
-Xms256m \
-Xmx512m \
-Xmn128m \
-Xss228k \
-XX:SurvivorRatio=8 \
-XX:+UseConcMarkSweepGC \
"

EXPOSE 8081
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /blog.jar" ]
```

个人使用的服务器配置比较低，所以Xms和Xmx设置得都比较小，不过也够用了。

然后就是nginx的配置：