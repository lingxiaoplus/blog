FROM java:latest
MAINTAINER "lingxiao"<1755979775@qq.com>
ADD target/blog-0.0.1-SNAPSHOT.jar blog.jar
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
ENV PASSWORD=lE1rl5K$
EXPOSE 8081
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djasypt.encryptor.password=$PASSWORD -Djava.security.egd=file:/dev/./urandom -jar /blog.jar" ]