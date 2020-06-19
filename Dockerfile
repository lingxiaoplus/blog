FROM java:latest
MAINTAINER "lingxiao"<1755979775@qq.com>
ADD target/blog-0.0.1-SNAPSHOT.jar blog.jar
#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
#RUN mvn package -P pro -Dmaven.test.skip=true
VOLUME /tmp
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