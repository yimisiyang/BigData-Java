# 一、大数据环境 #

## 1.准备工作：##


**1. 安装路径：**

- 安装的是hdp版本的hadoop集群。hdp版本是`2.4.2.0-258`.安装目录为`/usr/hdp`
- hadoop安装目录为`/hadoop`
- `/home`

**2.集群IP分配**

```
master IP: 172.16.70.100
slave1 IP: 172.16.70.101  
slave2 IP: 172.16.70.102
slave3 IP: 172.16.70.103
slave4 IP: 172.16.70.104
slave5 IP: 172.16.70.105
```



**3.mysql远程登录查询**

MySQL安装在slave1上。  注意：这里数据库没有设置密码。

使用`mysql -u root` 可以直接登录。

**遇到问题**

在slave1上通过命令可以登录mysql数据库。通过其它服务器远程访问slave1上的数据库无法连接。
于是尝试通过ping命令ping slave1和其它服务器，发现之间均无法正常ping通。但是集群明明已经正常启动。 ---------------问题以解决。

**问题原因**

ping 时把 子节点的ip地址给写错了------------------粗心引起的。

**4.修改slave1上mysql root 用户的密码（修改后的密码为‘user’）**


命令：

```
#在slave1上免密登录mysql,登录用户为root

# mysql -u root

#修改slave1上mysql的登录密码

mysql> mysql set password for root@localhost=password（'user'）
```
参考文档：

