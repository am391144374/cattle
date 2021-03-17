# cattle

#### 介绍
简单的数据处理web平台，只需在界面配置，即可实现数据入库操作。

#### 软件架构
kettle脚本执行
springboot
mybatis-plus
mysql
webMagic

#### BUG及修复

- [x] 页面下载超时导致的数据拼接错误
- [x] 页面下载超时，并且超过重试步骤后，数据未记录，导致与正确数据和以前数据无法对比
    - [x] 目前想到的解决办法是增加重试次数和超时时间

#### 完成计划
- [ ] 完善所有功能的测试方法
    - [ ] 爬虫
    - [ ] kettle
    - [ ] 脚本执行类
    - [ ] 执行监控
- [ ] kettle脚本执行组件
    - [x] 执行kettle脚本
    - [x] 解析excel文件并导入数据库
    - [ ] 自定义导入导出数据库
    - [ ] 自定义拼接步骤执行
- [ ] 爬虫组件（基于webMagic）
    - [x] 执行自定义爬虫
    - [ ] 数据保存类别（全量、增量规则）
- [ ] redis整合，布隆过滤器（数据增量保存）
- [x] 优化通用组件执行方法，以插件形式导入
- [ ] Quartz定时任务整合
- [ ] web界面
    - [ ] 各脚本执行参数配置
    - [ ] 新增定时任务
    - [ ] 执行状态查询