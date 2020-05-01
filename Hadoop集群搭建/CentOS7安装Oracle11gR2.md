# 1.安装前准备（本文采用无图形化界面的静默安装方式）#

**软件要求：**

CentOS7_64位系统（这里用的是7.6-1810版本。）

Oracle数据库（版本11.2.0.4.0）

安装包位置：`G:\CentOS_Share\oracle`

**本文采用无图形化界面的静默安装方式**

# 2.准备工作 #

## 1.关闭selinux ##

查看`selinux`状态：` getenforce` 或者 `sestatus -v`

临时关闭：`setenforce 0`

永久关闭：`vim /etc/selinux/config` 设置`SELINUX=disabled`

**注意：**为了防止重启后忘记关闭，这里设置的是永久关闭`selinux`。

![](https://i.imgur.com/uK5GZmq.png)

## 2.关闭firewalld防火墙 ##

    systemctl status firewalld

## 3.创建用户组和用户（使用root用户）##

```
groupadd -g 501 oinstall
groupadd -g 502 dba
groupadd -g 503 oper
```

![](https://i.imgur.com/Pi0KQdc.png)

## 4.创建用户 ##

    useradd -u 502 -g oinstall -G dba,oper oracle

![](https://i.imgur.com/ZXRAYJi.png)

更改上面创建的oracle用户密码（这里设置成oracle）

    passwd oracle

## 5.创建安装oracle所需要的文件夹 ##

Create directory structure

```
mkdir -p /ora01/app
chown oracle:oinstall /ora01/app
chmod 775 /ora01/app
```

create ORACLE_BASE directory for oracle

```
mkdir -p /ora01/app/oracle
chown oracle:oinstall /ora01/app/oracle
chmod 775 /ora01/app/oracle
```

Create ORACLE_HOME directory for oracle

```
mkdir -p /ora01/app/oracle/product/11.2.0/db_1
chown oracle:oinstall -R /ora01/app/oracle
```
Create oradata directory for oracle

```
mkdir -p /ora01/app/oraInventory
chown oracle:oinstall /ora01/app/oraInventory
chmod 775 /ora01/app/oraInventory
```

create oradata directory for oracle

```
mkdir -p /ora01/app/oradata
chown oracle:oinstall /ora01/app/oradata
chmod 775 /ora01/app/oradata
```

create flash_recovery_area directory for oracle

```
mkdir -p /ora01/app/flash_recovery_area
chown oracle:oinstall /ora01/app/flash_recovery_area
chmod 775 /ora01/app/flash_recovery_area
```

**下面解释各个文件夹的作用**

我安装时创建的文件夹（这里要注意这些文件夹所属用户和用户组）

**切记不要只在root下创建而不修改这些文件夹的权限和所属用户**

![](https://i.imgur.com/g5wsw4K.png)

`database`：下载的oracle安装包（11gR2版本），解压完成后会生成database文件夹。

`flash_recovery_area`:先创建好后面配置`/data/src/database/response/dbca.rsp`时要使用该文件夹。

`oraInventory`：在静默安装过程中（即无图形界面），用来存放log以及其它一些文件。若使用图形化界面安装无需创建，会自动生成该文件夹。

`oracle`：ORACLE_BASE目录，即oracle安装基目录。

`oradata`：和`flash_recovery_area`类似,同样添加数据库实例时要用到，需要在配置dbca.rsp时使用。

## 6.修改配置文件 ##

### 1.修改/etc/sysctl.conf文件(root用户) ###

    vim /etc/sysctl.conf

修改代码如下（这里虚拟机是2G内存）：

```
kernel.shmmni = 4096
kernel.shmmax = 2147483647
kernel.shmall = 524288
kernel.sem = 250 32000 100 128

fs.aio-max-nr = 1048576
fs.file-max = 6815744
net.ipv4.ip_local_port_range = 9000 65500
net.core.rmem_default = 262144
net.core.rmem_max = 4194303
net.core.wmem_default = 262144
net.core.wmem_max = 1048586

```
修改后的截图：

![](https://i.imgur.com/3S5I1lh.png)

使修改生效：

    /sbin/sysctl -p

改该配置参考文档（物理内存不同可能配置不太一样，仅供参考。）

[https://www.cnops.xyz/archives/1156](https://www.cnops.xyz/archives/1156)

[http://dbaora.com/install-oracle-11g-release-2-11-2-on-centos-linux-7/](http://dbaora.com/install-oracle-11g-release-2-11-2-on-centos-linux-7/)

### 2.在`/etc/security/limits.conf`添加以下代码为oracle用户设置shell限制 （root用户）###

修改内容：

```
racle   soft   nproc    131072
oracle   hard   nproc    131072
oracle   soft   nofile   131072
oracle   hard   nofile   131072
oracle   soft   core     unlimited
oracle   hard   core     unlimited
oracle   soft   memlock  50000000
oracle   hard   memlock  50000000

```

![](https://i.imgur.com/rTAgPIu.png)

### 3.将服务器名写入到host文件（/etc/hosts）(root用户) ###

修改格式：

    <IP-address>  <fully-qualified-machine-name>  <machine-name>

举例：

    127.0.0.1 myhost localhost localhost.localdomain

![](https://i.imgur.com/7r1b2YJ.png)

测试ping是否返回127.0.0.1

    ping -c 3 myhost

![](https://i.imgur.com/Voln4Ra.png)

### 4.设置环境变量(在`/etc/profile`文件末尾添加以下内容)(root用户) ###

```
#oracle
export ORACLE_HOME=/ora01/app/oracle/product/11.2.0/db_1
export ORACLE_SID=orcl
if [ $USER = "oracle" ]; then
if [ $SHELL = "/bin/ksh" ]; then
ulimit -p 16384
ulimit -n 65536
else
ulimit -u 16384 -n 65536
fi
fi

```

![](https://i.imgur.com/F4Gx9JY.png)


**使修改生效**

    source /etc/profile

### 5.修改oracle用户的环境变量（oracle用户修改即可） ###

修改文件在（/home/oracle）目录下。使用`ls -la`命令查看该目录下所有文件。主要是`.bashrc`和`.bash_profile`文件的修改。有的文件只修改了其中一个，我这里两个都改了。具体修改内容如下（其实两个文件修改的内容一样，若想深究，可以研究下这两个文件的区别）。

**.bashrc的修改**

```
export TMP=/tmp
export ORACLE_BASE=/ora01/app/oracle
export ORACLE_HOME=$ORACLE_BASE/product/11.2.0/db_1
export ORACLE_SID=orcl
export ORACLE_UNQNAME=orcl
export PATH=$ORACLE_HOME/bin:/usr/sbin:$PATH
export LD_LIBRARY_PATH=$ORACLE_HOME/lib:/lib:/usr/lib
export LANG=C
export NLS_LANG=AMERICAN_AMERICA.AL32UTF8

```

![](https://i.imgur.com/A5x57Xm.png)

**使修改生效**

    source /home/oracle/.bashrc

**.bash_profile的修改**

```
export TMP=/tmp
export ORACLE_BASE=/ora01/app/oracle
export ORACLE_HOME=$ORACLE_BASE/product/11.2.0/db_1
export ORACLE_SID=orcl
export ORACLE_UNQNAME=orcl
export PATH=$ORACLE_HOME/bin:/usr/sbin:$PATH
export LD_LIBRARY_PATH=$ORACLE_HOME/lib:/lib:/usr/lib
export LANG=C
export NLS_LANG=AMERICAN_AMERICA.AL32UTF8

```

![](https://i.imgur.com/bYWyQ39.png)

**使修改生效**

    source /home/oracle/.bash_profile

### 6.修改CentOS系统标识（据参考文档说Oracle默认不支持CentOS） ###

个人感觉可能没有必要，但为了保险起见，我还是更改了系统标识（使用root用户）

修改文件`/etc/redhat-release`

将内容替换为：`redhat-7`

执行以下命令`mv /etc/redhat-release redhat-7`

### 7.检查安装oracle缺少的依赖 ###

```
rpm -q --qf '%{NAME}-%{VERSION}-%{RELEASE}(%{ARCH})\n' binutils \
elfutils-libelf \
elfutils-libelf-devel \
gcc \
gcc-c++ \
glibc \
glibc-common \
glibc-devel \
glibc-headers \
ksh \
libaio \
libaio-devel \
libgcc \
libstdc++ \
libstdc++-devel \
make \
sysstat \
unixODBC \
unixODBC-devel
```

检查结果如下：

![](https://i.imgur.com/4hqxUkX.png)

安装缺少的依赖包(可联网情况)：

    yum -y install 包名

### 8.基础准备工作做完后重启计算机，为安装oracle做准备。 ###

若上面所有配置均已source即以生效，可以忽略重启。直接安装。

    reboot

# 3.oracle安装，这里选择的静默安装 #

（采用图形化界面安装的需要安装图形化界面（对中文不友好），具体方式自行百度）

**注意安装时使用的一定是oracle用户，并非root用户**
前提已将解压好的安装包放在了/ora01/app目录下

## 1.编辑数据库安装文件：/ora01/app/database/response/db_install.rsp ##

    vim /ora01/app/database/response/db_install.rsp

```
oracle.install.option=INSTALL_DB_SWONLY
ORACLE_HOSTNAME=oracle.server
UNIX_GROUP_NAME=oinstall
INVENTORY_LOCATION=/ora01/app/oraInventory
SELECTED_LANGUAGES=en,zh_CN
ORACLE_HOME=/ora01/app/oracle/product/11.2.0/db_1
ORACLE_BASE=/ora01/app/oracle
oracle.install.db.InstallEdition=EE
oracle.install.db.DBA_GROUP=dba
oracle.install.db.OPER_GROUP=oinstall
oracle.install.db.config.starterdb.type=GENERAL_PURPOSE
oracle.install.db.config.starterdb.globalDBName=oral
oracle.install.db.config.starterdb.SID=oral
oracle.install.db.config.starterdb.characterSet=AL32UTF8
oracle.install.db.config.starterdb.memoryLimit=800
oracle.install.db.config.starterdb.password.ALL=oracle
DECLINE_SECURITY_UPDATES=true
```
**以上配置小白可以直接对着自己的文件更改，若想研究各参数含义参考以下文档**

[https://blog.51cto.com/loofeer/1119713](https://blog.51cto.com/loofeer/1119713)

设置完成后执行以下命令

    /ora01/app/database/runInstaller -silent -responseFile /ora01/app/database/response/db_install.rsp -ignorePrereq

-------------------漫长的等待-----------------------------

由于当时没有截图，借用网上的图片。

![](https://i.imgur.com/lkpeMk2.png)

根据上图的提示使用root用户再打开一个终端执行以下脚本

```
/ora01/app/oraInventory/orainstRoot.sh
/ora01/app/oracle/product/11.2.0/db_1/root.sh
```

到这里Oracle主程序安装完成。

## 2.配置oracle监听程序（oracle用户执行） ##

编辑监听配置文件：`/ora01/app/database/response/netca.rsp`

命令：

    vim /ora01/app/database/response/netca.rsp

修改以下参数 (大部分已默认)：

```
INSTALL_TYPE=""custom""                               # 安装的类型
LISTENER_NUMBER=1                                     # 监听器数量
LISTENER_NAMES={"LISTENER"}                           # 监听器的名称列表
LISTENER_PROTOCOLS={"TCP;1521"}                       # 监听器使用的通讯协议列表
LISTENER_START=""LISTENER""                           # 监听器启动的名称

```

执行以下命令：

    /ora01/app/oracle/product/11.2.0/db_1/bin/netca /silent /responseFile /ora01/app/database/response/netca.rsp

查看监听程序是否运行

    netstat -tnulp | grep 1521

查询结果：

![](https://i.imgur.com/E1uBeIg.png)

开启监听命令

    /ora01/app/oracle/product/11.2.0/db_1/bin/lsnrctl start

关闭监听命令:

    /ora01/app/oracle/product/11.2.0/db_1/bin/lsnrctl stop

## 3.添加数据库实例：/ora01/app/database/response/dbca.rsp（oracle用户执行） ##

命令：

    vim /ora01/app/database/response/dbca.rsp

```
RESPONSEFILE_VERSION ="11.2.0"                              // 不要变
OPERATION_TYPE ="createDatabase"                            // 操作为创建实例  
GDBNAME ="orcl"                                             // 数据库实例名
SID ="orcl"                                                 // 实例名字
TEMPLATENAME = "General_Purpose.dbc"                        // 建库用的模板文件
SYSPASSWORD = "oracle"                                      // SYS管理员密码
SYSTEMPASSWORD = "oracle"                                   // SYSTEM管理员密码
SYSMANPASSWORD= "oracle"
DBSNMPPASSWORD= "oracle"
DATAFILEDESTINATION =/ora01/app/oradata                   // 数据文件存放目录
RECOVERYAREADESTINATION=/ora01/app/flash_recovery_area    // 恢复数据存放目录
CHARACTERSET ="AL32UTF8"                                    // 字符集
NATIONALCHARACTERSET= "AL16UTF16"                           // 字符集
TOTALMEMORY ="1638"                                         // 1638MB，物理内存2G*80%。
SOURCEDB = "myhost:1521:orcl"
SYSDBAUSERNAME = "system"
SYSDBAPASSWORD = "oracle"
```

配置完成后执行以下命令

    /ora01/app/oracle/product/11.2.0/db_1/bin/dbca -silent -responseFile /ora01/app/database/response/dbca.rsp

![](https://i.imgur.com/0OGMdda.png)

参考文档：

[https://blog.csdn.net/wangshui898/article/details/65442103](https://blog.csdn.net/wangshui898/article/details/65442103)

[https://blog.csdn.net/olengyuehun/article/details/88849948](https://blog.csdn.net/olengyuehun/article/details/88849948)

## 4.启动和关闭实例的程序 ##

修改文件`/ora01/app/oracle/product/11.2.0/db_1/bin/dbstart`和`/ora01/app/oracle/product/11.2.0/db_1/bin/dbshut`

将`ORACLE_HOME_LISTNER=$1`修改为`ORACLE_HOME_LISTNER=/ora01/app/oracle/product/11.2.0/db_1`

![](https://i.imgur.com/bDjDJjm.png)

修改文件 `/etc/oratab`将`orcl:/ora01/app/oracle/product/11.2.0/db_1:N`修改为`orcl:/ora01/app/oracle/product/11.2.0/db_1:Y`

![](https://i.imgur.com/WBCxCmz.png)

到这里，oracle就安装完成了，但是需要注意的是每次重启，都需要重新开启监听服务。

执行命令：`/ora01/app/oracle/product/11.2.0/db_1/bin/lsnrctl start`

这里设置成开机自动启动（root用户执行以下命令）

    chmod +x /etc/rc.d/rc.local

修改文件/etc/rc.d/rc.local,在文件最后追加以下内容。

```
su oracle -lc "/ora01/app/oracle/product/11.2.0/db_1/bin/lsnrctl start"
su oracle -lc "/ora01/app/oracle/product/11.2.0/db_1/bin/dbstart"

```

![](https://i.imgur.com/ey3Z8zl.png)

**简单登录oracle**

执行以下命令

```
su oracle     切换oracle用户
sqlplus       进入oracle环境 
systom        用户名
******        输入设置的密码 
```

**问题解决：**

当我修改/etc/hosts后发现 监听无法启动。

启动监听命令如下

    /ora01/app/oracle/product/11.2.0/db_1/bin/lsnrctl start

查看监听是否启动命令如下

    netstat -tnulp | grep 1521

这是由于`/etc/hosts`和`/ora01/app/oracle/product/11.2.0/db_1/network/admin/listener.ora`中的hostname不一致引起的。当修改`/etc/hosts`下的hostname时一定要将`/ora01/app/oracle/product/11.2.0/db_1/network/admin/listener.ora`中的hostname一块修改掉。否则会导致oracle无法启动监听。

![](https://i.imgur.com/35TSNYU.png)

