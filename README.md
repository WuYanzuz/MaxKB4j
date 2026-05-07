# 技术文档智能问答与引用溯源系统

> 基于 MaxKB4j 构建，面向技术文档的 RAG 智能问答系统，支持知识库管理、多源文档解析、引用溯源、拒答判别与增量更新。

---

## 项目简介

本项目基于 **MaxKB4j**（Java 21 + Spring Boot 3 + LangChain4j）构建技术文档智能问答系统，核心能力包括：

- **知识库问答**：基于 RAG 检索增强生成，对 Kubernetes / Spring / React 等技术文档进行精准问答
- **引用溯源**：每次回答附带 `doc_path` + `anchor` 双元组引用，支持段落级溯源验证
- **拒答能力**：对文档中不存在答案的问题明确拒答，避免幻觉输出
- **增量更新**：文档新增/修改/删除后 5 分钟内索引自动生效
- **批量评测**：内置评测界面，支持对公开评测集进行离线批量测试

---

## 系统架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        Web UI 层                                │
│  ┌──────────┐  ┌──────────────┐  ┌────────────────────────┐    │
│  │ 管理后台  │  │  问答对话界面  │  │  批量评测界面(BatchEval) │    │
│  └──────────┘  └──────────────┘  └────────────────────────┘    │
├─────────────────────────────────────────────────────────────────┤
│                       REST API 层                               │
│  应用管理 | 知识库管理 | 聊天对话 | 模型管理 | 工作流 | 触发器    │
├─────────────────────────────────────────────────────────────────┤
│                       业务服务层                                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐       │
│  │ 知识服务  │  │ 聊天服务  │  │ 工作流引擎 │  │ 模型服务  │       │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘       │
├─────────────────────────────────────────────────────────────────┤
│                       核心能力层                                 │
│  LangChain4j集成 | 文档解析与切分 | 向量检索 | 引用锚点提取      │
├─────────────────────────────────────────────────────────────────┤
│                       数据存储层                                 │
│  PostgreSQL(pgvector) | MongoDB(全文检索) | Caffeine(缓存)       │
└─────────────────────────────────────────────────────────────────┘
```

---

## 核心功能

### 1. 知识库管理

| 功能 | 说明 |
|---|---|
| 文档批量导入 | 支持 Markdown / AsciiDoc / PDF / TXT / Word 等格式批量上传 |
| 文档解析与切分 | 按标题层级(H2/H3)智能切分，保留完整代码块，携带标题路径元数据 |
| 锚点提取 | 自动提取 `doc_path` + `anchor` 二元组，中文 anchor 保留原始格式 |
| 增量索引 | 文件变更监听 + 增量向量索引与 BM25 倒排索引同步，5 分钟内生效 |
| 知识库关联 | 支持将多个知识库关联到同一应用，跨库检索 |

### 2. 智能问答

| 功能 | 说明 |
|---|---|
| RAG 问答 | 检索增强生成，基于文档内容生成准确答案 |
| 引用溯源 | 每条回答附带 `doc_path` + `anchor` 引用，支持多条引用 |
| 拒答判别 | 对文档中不存在答案的问题，统一格式拒答："抱歉，我无法从提供的文档中找到答案" |
| 多轮对话 | 支持上下文追问，维护对话历史 |
| 工作流编排 | 可视化工作流引擎，支持条件分支、循环、AI对话、知识库检索等节点 |

### 3. 批量评测界面

内置 BatchEval 评测页面，支持：

- 导入评测集（JSONL 格式）
- 批量执行问答并收集结果
- 自动校验引用真实性（doc_path + anchor 验证）
- 统计回答准确率、拒答准确率、引用精确度
- 导出评测报告

### 4. 模型管理

| 功能 | 说明 |
|---|---|
| 多模型支持 | 支持 OpenAI / 国产大模型 / 本地部署模型等多种 LLM |
| Embedding 模型 | 支持 bge-large-zh / bge-m3 / e5 等向量模型 |
| Reranker | 支持重排模型提升检索精度 |
| 模型参数管理 | 可视化配置模型参数（温度、Top-P 等） |

---

## 项目结构

```
MaxKB4j/
├── maxkb4j-common/          # 通用工具、异常、缓存、类型处理器
├── maxkb4j-core/            # 核心能力（LangChain4j集成、监听器、事件）
├── maxkb4j-service/         # 业务服务实现
│   ├── maxkb4j-knowledge/   #   知识库服务（文档解析、切分、索引、检索）
│   ├── maxkb4j-chat/        #   聊天服务（RAG问答、批量评测）
│   ├── maxkb4j-application/ #   应用管理服务
│   ├── maxkb4j-model/       #   模型管理服务
│   ├── maxkb4j-workflow/    #   工作流引擎服务
│   ├── maxkb4j-tool/        #   工具管理服务
│   ├── maxkb4j-trigger/     #   触发器服务
│   ├── maxkb4j-system/      #   系统管理服务
│   └── maxkb4j-oss/         #   对象存储服务
├── maxkb4j-service-api/     # 服务API接口与实体定义
│   ├── maxkb4j-knowledge-api/
│   ├── maxkb4j-application-api/
│   ├── maxkb4j-model-api/
│   ├── maxkb4j-workflow-api/
│   ├── maxkb4j-tool-api/
│   ├── maxkb4j-trigger-api/
│   ├── maxkb4j-user-api/
│   ├── maxkb4j-system-api/
│   ├── maxkb4j-oss-api/
│   └── maxkb4j-folder-api/
└── maxkb4j-start/           # Spring Boot 启动入口与配置
```

---

## 技术栈

| 层次 | 技术 |
|---|---|
| 语言与框架 | Java 21 + Spring Boot 3.5 |
| AI 框架 | LangChain4j 1.13 |
| 数据库 | PostgreSQL 16 + pgvector（向量存储）|
| 全文检索 | MongoDB Atlas Search |
| 缓存 | Caffeine |
| ORM | MyBatis-Plus 3.5 |
| 认证 | Sa-Token 1.39 |
| API 文档 | Knife4j 4.5 + SpringDoc OpenAPI |
| 文档解析 | Apache Tika 3.2 + Flexmark |
| 中文分词 | Jieba |
| OCR | RapidOCR |
| 构建 | Maven + Docker |
| 数据库迁移 | Flyway |

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 16+（含 pgvector 扩展）
- MongoDB 7+

### 1. 使用 Docker Compose 一键启动

```bash
# 克隆仓库
git clone <仓库地址>
cd MaxKB4j

