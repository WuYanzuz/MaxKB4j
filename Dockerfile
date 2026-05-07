# ============================================
# MaxKB4j 运行镜像 Dockerfile
#
# 构建前准备（只需一次）：
#   1. 拉取基础镜像（换手机热点执行一次即可，之后本地有缓存）：
#      docker pull amazoncorretto:21
#   2. 在本地执行 Maven 编译：
#      mvn clean package -DskipTests -pl maxkb4j-start -am
#   3. 构建镜像：
#      docker build -t maxkb4j:latest .
#   4. 启动所有服务：
#      docker-compose up -d
# ============================================

FROM amazoncorretto:21

LABEL maintainer="tarzan <1334512682@qq.com>"

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /opt/running/

COPY maxkb4j-start/target/maxkb4j-start.jar /opt/running/maxkb4j-start.jar

# 外置配置目录（可通过 volume 挂载覆盖）
RUN mkdir -p /opt/running/config

EXPOSE 8080

# Spring Boot 会自动加载 ./config/application.yml 覆盖内置配置
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "maxkb4j-start.jar", "--spring.config.additional-location=optional:file:/opt/running/config/"]
