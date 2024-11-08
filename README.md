本工具仅限技术研究与测试，严禁用于非法用途，否则产生的一切后果自行承担。

#### 介绍

WEB批量请求器（WebBatchRequest）是对目标地址批量进行快速的存活探测、Title获取，简单的banner识别，支持HTTP代理以及可自定义HTTP请求用于批量的漏洞验证等的一款基于JAVA编写的轻量工具。

基于 https://github.com/ScriptKid-Beta/WebBatchRequest 修改而来,修复排序BUG，增加一些好用功能。
config.properties 文件是自定义浏览器，和自定义EHole的配置文件

##### 支持功能

- [x] 支持数据的导入、导出
- [x] GET、POST、HEAD请求
- [x] HTTP代理
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

#### 效果
<img width="1293" alt="image" src="https://github.com/user-attachments/assets/12305540-2ede-4505-8a45-bc1a9ed8b32a">

<img width="1332" alt="image" src="https://github.com/user-attachments/assets/ff027657-006f-4a3e-9c0c-be52040bc230">




#### 最后

欢迎师傅star，最重要的是如果师傅们有什么建议或者Bug，请在ISSUES里提出来~



#### 感谢

```
https://github.com/ScriptKid-Beta/WebBatchRequest
https://github.com/EdgeSecurityTeam/EHole
```

