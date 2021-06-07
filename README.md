cattle

#### 介绍
简单的数据处理web平台，只需在界面配置，即可实现数据入库操作。
目前支持的组件有：
1. 爬虫
2. kettle脚本

#### 软件架构

##### 后端
kettle
springboot
mybatis-plus
mysql
webMagic
redis

##### 前端
pear admin（layui）

#### 执行环境配置

##### 环境要求
1. kettle执行脚本 需要把kettle下的plugins文件夹下的内容都拷贝下来，否则无法执行kettle脚本，在KettlePluginInit中配置地址
2. JDK 1.8+

##### 项目描述

- kettle

  - 进入如下

    ![kettle脚本](doc/image/kettle脚本配置.png)

  - 第一层为新增kettle脚本，如果当前执行模式是编辑模式，则脚本名能点击设置自定义excel文件参数

    ![](doc/image/kettle自定义excel.png)

  - 在这个kettle下创建的步骤都是单独的步骤，且都是按照上层的脚本信息来执行。

  - 点击步骤名来设置自定义字段。

    注意事项：

    1. 数据库表名在脚本中设置的默认替换值为<b>```` tableYearTemplate ````</b>  当前默认脚本的处理规则为，以excel名字为替换值。

- spider

  - 进入如下：

    ![](doc/image/爬虫脚本配置.png)

    - 爬虫脚本在新增或者修改时能页面调试

      ![](doc/image/爬虫配置图.png)

    - 爬虫需要执行会根据入口页和列表页的数据自动扫描添加下一步的扫描url
      - 目前支持数据增量更新
        - 判断规则：
          1. 如果有正文页需要下载，则会先过滤正文页的url（url过滤只过滤正文页url，不过滤列表页）
          2. 所有需要爬取的字段数据拼接，hash算出4个值，redis bitmap过滤
          3. 如果有字段的查询更新填”是“，则会在前两次的基础上，再根据字段的值去数据库中查询来更新或者插入

#### 完成计划
- [ ] 完善所有功能的测试方法
    - [x] 爬虫
    - [x] kettle
    - [x] 脚本执行类
    - [ ] 执行监控
- [ ] kettle脚本执行组件
    - [x] 执行kettle脚本
    - [x] 解析excel文件并导入数据库
    - [ ] 自定义导入导出数据库
    - [ ] 自定义拼接步骤执行
- [x] 爬虫组件（基于webMagic）
    - [x] 执行自定义爬虫
    - [x] 数据保存类别（全量、增量规则）
- [x] redis整合，布隆过滤器（数据增量保存）
- [x] 优化通用组件执行方法，以插件形式导入
- [ ] Quartz定时任务整合
- [ ] web界面
    - [x] 各脚本执行参数配置
        - [x] kettle
        - [x] spider
    - [ ] 新增定时任务
    - [x] 执行状态查询