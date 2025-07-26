# WebBatchRequest Pro

<p align="center">
    <img src="https://img.shields.io/github/stars/XF-FS/WebBatchRequestpro" alt="stars">
    <img src="https://img.shields.io/github/forks/XF-FS/WebBatchRequestpro" alt="forks">
    <img src="https://img.shields.io/github/issues/XF-FS/WebBatchRequestpro" alt="issues">
    <img src="https://img.shields.io/github/license/XF-FS/WebBatchRequestpro" alt="license">
</p>

> ⚠️ 免责声明：本工具仅供安全研究和技术交流，严禁用于非法用途，否则产生的一切后果自行承担。

## 项目简介

WEB批量请求器（WebBatchRequest）是对目标地址批量进行快速的存活探测、Title获取，简单的banner识别，支持HTTP/SOCKS5代理以及可自定义HTTP请求用于批量的漏洞验证等的一款基于JAVA编写的轻量工具（暂不更新最新代码，基本上等同于重构，如果有需要可以Issues反馈）。
- 批量网站存活性探测
- 网站标题(Title)获取
- Banner识别
- HTTP代理支持
- 批量漏洞验证

本项目基于 [WebBatchRequest](https://github.com/ScriptKid-Beta/WebBatchRequest) 进行了重构和增强，修复了多个问题并新增了实用功能。

## ✨ 核心特性

### 基础功能
- [x] 支持数据的导入、导出
- [x] GET、POST、HEAD请求
- [x] HTTP代理
- [x] **SOCKS5代理支持**（新增）
- [x] 自定义Header(可Host头碰撞等)
- [x] 自定义Cookies
- [x] 自定义User-Agent
- [x] 跟随302跳转
- [x] 进度条功能
- [x] 自定义线程数
- [x] 默认浏览器打开
- [x] 列表结果排序
- [x] 增加自定义浏览器
- [x] 增加指纹扫描功能，指纹扫描基于/EHole
- [x] 增加响应时间选择
- [x] 增加重试次数选择
- [x] **网页源码搜索功能**（新增）
- [x] **颜色过滤功能**（新增）

### 新增功能详细说明

#### SOCKS5代理支持
- 在代理设置中新增代理类型选择（HTTP/SOCKS5）
- SOCKS5代理仅需提供IP地址和端口，无需密码认证
- 支持通过SOCKS5代理进行所有HTTP请求

#### 网页源码搜索功能
- 在顶部菜单栏右侧新增搜索框和搜索按钮
- 可以搜索所有表格行的网页源码内容
- 匹配的行会以红色背景高亮显示
- 支持大小写不敏感搜索
- 显示搜索结果统计信息

#### 颜色过滤功能
- 在搜索框左侧新增颜色过滤下拉框
- 支持按背景颜色过滤表格行：
  - **无**：显示所有行
  - **红色**：仅显示搜索匹配的行（红色背景）
  - **灰色**：仅显示已点击访问的行（灰色背景）
- 实时过滤，提高结果查看效率


## 🖥 界面预览

<img width="2500" height="1640" alt="PixPin_2025-07-26_13-12-36" src="https://github.com/user-attachments/assets/ae171783-80bd-42f3-831a-78bcea061084" />

## 访问标记

<img width="2500" height="1640" alt="PixPin_2025-07-26_13-15-51" src="https://github.com/user-attachments/assets/04a190d2-0a05-4364-9604-b0cc0786d1f1" />

## 内容搜索，颜色标记，颜色过滤

<img width="2500" height="1640" alt="PixPin_2025-07-26_13-16-43" src="https://github.com/user-attachments/assets/d6fdb82b-c92d-4e1a-8bdc-650dfe7ff0cd" />

## 插件扫描（预支持）

<img width="2500" height="1640" alt="PixPin_2025-07-26_13-17-44" src="https://github.com/user-attachments/assets/09080a55-9a89-4350-85d9-4acca7758254" />




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