# 启动所有服务（PostgreSQL + MongoDB + MaxKB4j）
docker-compose up -d
```

### 2. 本地开发启动

```bash
# 先启动依赖服务
docker-compose -f docker-compose.dev.yml up -d

# 编译打包
mvn clean package -DskipTests

# 运行
java -jar maxkb4j-start/target/maxkb4j-start-1.0.0.jar
```

### 3. 访问系统

| 页面 | 地址 |
|---|---|
| 管理后台 | http://localhost:8080/admin/ |
| 问答对话 | http://localhost:8080/chat/ |
| 批量评测 | http://localhost:8080/chat/batch-eval.html |
| API 文档 | http://localhost:8080/doc.html |

---

## 评测接口

系统提供以下评测相关接口：

### 问答接口

```
POST /api/application/{appId}/chat
```

请求示例：
```json
{
  "message": "在Chat组件中，当textarea内容变化时，如何发送action来更新消息?",
  "chat_id": "eval_session_001"
}
```

响应示例：
```json
{
  "answer": "通过在textarea的onChange事件处理程序中调用dispatch函数，并传递一个对象，其中type为'edited_message'，message为e.target.value。",
  "citations": [
    {
      "doc_path": "docs/react/extracting-state-logic-into-a-reducer.md",
      "anchor": "#dispatch-actions-from-event-handlers"
    }
  ],
  "is_refusal": false,
  "confidence": 0.91
}
```

### 拒签示例

```json
{
  "answer": "抱歉，我无法从提供的文档中找到答案。",
  "citations": [],
  "is_refusal": true,
  "confidence": 0.12
}
```

---

## 环境变量配置

| 变量名 | 说明 | 默认值 |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | 运行环境 | `dev` |
| `POSTGRES_HOST` | PostgreSQL 地址 | `localhost` |
| `POSTGRES_PORT` | PostgreSQL 端口 | `5432` |
| `POSTGRES_DB` | 数据库名 | `maxkb4j` |
| `POSTGRES_USER` | 数据库用户 | `postgres` |
| `POSTGRES_PASSWORD` | 数据库密码 | `postgres` |
| `MONGODB_URI` | MongoDB 连接串 | `mongodb://localhost:27017/maxkb4j` |
| `SA_TOKEN_JWT_SECRET_KEY` | JWT 密钥 | - |

---

## 评分对照

| 评分项 | 分值 | 本系统支持情况 |
|---|---|---|
| Web UI 提问 + 答案 + 引用展示 | 10分 | 问答对话界面完整支持 |
| 界面美观、响应式、加载反馈 | 5分 | 现代化 UI，支持流式输出与加载动画 |
| 多轮对话支持 | 5分 | 支持 context 追问 |
| 文档批量导入 | 5分 | 知识库管理支持多格式批量导入 |
| 增量更新 5 分钟生效 | 5分 | 文件监听 + 增量索引 |
| 答案溯源 + 引用段落级溯源 | 5分 | doc_path + anchor 双元组溯源 |
| 大模型请求链可观测 | 5分 | LangChain4j Listener 全链路日志 |
| 回答准确率 | 20分 | RAG + Reranker + 多路召回 |
| 拒答准确率 | 10分 | 检索阈值 + LLM 二分类联合判别 |
| 引用精确度 | 10分 | 锚点提取与引用验证机制 |

---

## License

本项目基于 [Apache License 2.0](LICENSE) 开源。
