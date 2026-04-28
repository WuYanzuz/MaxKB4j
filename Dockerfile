# ============================================
# MaxKB4j 多阶段构建 Dockerfile
# 支持在容器内完成 Maven 编译，无需本地安装 JDK/Maven
#
# 使用方法：
#   1. 直接构建镜像：docker build -t maxkb4j:latest .
#   2. 或使用 docker-compose up --build 一键启动
# ============================================

# ---------- 第一阶段：编译 ----------
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# 先复制 pom.xml，利用 Docker 缓存层加速依赖下载
COPY pom.xml .
COPY maxkb4j-common/pom.xml ./maxkb4j-common/
COPY maxkb4j-core/pom.xml ./maxkb4j-core/
COPY maxkb4j-service/pom.xml ./maxkb4j-service/
COPY maxkb4j-service-api/pom.xml ./maxkb4j-service-api/
COPY maxkb4j-start/pom.xml ./maxkb4j-start/

# 预下载依赖（缓存层）
RUN mvn dependency:go-offline -B -pl maxkb4j-start -am

# 复制完整源码并编译
COPY . .
RUN mvn clean package -DskipTests -B -pl maxkb4j-start -am

# ---------- 第二阶段：运行 ----------
FROM amazoncorretto:21

LABEL maintainer="tarzan <1334512682@qq.com>"

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /opt/running/

# 从构建阶段复制 jar 包
COPY --from=builder /build/maxkb4j-start/target/maxkb4j-start.jar /opt/running/maxkb4j-start.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dfile.encoding=UTF-8", "maxkb4j-start.jar"]
