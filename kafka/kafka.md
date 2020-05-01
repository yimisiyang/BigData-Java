# 一、准备工作 #

1.安装jdk     版本1.8
2.安装Scala   最新版本Scala2.11   课中使用0.10.1.0
3.安装IDEA工具和Scala插件
4.安装gradle  版本gradle-3.1-bin.zip  配置环境变量
由于kafka的源码采用没有采用maven管理，而是使用的gradle，类似于maven，下载安装需要的依赖jar包。

后期两条线  写生产者代码（将数据写入kafka集群内部）或消费者代码（从kafka中将数据取出使用）。

# 二、kafka的基础架构 #

图片

# 三、kafka源码 #

**1.生产者源码（重点）**

producer写数据核心流程。

参考源码中的producer.java代码。在example中。


**2.kafka集群源码**



**3.消费者源码**