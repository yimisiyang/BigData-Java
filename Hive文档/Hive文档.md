# 1.Hive入门命令 #

**注意：**
（1）制表符（Tab）会自动补全Hive的关键字和函数。
（2）Hive关键字不区分大小写，类似于mysql。hive也是一种关系型数据库。
（3）可以使用HUE工具写Hive命令，优点自动补全命令、提示等。
（4）Hive创建表时，表头只能用英文。不支持中文、括号等特殊字符。

**1.创建表**
以下创建一个表名为pokes,两列foo和bar,类型分别为int和string。

    GREATE TABLE pokes(foo INT, bar STRING);

以下命令创建一个表名invites，两个普通列（foo和bar）和一个分区列（ds），分区列是一个序列，不属于数据的一部分，但是它是从一个特定的数据集加载到的分区中派生出来的。

    CREAT TABLE invites(foo int, bar string) partitioned by (ds string);

以下命令创建了一个records表，该表有三列；`ROW FORMAT DELIMITED FIELDS TERMINATED BY`子句是HiveQL所特有的，这个子句所声明的文件的每一行是由制表符分隔的文本。（当然用户也可以根据需要设置不同的行分割符）。

    CREATE TABLE records (year STRING, temperature INT, quality INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

向创建的表中导入文件时自动忽略第一行内容（通常导入表中有表头时这样创建表）。

    CREATE TABLE records (year STRING, temperature INT, quality INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' TBLPROPERTIES("skip.header.line.count" = "1");

**2.查看表**
查看所有表：

    SHOW TABLES;
查看部分表（例：表名为kes结尾的表）

    SHOW TABLES '.*kes'

**3.查看列（即描述表的结构，该命令并不显示列中的内容）**

    DESCRIBE 表名

**4.本地数据加载到Hive**
加载本地数据到hive：

    LOAD DATA LOCAL INPATH './examples/files/kv1.txt' OVERWRITE INTO TABLE pokes;

注意：
（1）本地文件系统指的是hive所在服务器的本地，通过ssh远程登录时，并不是指本地电脑。
（2）关键字`LOCAL`表示本地文件，如果不写，会从hdfs路径读取。
（3）关键字`OVERWRITE`表示覆盖之前导入的文件，如果不写则表示追加。
（4）创建数据库的表时指定的分隔符要和即将导入的文本文件的数据分隔符要一致，比如创建数据表时使用“/t”分隔符，文本文件中字段之间也是要输入“tab”键。

**5.删除数据表**

    DROP TABLE 表名;

**6.查看表中所有的数据**

    SELECT * FROM 表名；


