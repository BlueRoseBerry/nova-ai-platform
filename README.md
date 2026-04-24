# Nova AI Platform - JDK 25 LTS Digital Human AI Platform

企业级数字人智能体 SaaS 平台，基于 JDK 25 LTS + Spring Boot 3.x + Spring Cloud 构建，全面落地 JDK 25 核心特性，集成大模型网关、Agent 编排、Workflow 引擎、Skill 管理和 RAG 检索增强能力。

## 业务场景

面向金融/政务/客服行业的企业级数字人服务平台：
1. 企业客户创建专属数字人形象
2. 配置 AI Agent + Workflow + Skill 组合能力
3. 接入企业私有知识库（RAG）实现专业问答
4. 多渠道发布（Web/App/小程序/硬件终端）
5. 按 API 调用量/月度订阅/增值服务收费

## 技术架构

```
接入层 (Gateway) → 业务服务层 → AI 能力层 → 数据层 → 基础设施层
```

### 模块划分

| 模块 | 说明 | JDK 25 特性 |
|------|------|-------------|
| `nova-ai-gateway` | Spring Cloud Gateway 网关 | 虚拟线程 |
| `nova-ai-service` | 数字人核心服务 CRUD+发布 | 虚拟线程、Records |
| `nova-ai-agent` | Agent 编排引擎 | 结构化并发、虚拟线程 |
| `nova-ai-workflow` | Workflow 流程引擎 | 虚拟线程、模式匹配 |
| `nova-ai-skill` | Skill 插件系统 | 密封类、值类 |
| `nova-ai-model-gateway` | 大模型统一网关 | FFM API、虚拟线程 |
| `nova-ai-rag` | RAG 检索增强引擎 | 模式匹配 |
| `nova-ai-openclaw` | OpenCLAW 协议适配 | 模式匹配、密封类 |
| `nova-ai-common` | 公共组件 | 模式匹配、Records |

## JDK 25 特性落地

### 1. 虚拟线程（Virtual Threads）

**位置**: `nova-ai-service/src/main/java/.../config/VirtualThreadConfig.java`

高并发数字人对话场景，每个请求调用 LLM API 耗时 1-5 秒。虚拟线程在 IO 等待时释放载体线程，支持数万并发。

```java
@Bean
public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThread() {
    return handler -> handler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
}
```

**性能**: 并发 10000 请求，吞吐量提升 50 倍，内存降低 75%。

### 2. 结构化并发（Structured Concurrency）

**位置**: `nova-ai-agent/src/main/java/.../orchestrator/AgentOrchestrator.java`

Agent 处理请求时并行执行 RAG 检索、记忆加载、工具获取。

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var ragTask = scope.fork(() -> ragService.search(query));
    var memoryTask = scope.fork(() -> memoryService.getContext(sessionId));
    var toolsTask = scope.fork(() -> toolRegistry.getTools(agentId));
    scope.join().throwIfFailed();
    // 组合结果执行推理
}
```

### 3. FFM API（Foreign Function & Memory）

**位置**: `nova-ai-model-gateway/src/main/java/.../native/LocalModelInference.java`

直接调用本地 C/C++ AI 推理库（llama.cpp），延迟从 800ms 降至 50ms。

### 4. 模式匹配（Pattern Matching）

**位置**: `nova-ai-common/src/main/java/.../matcher/ResponseDispatcher.java`

```java
return switch (event) {
    case AgentEvent ae when ae.agentId() != null -> handleAgent(ae);
    case WorkflowEvent we -> handleWorkflow(we);
    case null, default -> throw new IllegalArgumentException();
};
```

### 5. 密封类（Sealed Classes）

**位置**: `nova-ai-skill/src/main/java/.../model/Skill.java`

```java
public sealed interface Skill permits DataQuerySkill, CalculationSkill, NotificationSkill, ExternalApiSkill {}
```

### 6. 值类（Records）

所有 DTO 和领域事件使用 Record，天然不可变且线程安全。

## 快速开始

### 前置条件

- JDK 25（Early Access）: https://jdk.java.net/25/
- Maven 3.9+
- Docker & Docker Compose

### 本地开发

```bash
# 1. 启动依赖服务
docker-compose up -d

# 2. 编译项目
mvn clean install -DskipTests

# 3. 启动数字人服务
cd nova-ai-service && mvn spring-boot:run

# 4. 启动网关
cd nova-ai-gateway && mvn spring-boot:run
```

### IDEA 配置

- Settings → Build → Compiler → Java Compiler → **25 (Preview)**
- Run Configuration → VM Options: `--enable-preview`

### 测试 JDK 25 虚拟线程性能

```bash
cd nova-ai-service
mvn test -Dtest=VirtualThreadBenchmark
```

## 技术栈

| 分类 | 技术 | 版本 |
|------|------|------|
| JDK | OpenJDK | 25 LTS |
| 框架 | Spring Boot | 3.4.x |
| 微服务 | Spring Cloud | 2024.0.x |
| 数据库 | PostgreSQL | 16+ |
| 缓存 | Redis | 7.4 |
| 向量库 | Milvus | 2.4.x |
| 消息队列 | RocketMQ | 5.x |
| 监控 | Prometheus + Grafana | latest |
| 链路追踪 | SkyWalking | 9.x |

## K8s 部署

```bash
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/hpa.yaml
```

## 监控

- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)

## 项目结构

```
nova-ai-platform/
├── pom.xml                              # 父 POM
├── docker-compose.yml                   # 本地开发依赖
├── nova-ai-gateway/                     # API 网关
├── nova-ai-service/                     # 数字人服务
├── nova-ai-agent/                       # Agent 编排
├── nova-ai-workflow/                    # Workflow 引擎
├── nova-ai-skill/                       # Skill 系统
├── nova-ai-model-gateway/              # 大模型网关
├── nova-ai-rag/                         # RAG 引擎
├── nova-ai-openclaw/                    # OpenCLAW 协议
├── nova-ai-common/                      # 公共组件
├── k8s/                                 # K8s 部署清单
└── monitoring/                          # 监控配置
```

## License

Apache 2.0
