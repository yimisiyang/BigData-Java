# 一、HBase简介 #
HBase是一个在HDFS上开发的面向列的分布式数据库，适合用来做需要实时地随机访问超大规模数据集。HBase不是关系型数据库，不支持SQL。HBase依赖于Zookeeper。

# 二、HBase常用命令 #
**1.启动HBase的shell环境。**

    hbase shell

**2.启动一个使用本地文件系统/tmp目录作为持久化存储的HBase实例**

    start-hbase.sh
默认情况下，HBase会被写入到`/${java.io.tmpdir}/hbase-${user.name}`中，`${java.io.tmpdir}`通常被映射为/tmp。通过设置`hbase-site.xml`中的`hbase.tmp.dir`来对HBase进行配置。

**3.创建一个hbase表。**
创建一个名为test的表，使其只包含一个名为data的列，表和列的属性都为默认值。

    create 'test', 'data'

hbase默认日志目录是`${HBASEHOME}/logs`当出现问题时，可以查看日志进行追踪。

查看列表是否创建成功

    list

在hbase shell中用命令往上面创建的表里插入数据

```
put 'test','row1','data:1','value1'
put 'test','row2','data:2','value2'
put 'test','row3','data:3','value3'
//查看第一行数据
get 'test','row1'    
//查看所有数据
scan 'test'  
```

**4.移除刚才创建的test表**
为了删除这个表，首先要把它设置为禁用，然后删除（在HBase shell里做如下命令）：

```
disable 'test'
drop 'test'
list  //查看表是否删除成功。
```
**5.通过以下命令关闭HBase实例：**

    stop-hbase.sh



