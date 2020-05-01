# MySQL 实现双机热备（master/slave）

## 一、准备服务器

查看主、从服务器的Mysql版本，可在服务器上输入mysql -V查看版本。主（Master）服务器的Mysql版本必须跟从（Slave）服务器的版本一样或者更低，即从（Slave）服务器的Mysql版本等于或高于主（Master）服务器的版本。

**解释：**这里我安装的MySQL版本为5.7.27，CentOS7。 主：192.168.0.143 ；备：10.10.1.164。要备份的数据库是DX。

## 二、MySQL 建立主-从服务器双机热备步骤

### 1. 获取主备服务器的IP地址

使用如下命令：

```shell
ifconfig
```



### 2.确保主从服务器上要热备的数据库数据一致

我这里数据库内容已经一致了，该步骤没有操作。若不一致可以自行百度 MySQL拷贝数据。

### 3.在主服务器上创建热备份账户

**注意：**该账户必须授予REPLICATION SLAVE权限。

**创建命令如下(主服务器上执行)：**

```mysql
mysql -u root -p ;
[Enter password]
.....
mysql>GRANT REPLICATION SLAVE ON *.* TO 'replicate'@'10.10.1.164' IDENTIFIED BY 'Mlamp1234!'; 
```

**解释：**replicate 是我创建的热备账户； 10.10.1.164 是我要备份服务器IP ；Mlamp1234！是设置的密码。

**刷新Mysql配置**

```mysql
mysql>FLUSH PRIVILEGES;
```



**创建成功后到从服务器用远程登陆方法登陆主服务器MySQL，命令如下**

```
# mysql -h192.168.0.143 -ureplicate -pMlamp1234!
```

**解释：**-h 后跟自己从服务器IP；-u后跟自己创建的热备账户；-p后跟密码。

### 4.更改主服务器的MySQL配置

**解释：**Linux下配置文件为/etc/my.cnf；windows下的配置文件为my.ini。

使用`vim /etc/my.cnf`  打开MySQL配置文件，在打开的文件内的 **[mysqld]**下添加配置参数：

```mysql
bind-address=192.168.0.143   #主服务器IP
server-id=1                  #主id，与从id不能相同
log-bin=mysql-bin            #设定生成log文件名
binlog-do-db=databasename    #要记录日志的数据库，databasename为要热备的数据库名称，如果要备份多个库，起一行再次添加此句
binlog-do-db=databasename
binlog-ignore-db=mysql       
```

**注意：**在此特别注意，配置要写在[mysqld]范围内，否则无效。

接下来重新启动数据库服务:`service mysqld restart`。 重启数据库完成后登陆数据库，输入以下命令：

```
show master status\G;                  #查看数据库Master状态
show master status;                    #两条命令都是查看master状态，可以都看一下。
```

我这里查看到的状态：

```
******************************1.row*********************************
             File: mysql-bin.000005
         Position: 4883
     Binlog_Do_DB: dx
Executed_Gtid_Set: mysql
1 row in set (0.00 sec)
```

**解释：**这里要记下File和Position的内容，后面配置从服务器时要用到。

### 5.更改从服务器的MySQL配置

和主服务器一样，同样更改  `my.cnf` 文件，增加内容如下：

```mysql
server-id=2                            #从id，与主id不能相同
log-bin=mysql-bin                      #设定生成log文件名
binlog-do-db=databasename    #主设备要热备的数据库名称，如果要备份多个库，起一行再次添加此句
binlog-do-db=databasename    #要热备的数据库名
binlog-ignore-db=mysql       #避免同步mysql用户配置    
replicate-do-db=databasename #从设备要备份的数据库名
replicate-ignore-db=mysql,information_schema,performance_sacema 
```

修改完成后，保存退出，重启MySQL服务 `service mysql restart`。重启完成后登陆MySQL数据库。

--------------------------------------------**以下是重点**-------------------------------------------------------

登陆成功数据库后输入以下命令：

```mysql
stop slave;       #停止从服务
```

然后输入：

```mysql
Change master to master_host='masterIP',master_port=masterport, master_user='username',master_password='userpassword', master_log_file='mysql-bin.000xxx',master_log_pos=position;
```

**解释：**username为创建热备的用户名，userpassword为密码；master_log_file值为你刚才在主服务器上用 `show master status;` 命令查询到的，我这里为**mysql-bin.000005**；master_log_pos的值也是刚才在主服务器上用`show master status;` 命令查询到的，我这里为4883。

最后输入：

```mysql
start slave;     #启动从服务
```



查看从服务状态：

```mysql
show slave status\G;  
#或者
show slave status;
```



在查询到的状态中若以下两个字段为YES，证明热备份设置已经成功了。

```
Slave_IO_Running:YES
Slave_SQL_Runing:YES
```

## 三、测试

 在主服务器上热备的数据库内创建个新表，然后去查看从服务器上的热备数据库是否也创建了新表。同样可以测试在表中增加一条数据，看从服务器的表里是否有增加的数据。



## **参考文档**

https://minervadb.com/index.php/2018/01/24/step-by-step-mysql-master-slave-replication-on-centos/

https://www.cnblogs.com/wintercloud/p/10620232.html

https://blog.csdn.net/qq_23676873/article/details/79668819















