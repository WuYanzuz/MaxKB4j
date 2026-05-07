# MaxKB4j 部署文档

## 环境要求

- Docker Desktop（Windows）或 Docker（Linux）
- Maven 3.6+（构建机器需要）
- JDK 21+（构建机器需要）

---

## 一、构建镜像（在源码机器上执行）

### 1. 拉取基础镜像（只需一次，需要能访问 Docker Hub 的网络）

```bash
docker pull amazoncorretto:21
```

> 如公司网络无法访问，可临时切换手机热点执行。

### 2. Maven 编译

```bash
cd E:\aichat\MaxKB4j
mvn clean package -DskipTests -pl maxkb4j-start -am
```

### 3. 构建应用镜像

```bash
docker build -t maxkb4j:latest .
```

### 4. 导出所有镜像为 tar 包（用于跨机器部署）

```bash
docker save maxkb4j:latest registry.cn-hangzhou.aliyuncs.com/tarzanx/pgvector:0.7.0-pg15 registry.cn-hangzhou.aliyuncs.com/tarzanx/mongo:8.0 -o maxkb4j-all-images.tar
```

---

## 二、部署（在目标机器上执行）

### 需要的文件

将以下文件拷贝到目标机器同一目录下：

```
maxkb4j-all-images.tar      # 镜像包
docker-compose.yml          # 启动配置
config/
  application-prod.yml      # 外置配置文件（数据库地址、密码等）
server.crt                  # SSL 证书（如无可创建空文件）
```

### 1. 导入镜像

```bash
docker load -i maxkb4j-all-images.tar
```

### 2. 创建空证书文件（如不需要 SSL）

**Windows：**
```powershell
New-Item -ItemType File -Path server.crt -Force
```

**Linux：**
```bash
touch server.crt
```

### 3. 启动服务

```bash
docker-compose up -d
```

### 4. 查看启动状态

```bash
docker-compose ps
docker-compose logs -f maxkb4j
```

---

## 三、访问

| 地址 | 说明 |
|---|---|
| http://localhost:8080 | Web 管理界面 |
| 默认账号 | admin |
| 默认密码 | MaxKB4j |

---

## 四、数据持久化目录

| 目录 | 说明 |
|---|---|
| `./postgres/data` | PostgreSQL 数据 |
| `./mongo/data` | MongoDB 数据 |
| `./logs` | 应用日志 |

---

## 五、修改配置（无需重新构建镜像）

编辑 `config/application-prod.yml` 后重启应用即可生效：

```bash
docker-compose restart maxkb4j
```

---

## 六、更新应用

1. 在源码机器上重新编译并构建镜像（执行第一节步骤 2~4）
2. 将新的 `maxkb4j-all-images.tar` 拷贝到目标机器
3. 导入并重启：

```bash
docker load -i maxkb4j-all-images.tar
docker-compose up -d --no-deps maxkb4j
```

---

## 七、常用 Docker 命令

### 服务管理

```bash
# 启动所有服务（后台运行）
docker-compose up -d

# 停止所有服务（保留数据）
docker-compose down

# 重启单个服务
docker-compose restart maxkb4j

# 停止单个服务
docker-compose stop maxkb4j

# 启动单个服务
docker-compose start maxkb4j

# 停止并删除容器+数据卷（慎用，数据会丢失）
docker-compose down -v
```

### 查看状态

```bash
# 查看所有容器运行状态
docker-compose ps

# 查看所有本地镜像
docker images

# 查看正在运行的容器
docker ps

# 查看所有容器（包含已停止的）
docker ps -a
```

### 日志查看

```bash
# 实时查看应用日志
docker-compose logs -f maxkb4j

# 查看最近 100 行日志
docker-compose logs --tail=100 maxkb4j

# 查看所有服务日志
docker-compose logs -f

# 查看 PostgreSQL 日志
docker-compose logs -f postgres

# 查看 MongoDB 日志
docker-compose logs -f mongo
```

### 进入容器

```bash
# 进入应用容器
docker exec -it maxkb4j-app bash

# 进入 PostgreSQL 容器并连接数据库
docker exec -it maxkb4j-pgvector psql -U tarzan_postgres -d MaxKB4j

# 进入 MongoDB 容器
docker exec -it maxkb4j-mongo mongosh -u tarzan_mongo -p ycn4NRhjN2 --authenticationDatabase admin
```

### 镜像管理

```bash
# 导出镜像为 tar 包
docker save maxkb4j:latest registry.cn-hangzhou.aliyuncs.com/tarzanx/pgvector:0.7.0-pg15 registry.cn-hangzhou.aliyuncs.com/tarzanx/mongo:8.0 -o maxkb4j-all-images.tar

# 导入 tar 包镜像
docker load -i maxkb4j-all-images.tar

# 删除旧镜像（先停止容器）
docker rmi maxkb4j:latest

# 清理无用镜像/容器/缓存
docker system prune -f
```

### 资源查看

```bash
# 查看容器 CPU/内存占用
docker stats

# 查看磁盘使用情况
docker system df
```
