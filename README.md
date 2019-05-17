# 测试应用

## QuickStart
### 构建
```bash
docker build -t test-app:latest .
```

### 运行
```bash
docker run -d -p 10083:10083 test-app:latest
```

### 查看文档
```bash
curl http://localhost:10083/swagger-ui.html
```