[https://blog.csdn.net/qq_31220649/article/details/89468706](https://blog.csdn.net/qq_31220649/article/details/89468706)

**5.在master上登录slave1上的mysql**

命令：

```
mysql -h 172.16.70.101 -u root -p 
ENTER PASSWOED: user
```
出现问题1：

由于slave1上的mysql 并没有开启远程登录功能 所以无法远程连接。

解决方法：

```
#在slave1上本地登录mysql。

mysql -u root -p

#授权（给用户root密码为‘user’的账户授权所有电脑都可以连接的权限）

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'user' WITH GRANT OPTION;

#命令立即生效

flush privileges;

#退出mysql

exit

#重启mysql服务（在shell中执行）

service mysql restart
```

参考文档：

授权
[https://blog.csdn.net/songpeiying/article/details/86028347](https://blog.csdn.net/songpeiying/article/details/86028347)

mysql服务重启：

[https://www.cnblogs.com/angelye/p/8560832.html](https://www.cnblogs.com/angelye/p/8560832.html)

出现问题2：

```
Warning: World-writable config file '/etc/my.cnf' is ignored
```

问题原因：

` /etc/my.cnf`这个文件的权限太高（777），任何一个用户都可以写。mysql担心这种文件被其他用户恶意修改，所以忽略掉这个配置文件。

解决方法：

```
#在master上定位到/etc/my.cnf,查看权限为777

ls -l /etc/my.cnf

#修改/etc/my.cnf的权限为644

chmod 644 /etc/my.cnf

```

## 2.sqoop命令测试 ##

**1.在数据库上创建表**

```
#在slave1上登录本地mysql数据库

mysql -u root -p

#创建空的数据库

create database students;

#使用students数据库

use students;

#设置数据库编码

set names utf8;

#创建数据库表

CREATE TABLE students_message(name VARCHAR(20), sex VARCHAR(20),age VARCHAR(20), address VARCHAR(20));

```
**2.在本地/home/cxk/下创建students_message.sql表**

**3.将/home/cxk/下的students_message.sql导入数据库**

    LOAD DATA LOCAL INFILE '/home/cxk/students_message.sql' INTO TABLE students_message;

**4.查看是否导入成功**

    select * from students_message;

**5.在主节点上使用sqoop工具创建MapReduce任务将students_message表导入到HDFS**

```
sqoop import --connect jdbc:mysql://172.16.70.101/students --table students_message --username root -p
```
提示错误：

```
Imported failed:No privmary key could be found for table students_message.Please specify one with --split-by or perform a sequential import with '-m 1'

```
根据提示修改命令如下：

```
sqoop import --connect jdbc:mysql://172.16.70.101/students --table students_message -m 1 --username root -p
```
问题提示.

```
INFO orm.CompilationManager:HADOOP_MAPRED_HOME is /usr/hdp/2.4.2.0-258/hadoop-mapreduce
注：/tmp/sqoop-root/compile/ac04cc0afae7cfe4e75c70c66497ac44/students_message.java使用或覆盖了已过时的API。
注：有关详细信息，请使用-Xlint：deprecation重新编译。
```
问题原因：

在/etc/profile和sqoop安装目录下的./conf/sqoop-env.sh中HADOOP_MAPRED_HOME指定目录出错了。应该指定到`/usr/hdp/2.4.2.0-258/hadoop-mapreduce`

指定完成后重新让/etc/profile生效。

    source /etc/porfile

---------------根据问题提示修改后问题仍没有解决------------------------


**6.查看hdfs上的内容**

hdfs命令基本使用：

[https://blog.csdn.net/afafawfaf/article/details/80254989](https://blog.csdn.net/afafawfaf/article/details/80254989)

文件上传默认目录：

```
su hdfs

hadoop fs -ls /user/root
```

删除上传文件：

```
hadoop fs -rm -R /user/root/students_message
```

**7.将数据导入到hive**

命令

```
sqoop import --connect jdbc:mysql://172.16.70.101/students --table students_message --hive-import -m 1 --username root -p
```
出现问题1：

由于当时在配置sqoop时，在sqoop安装目录下的./conf/sqoop-env.sh中只配置了`HADOOP_COMMON_HOME`和`HADOOP_MAPRED_HOME` 这两个，并没有配置HBASE_HOME和HIVE_HOME。因此在导入数据时会报错。

同时在`/etc/profile`环境变量配置中`HIVE_HOME`和`HBASE_HOME`,增加了各自的`PASH`。使用`source /etc/profile` 使环境变量生效。

出现问题2：

```
ERROR hive.HiveConfig: Could not load org.apache.hadoop.hive.conf.HiveConf.Make sure HIVE_CONF_DIR is set correctly.
```

问题原因：

`/etc/profile`文件中没有配置`HIVE_CONF_DIR`这个环境变量 。

解决方法：

打开`vim /etc/profile` 添加`export HADOOP_CLASSPATH=$/usr/hdp/2.4.2.0-258/hadoop/lib`和`export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:$HIVE_HOME/lib/*`以及`HIVE_CONF_DIR=$HIVE_HOME/conf`。这里HIVE_HOME为：`$HIVE_HOME=/usr/hdp/2.4.2.0-258/hive`。

参考文档：

[https://blog.csdn.net/weixin_41018467/article/details/87105303](https://blog.csdn.net/weixin_41018467/article/details/87105303)

[https://blog.csdn.net/qq_26840065/article/details/51498934](https://blog.csdn.net/qq_26840065/article/details/51498934)

再次执行上述命令，表格成功导入到了hive。

查看导入到hive中的数据表。

执行命令

```
进入hive
# hive

显示hive中所有表
hive> show tables;

查看刚才上传的students_message的内容
hive>select * from students_message

```

**8.将数据导入到hbase**

命令：

```
sqoop import --connect jdbc:mysql://172.16.70.101/students --table students_message --hbase-create-table -m 1 --username root -P
```
显示导入数据成功。这里`--hbase-create-table`代表的是若HBASE中没有表则自动创建。

查看导入到HBASE中的数据。

表存的位置和HDFS导入存的位置一样用`hadoop fs -ls /user/root`查看。

参考文档：

[https://blog.csdn.net/qq_38256924/article/details/79850369](https://blog.csdn.net/qq_38256924/article/details/79850369)

## 问题汇总： ##

## 问题1：APP Timeline Server无法启动 ##

**查看日志：**

```
"RemoteException": {
"exception": "SaveMOdeException",
"javaClassName":"org.apache.hadoop.hdfs.server.namenode.SafeModeException"
"message":"Cannot set permission for /ats/done. Name node is in safe mode.
The reported blocks 835 needs addtional 2 blocks to reach the threshold 1.0000 of total blocks 836.
The number of live datanodes 6 has reached the minimum number 0. Safe mode will be turned off automatically once the thresholds have been reached."
}
```

**问题原因：**

HDFS在安全模式下，只能查看文件不能写文件。

参考文档：
[https://blog.csdn.net/weixin_33800463/article/details/92370608](https://blog.csdn.net/weixin_33800463/article/details/92370608)

[http://bbs.csdn.net/topics/390657293](http://bbs.csdn.net/topics/390657293)

**解决方法：**

**1.切成hdfs用户。**

    `su hdfs`

**2.查看安全模式状态：**

```
hdfs dfsadmin -safemode get 查看安全模式状态
hdfs dfsadmin -safemode enter 进入安全模式
hdfs dfsadmin -safemode leave 退出安全模式
hdfs dfsadmin -safemode wait  等待退出安全模式
```

**3.若无法退出安全模式则原因可能**

1、磁盘写满；

2、HDFS里面的备份块丢失过多

解决方法参考：

[http://bbs.csdn.net/topics/390657293](http://bbs.csdn.net/topics/390657293)

[http://www.360doc.com/content/11/1201/09/3294720_168811892.shtml](http://www.360doc.com/content/11/1201/09/3294720_168811892.shtml)

**4.在Ambari再次重启App Timeline Server**

重启成功。

# 问题2：安装sqoop后出现的问题 #

参考安装文档：

[https://github.com/heibaiying/BigData-Notes/blob/master/notes/Sqoop%E7%AE%80%E4%BB%8B%E4%B8%8E%E5%AE%89%E8%A3%85.md](https://github.com/heibaiying/BigData-Notes/blob/master/notes/Sqoop%E7%AE%80%E4%BB%8B%E4%B8%8E%E5%AE%89%E8%A3%85.md)

1.刚开始安装的是sqoop-1.4.7.tar.gz，在配置时发现conf文件下没有`sqoop-env-template.sh`,于是改下载`sqoop-1.4.7.bin_hadoop-2.6.0.tar.gz`

2.在`/etc/profile`中设置了hadoop环境变量，以及`HADOOP_COMMON_HOME`和`HADOOP_MAPRED_HOME`

3.在sqoop安装目录下复制`cp sqoop-env-template.sh sqoop-env.sh` 使用`vim sqoop-env.sh`打开文件，在里面设置了`HADOOP_COMMON_HOME`和`HADOOP_MAPRED_HOME`

4.在测试`sqoop-version`时出现以下问题。

```
Warning：/opt/modules/sqoop-1.4.7.bin_hadoop-2.6.0/../hcatalog does not exist!HCatalog job will fail.Please set $HCAT_HOME to the root of your HCatalog installation.
Warning：/opt/modules/sqoop-1.4.7.bin_hadoop-2.6.0/../accumulo does not exist!Accumulo imports will fail.Please set $ACCUMULO_HOME to the root of your Accumulo installation.

```
解决方法：

[http://blog.csdn.net/jmx_bigdata/article/details/98752357](http://blog.csdn.net/jmx_bigdata/article/details/98752357)

## 文件从windows导入虚拟机出现错误 ##

为什么从windows导入到虚拟机时出错。导致sqoop包有问题，折腾了半天才发现。正确导入包后问题解决。该集群中安装的是`sqoop-1.4.7`


# 问题3：各个虚拟机之间无法ping通 #

**可能原因排查**

1.检查防火墙状态（centos防火墙查看参考链接，不是传统的iptables服务。）

[https://www.jb51.net/article/101576.htm](https://www.jb51.net/article/101576.htm)


2.VMWare 4中网络连接方式（桥接模式、NAT模式、仅主机模式、自定义模式）

[https://www.cnblogs.com/xuliangxing/p/7027124.html](https://www.cnblogs.com/xuliangxing/p/7027124.html)

3.查看物理主机的网络地址：

    `ipconfig`

```
#以太网适配器 以太网：

IPv4 地址 ........................172.16.70.14

#以太网适配器 VMware Network Adapter VMnet1:

IPv4 地址.........................192.168.108.1

#以太网适配器 VMware Network Adapter VMnet8：

IPv4 地址.........................172.16.70.1
```

4.查看物理主机的网络适配器发现只有VMnet1(仅主机模式)、VMnet8（NAT模式），并没有VMnet0（桥接模式）。而虚拟机中设置的是桥接模式，无法ping通的原因可能在这里。

原因：看成了VMnet8的IP。

# 问题4：Heartbeat Lost（由于某天掉电产生） #

解决方法：重启ambari agent 客户端。

    ambari-agent restart

问题原因：主节点的网络连接方式设置成了Nat模式，忘了修改回来。其它节点的网络连接方式均为桥接。

# 问题5：集群HDFS的NameNode Blocks Health问题 #

问题描述：

```
Total Blocks:[937],Missing Blocks:[1]
```

解决方法：

```
切换到 hdfs 用户。
su hdfs

使用hadoop fsck命令检查坏的块

hadoop fsck /

清除损坏的块。（在查到的内容中寻找）

hadoop fsck -delete 块内容

再次使用hadoop fsck查看是否仍有损坏的块
hadoop fsck /

```
此时在Ambari上查看master主节点上丢失块的警告已经消失了。

参考文档：

[https://blog.csdn.net/Post_Yuan/article/details/103489879](https://blog.csdn.net/Post_Yuan/article/details/103489879)

