

**hadoop-slave104 上安装了Oracle11gR2，Linux 上oracle 账户登陆密码oracle  ;system账户的密码也是oracle。**

创建了一个普通账户oracle密码是oracle,该用户的表空间位于 `ora01/app/oradata/orcl` 下，名字为oracle.dbf。



## 1.1 Oracle 的服务名（ServiceName）查询

```plsql
show parameter service_name;
```



## 1.2 以sys 用户登录Oracle 数据库

```plsql
sqlplus "sys/Oracle123 as sysdba"
```



## 1.3 Oracle 的SID查询命令

**实例名查询方式也是以下命令**

````plsql
select instance_name from v$instance;
````

## 1.4 查询Oracle版本

```plsql
select version from v$instance;
```

## 1.5 在Oracle下创建普通用户

**第1步：在windows的dos命令 登陆 oracle数据库**

sqlplus system/Oracle123@orcl

sqlplus oracle用户/orale用户的密码@数据库的实例名（如果还有疑问，请百度，这里就不赘述了）

 

![img](https://img-blog.csdnimg.cn/20190613095044398.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5OTk5NDc4,size_16,color_FFFFFF,t_70)
       

**第2步：创建用户**

```plsql
create user oracle identified by Oracle123;
```

**解释：**这里oracle是创建的用户名 ； Oracle123是密码。



**第3步：创建表空间**

```plsql
create tablespace oracle  datafile 'D:\app\Administrator\oracle.dbf' size 1000M;
```

**解释：**create tablespace oracle数据库表空间的名称  datafile 表空间的位置 size 表空间的大小；

创建表空间之前需要在windows对应的盘下创建好文件夹。默认表空间对应路径为 安装目录下`/product/11.2.0/dbhome_1/database`  或者安装时设定的`/oradata/orcl`文件夹。

**第4步：授权用户使用表空间**

```plsql
alter user oracle quota unlimited on ORACLE;
```

**解释：**alter user oracle数据库用户名  quota unlimited on  表空间名称;  

**第5步：给用户授权**

```plsql
grant connect,resource,dba to oracle;
```



## 1.6  Oracle使用命令行 导入  .dmp数据

**命令：(CMD命令提示符下输入)**

```
imp oracle/Oracle123@orcl file=E:\FTMS\FTMS_BAK_201912160018(1).dmp full=y ignore=y
```

**报错：**

```
已经完成ZHS16GBK字符集和AL16UTF16 NCHAR 字符集中的导入
。正在将SYS的对象导入到ORACLE
。正在将 JXXT2的对象导入到 JXXT2
IMP-00003：遇到ORACLE错误1435
ORA-01435：用户不存在
成功终止导入，但出现警告
```

解决方法参考：https://blog.csdn.net/hanchao5272/article/details/79818882

将上面命令改为：

```
imp oracle/Oracle123@orcl fromuser=JXXT2 touser=oracle file=E:\FTMS\FTMS_BAK_201912160018(1).dmp ignore=y
```

## 1.7 Oracle 导入数据时报以下错误

**IMP-00010：不是有效的导出文件，头部验证失败**

用Notepad++或者UltraEdit查看dmp文件，在头部具修改成你将导入目标数据库的版本号
以下对应的版本号：
　　11g R2：V11.02.00
　　11g R1：V11.01.00

　　10g：V10.02.01

**问题解决的关键就是导出时用的数据库版本号改为当前你导入数据所用的Oracle数据库版本号。**

DT_FC_FXDZ  DA_GR_NDJHSS 

143  164

## 1.8oracle 远程登陆方法及配置文件所在位置

配置文件所在位置： `/ora01/app/oracle/product/11.2.0/db_1/network/admin` 

修改admin下面的两个文件中的host 为IP本机IP地址，我这里设置的是192.168.1.104.

**远程连接方式：**

安装连接oracle 所需要的工具，使用以下命令连接：

`sqlplus username/password@IPaddress:port`

## 1.9 Oracle 删除表空间和数据的方法

- 删除空的表空间，但是不包含物理文件

```plsql
dorp tablespace tablespace_name;
```

- 删除非空表空间，但是不包含物理文件

```plsql
drop tablespace tablespace_name including contents;
```

- 删除空表空间，包含物理文件

```plsql
drop tablespace tablespace_name including datafiles;
```

- 删除非空表空间，包含物理文件

```plsql
drop tablespace tablespace_name including contents and datafiles;
```

- 如果其他表空间中的表有外键等约束关联到了本表空间中的表的字段，就要加上CASCADE CONSTRAINTS

```plsql
drop tablespace tablespace_name including contents and datafiles CASCADE CONSTRAINTS;
```







































