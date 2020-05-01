# kettle



## 1、kettle作用

- 用于对源数据层数据进行采集，提取出大数据分析时所需要用到的有用数据。

- 一款开源ETL工具（ETL概念：对数据抽取、转换、加载。经过清洗、转换将数据弄到数据仓库中，是构建数据仓库的重要环节）

- ETL工具都是对数据进行整合的。

  **kettle作用：**

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/3d3a16423c479ce68975a77990993a2.png)

## 2、kettle安装

**下载地址（官网）：**https://sourceforge.net/projects/pentaho/files/

将下载的 `pdi-ce-9.0.0.0-423.zip`压缩包解压到安装目录，解压完成后出现 `data-integration` 文件。

在官网下载`mysal-connector-java. *-bin.jar`( 连接MySQL用的驱动) `ojdbc*.jar`（连接Oracle用的驱动），将这俩包移动到  `data-integration`目录下的`lib` 文件夹。

windows下点击`spoon.bat` Linux下点击 `spoon.sh` 运行。

## 3、创建资料库

**解释：** kettle资料库的创建要根据使用的数据库来创建（MySQL或Oracle等）

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200306112819.png)

## 4、原始数据抽取

**小测试**

 将students表数据从kettle-master数据库导到kettle-slave数据库的students表中。

**解释：**

kettle-master 所在IP：`192.168.1.100`，students表中有数据。

kettle-slave1所在IP：`192.168.1.101`，students表中无数据，表结构与kettle-master表结构相同。

**步骤：**

**第一步（若数据表存在可以忽略这一步）**

在master上的kettle-master中创建一个students表。

```
CREATE TABLE students(
id INT,
name VARCHAR(30),
sex VARCHAR(10),
address VARCHAR(40),
age INT
);

INSERT INTO students(id,name,sex,address,age) VALUES(1,'xiaoming','male','hebei',14);
INSERT INTO students(id,name,sex,address,age) VALUES(2,'xiaogang','male','hebei',13);
INSERT INTO students(id,name,sex,address,age) VALUES(3,'xiaohong','female','henan',13);
INSERT INTO students(id,name,sex,address,age) VALUES(4,'xiaoming','female','beijing',14);
```

在slave1上的kettle-slave1中创建相同的表结构，不插入数据。

```
CREATE TABLE students(
id INT,
name VARCHAR(30),
sex VARCHAR(10),
address VARCHAR(40),
age INT
);
```

**第二步：**

- 创建目标数据库连接（192.168.1.101主机）
- 创建源数据库连接（192.168.1.100主机）

**主对象树**  ---->**转换** --->**DB连接（右键：新建）** 

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200309110430.png) 

将源和目的数据苦连接创建完成后如下：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/9409d3b5217521922cd164da32a0ee9.png)

**第三步：**

在核心对象中选择表输入、表输出。并配置。

配置完成如下

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/05e1fd9f90e736fc6dd119aa883fd2e.png)

点击执行，按照步骤即可执行完成（完成图如下：）。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200309111303.png)

## 5、删除源数据库和目标数据库的连接

首先需要查看下连接所需要的依赖，只有将依赖删掉后，才能删除数据库连接。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200309111803.png)

## 6、多表抽取

目标表 slave1上的 dm_shagpin表；源表ecs_goods表、ecs_category商品类别表、ecs_brand品牌表。

**步骤：**

**第一步：**

在slave1上创建dm_shangpin表的表结构：

```
CREATE table dm_shangpin(
goods_id int,
goods_name VARCHAR(50),
cat_name VARCHAR(50),
brand_name VARCHAR(50)
);
```

在master上创建ecs_goods\ecs_category\ecs_brand 三个表，并插入数据。

创建ecs_goods表

```

```

创建ecs_category表

```
CREAT TABLE ecs_category(
cat_id int,
cat_name VARCHAR(50),

)
```

创建ecs_brand表

```

```

**第二步：**







