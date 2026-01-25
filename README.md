# 期刊在线采编系统

一个基于 Spring Boot 的期刊投稿与采编工作流系统。

## 版本

- Java：JDK 25（LTS）
- Spring Boot：4.0.1
- 数据库：MySQL 9.5

## 功能特性

- 角色与权限
    - 统一用户表，支持作者/审稿专家/编辑角色，基于 JWT 做认证与授权
    - 作者可升级为审稿专家，接口权限随角色变化
- 稿件工作流
    - 支持 提交 → 初审 → 同行评审 → 修订 → ... -> 最终决定 → 出版 等多阶段流程
    - 流程节点及状态可追踪，支持任务查询（作者待修订、审稿人待评审、编辑待处理）
- 稿件与附件
    - 多附件上传与下载，白名单与大小限制可配置
    - 统一作为附件持久化，上传失败与越权访问均有明确错误响应
- 接口与文档
    - OpenAPI/Knife4j 文档，清晰示例展示
    - MapStruct 映射 DTO/VO，PATCH 忽略空值避免误覆盖
- 安全与异常
    - JWT 校验与签名错误处理，统一业务异常结构（ApiResponse）
    - 基于角色的接口访问控制；文件类型与大小二次校验
- 工程与运维
    - 可执行 jar 打包；配置项集中在 application.yml 并可外部化
    - 存储目录与限制项可按环境调整

## 技术栈

- 后端框架：Spring Boot（WebMVC、Security、Data JPA）
- 安全与认证：Spring Security + JWT
- 数据访问：Hibernate/JPA，MySQL 驱动
- 接口文档：springdoc-openapi + Knife4j（OpenAPI 3）
- 对象映射：MapStruct（DTO/VO 与实体转换）
- 构建工具：Maven（spring-boot-maven-plugin 可执行打包）

## 配置

编辑 `src/main/resources/application.yml`：

- `spring.datasource.*`：数据库连接
- `jwt.secret`：提供强度足够的密钥（生产环境使用外部化配置或环境变量）
- `storage.*`：
    - `upload-dir`：默认 `uploads`
    - `max-size-bytes`：默认 10485760（10MB）
    - `allowed-types`：例如 `application/pdf,image/png,image/jpeg,text/plain`
- `springdoc`/`knife4j`：已配置好 OpenAPI UI

## 构建与运行

- 构建：

```bash
mvn clean package
```

- 运行：

```bash
java -jar target/journal-editorial-system-0.1.0.jar
```

- 或者：

```bash
mvn spring-boot:run
```

## 接口文档

- Knife4j UI：`http://localhost:8080/doc.html`

## 许可证

Apache License 2.0

## 变更日志

- 0.1.0：首个预览版，实现稿件采编系统核心功能
- 0.1.1：修复了部分接口的逻辑和返回视图
