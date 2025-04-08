# WebBatchRequest Pro

<p align="center">
    <img src="https://img.shields.io/github/stars/XF-FS/WebBatchRequestpro" alt="stars">
    <img src="https://img.shields.io/github/forks/XF-FS/WebBatchRequestpro" alt="forks">
    <img src="https://img.shields.io/github/issues/XF-FS/WebBatchRequestpro" alt="issues">
    <img src="https://img.shields.io/github/license/XF-FS/WebBatchRequestpro" alt="license">
</p>

> ⚠️ 免责声明：本工具仅供安全研究和技术交流，严禁用于非法用途，否则产生的一切后果自行承担。

## 项目简介

WebBatchRequest Pro 是一款基于Java开发的轻量级Web请求工具，主要用于:
- 批量网站存活性探测
- 网站标题(Title)获取
- Banner识别
- HTTP代理支持
- 批量漏洞验证

本项目基于 [WebBatchRequest](https://github.com/ScriptKid-Beta/WebBatchRequest) 进行了重构和增强，修复了多个问题并新增了实用功能。

## ✨ 核心特性

### 基础功能
- [x] 多种请求方式(GET/POST/HEAD)
- [x] HTTP代理支持
- [x] 自定义请求头(支持Host碰撞)
- [x] Cookie自定义
- [x] User-Agent自定义
- [x] 重定向(301/302/303/307/308)处理
- [x] 多线程并发请求
- [x] 数据导入导出

### 增强功能
- [x] 现代化UI界面重构
- [x] 自定义浏览器打开链接
- [x] 智能结果排序
- [x] 基于EHole的指纹识别
- [x] 灵活的响应时间设置
- [x] 请求重试机制
- [x] 访问记录标记
- [x] 空节点智能清理

### 问题修复
- [x] 修复内容长度排序异常
- [x] 优化响应时间计算
- [x] 解决线程阻塞问题

## 🖥 界面预览

<details>
<summary>点击查看截图</summary>

![主界面](https://github.com/user-attachments/assets/1378acfc-880a-4f9a-8f4a-38399a10b94b)
![功能展示](https://github.com/user-attachments/assets/12305540-2ede-4505-8a45-bc1a9ed8b32a)
![结果展示](https://github.com/user-attachments/assets/ff027657-006f-4a3e-9c0c-be52040bc230)

</details>

## 📦 配置说明

配置文件 `config.properties`:
```properties
browserPath=自定义浏览器路径
eholePath=EHole工具路径
```

## 🚀 即将推出
- [ ] 插件系统
- [ ] 更多指纹库支持
- [ ] 自定义扫描规则
- [ ] 报告导出功能

## 🤝 贡献

欢迎提交 Issue 和 Pull Request。如果您有任何建议或发现了bug，请在[Issues](https://github.com/XF-FS/WebBatchRequestpro/issues)中提出。

## 📄 致谢

- [WebBatchRequest](https://github.com/ScriptKid-Beta/WebBatchRequest) - 原始项目
- [EHole](https://github.com/EdgeSecurityTeam/EHole) - 指纹识别支持

## 📝 许可证


## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=XF-FS/WebBatchRequestpro&type=Date)](https://star-history.com/?utm_source=bestxtools.com#XF-FS/WebBatchRequestpro&Date)

> ⚠️ 免责声明：本工具仅供安全研究和技术交流，严禁用于非法用途，否则产生的一切后果自行承担。
