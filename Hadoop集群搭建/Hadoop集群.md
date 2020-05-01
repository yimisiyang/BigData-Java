

# 1.准备工作 #

## 1.Hadoop三大版本 ##

（1）Apache版本：最原始的版本，对于入们学习最好。

（2）Cloudera版本（CDH）：在大型互联网企业用的较多。

（3）Hortonworks版本（HDP）：文档较好。

## 2.Hadoop组成 ##

### 1.HDFS组成 ###

**1.NameNode**

**2.DataNode**

**3.SecondaryNameNode**

### 2.YARN组成 ###

**1.ResourceManager**

**2.NodeManager**

**3.ApplicationMaster**

**4.Container**

### 3.MapReduce组成 ###

1.Map阶段并行处理数据。

2.Reduce阶段对Map结果进行汇总。

## 3.大数据技术生态体系 ##

![](https://i.imgur.com/toOATKG.jpg)

## 4.安装centos虚拟机 ##

### **1.用VMware自定义安装（重点图片）。**

![](https://i.imgur.com/nroOzrN.png)

![](https://i.imgur.com/sIn5xN0.png)

![](https://i.imgur.com/Ed34Aqs.png)

![](https://i.imgur.com/gl7HU3P.png)

![](https://i.imgur.com/YCMPSsY.png)

![](https://i.imgur.com/lSK6u0L.png)

![](https://i.imgur.com/QdWJgyk.png)

![](https://i.imgur.com/Sd765RE.png)

![](https://i.imgur.com/2MIa2dC.png)

![](https://i.imgur.com/GbX1kj9.png)

![](https://i.imgur.com/Lbxv1kv.png)

![](https://i.imgur.com/KVVWs5p.png)

### **2.启动虚拟机（虚拟机配置的一些图片）**

![](https://i.imgur.com/g3228Bu.png)

![](https://i.imgur.com/JGgUac7.png)

**注意：** 虚拟机上密码均为root@123

**系统自带的Java版本**

![](https://i.imgur.com/io2cYvG.png)

**系统自带的Python版本**

![](https://i.imgur.com/F13KRl4.png)

### **3.打通虚拟机和Windows之间互传文件**

方法1：

安装VMware Tools工具（默认已安装）。

找到VMware Tools安装包,执行以下命令


    tar -zxvf VMwareTools-10.3.10-13959562.tar.gz


定位到解压的目录下执行以下命令


    ./vmware-install.pl


一路回车即可安装完成。



方法2：

1.建立共享文件夹。

![](https://i.imgur.com/2OWdfTe.png)

2.创建完成后在centos中查看共享文件夹

```
cd /mnt/hgfs
ls -la

```
出现问题：

设置完共享文件夹后`/mnt`目录下并没有`hgfs`目录。

可能原因：

VMware Tools 没有安装成功。重新安装一次VMware Tools再次查看。

查看结果：

![](https://i.imgur.com/ZrDXqCU.png)

### **4.Linux目录结构**

[https://blog.csdn.net/yimisiyang/article/details/89458230](https://blog.csdn.net/yimisiyang/article/details/89458230)

### **5.查看网络IP和网关信息**

1.在VMware中打开虚拟网络编辑器进行设置。

![](https://i.imgur.com/GvqrGqy.png)

2.这里装机时选择的是NAT模式，iP分配如下。

```
hadoop-master001
ip地址：192.168.1.0
子网掩码：255.255.255.0
默认网关：192.168.1.2

```
### **6.配置网络IP地址**

网络配置文件所在位置

    vim /etc/sysconfig/network-scripts/ifcfg-ens33

修改字段如下：

```
ONBOOT=yes
BOOTPROTO=static

IPADDR=192.168.1.100
GATEWAY=192.168.1.2
DNS=192.168.1.2
```

修改后内容如下：

![](https://i.imgur.com/TcH12sd.png)

修改完后重启网卡

    service network restart

Windows下查看到的网络信息

![](https://i.imgur.com/qO40Oan.png)

CentOS虚拟机下查看到的网络信息

![](https://i.imgur.com/8CkiL8l.png)

### **7.配置主机名称**

查看主机名称：

    hostname

方法1：

主机名称配置文件（CentOS6中修改方法）：

    vim /etc/sysconfig/network  

主机名称配置文件（CentOS7中修改方法）：

    vim /etc/hostname

方法2：

主机名称配置文件(CentOS6/7均适用)，该方法适合集群配置 不需要一个一个机器配置，通过`scp`命令分发给其它主机：
    
    vim /etc/hosts

本集群的hosts文件

![](https://i.imgur.com/P3zykEl.png)

Windows和Linux之间进行Hadoop连接时也需要配置Windows下的hosts文件

可应用于如下场景：windows下idea远程链接linux hadoop集群做开发，可将hadoop集群的ip主机名的对应关系写进hosts文件。

Windows下hosts所在的目录：`C:\Windows\System32\drivers\etc\hosts`

**注意：**Windows下hosts文件不能直接修改，要将写好的同名hosts文件替换掉原先的文件。

修改完后如下所示：

![](https://i.imgur.com/nRsk3Xr.png)

### **8.防火墙(学习阶段为关闭状态。)**

1.查看防火墙状态

    service iptables status

2.开启、关闭、重启防火墙命令(只是临时的开启、关闭)

```
service iptables status
service iptables start
service iptables stop
service iptables restart
```

3.查看防火墙开机启动状态：

    chkconfig iptables --list   #CentOS6
    chkconfig --list            #CentOS7



出现问题：

![](https://i.imgur.com/6FFHHdd.png)

问题原因：

没有安装`iptables-services`服务，centos默认使用的是Firewalls命令。可以不安装iptables。

CentOS7下Firewalls 和 iptables的关系。

[https://blog.csdn.net/liitdar/article/details/80889177](https://blog.csdn.net/liitdar/article/details/80889177)

CentOS7下开启关闭防火墙命令

[https://www.cnblogs.com/chling/p/11538519.html](https://www.cnblogs.com/chling/p/11538519.html)

解决方法：

    yum install iptables-services

没网离线安装方法：

[https://blog.csdn.net/qq_21072793/article/details/78658979](https://blog.csdn.net/qq_21072793/article/details/78658979)

安装后再次查看防火墙

![](https://i.imgur.com/rC5VMZ0.png)

**注意：**在CentOS7中还是使用如下方法查看防火墙

![](https://i.imgur.com/9dzRqWR.png)

### **9.在Windows下远程登录虚拟机**

![](https://i.imgur.com/SfGOWy5.png)

### **10.安装中文输入法**

[https://blog.csdn.net/alex_my/article/details/38223449](https://blog.csdn.net/alex_my/article/details/38223449)

### **11.磁盘分区类命令**

1.fdisk查看分区(root用户)

    fdisk -l

2.df查看磁盘

    df -h

3.mount/umount挂载、卸载磁盘
一般挂载在/mnt目录下

```
cd  /mnt
mkdir cdrom
count -t iso9660 -o rw /dev/cdrom /mnt/cdrom  #-t输入，挂载不同文件-t后跟的不一样，iso9660是镜像文件,-o 后跟输出 这里是可读可写，/mnt/cdrom是挂载点。
```

卸载

    umount /mnt/cdrom

### **12.rpm安装、卸载软件（rpm安装时需要安装各种依赖包，若未安装可能导致安装失败）**

1.RPM包名称格式

软件名称-软件版本号，主版本和次版本-软件运行平台-rpm

2.RPM基本语法

查询：

    rpm -qa #查询所有的安装RPM软件包。

卸载：

```
rpm -qa | grep firefox    #查询要卸载的包是否安装了
rpm -e firefox-60.2.2-1.el7.centos.x86_64  #卸载安装的Firefox
```

![](https://i.imgur.com/vVmlXXv.png)

3.安装RPM安装包

定位到存放RPM安装包的目录下执行以下命令

    rpm -ivh firefox-60.2.2-1.el7.centos.x86_64

rpm安装时常用的四个参数

`-i=install`:安装。
`-v=verbose`:显示详细信息。
`-h=hash`:显示进度条。
`--nodeps`:不检测依赖进度。

参考文档：

rpm安装路径：[http://https://blog.csdn.net/wangzengdi/article/details/33323473](http://https://blog.csdn.net/wangzengdi/article/details/33323473)  

centos安装软件的几种方法：[https://www.cnblogs.com/gmlkl/p/9354254.html](https://www.cnblogs.com/gmlkl/p/9354254.html)

### **13.yum仓库配置**

1.yum概述：

YUM是一个在Fedora和RedHat以及CentOS中的shell前端软件包管理器。基于RPM包管理，能够从指定的服务器下载RPM包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包，无需频繁一次次下载安装。

2.yum的常用命令

- 安装httpd并确认安装

```
yum install -y httpd
```

- 列出所有可用的package和package组

```
yum list
```

- 清除所有缓冲数据

```
yum clean all
```

- 列出一个包的所有依赖

```
yum deplist httpd
```

- 查看已经配置的yum源

```
yum repolist   
```

或者

```
ls /etc/yum.repos.d/
```

3.关联网络yum源

这里以163镜像为例，关联网络YUM源。

- 访问http://mirrors.163.com/.help/centos.html，根据自己的系统版本下载CentOS-Base.repo文件

- 备份原有的CentOS-Base.repo

```
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
```

- 将下载的CentOS7-Base-163.repo文件重命名为CentOS-Base.repo(下载的文件是需要放在`/etc/yum.repos.d`目录下的。)

```
[root@node000 yum.repos.d]# pwd
/etc/yum.repos.d
[root@node000 yum.repos.d]# mv CentOS7-Base-163.repo  CentOS-Base.rep
```

![](https://i.imgur.com/Jx0H2wm.png)

- 生成新的元数据和缓存

```
yum clean all
yum makecache
```

4.制作本地yum源（公司的服务器或者本机自己的）。

配置完以后的效果是不光自己本机能获取安装包，集群中任意一台机器都可以获取。

参考文档：

配置本地yum源：

[https://blog.csdn.net/ljl890705/article/details/50948126](https://blog.csdn.net/ljl890705/article/details/50948126)

[https://blog.csdn.net/Post_Yuan/article/details/79455379](https://blog.csdn.net/Post_Yuan/article/details/79455379)

参考文档：

[https://www.jianshu.com/p/57f22e371e46](https://www.jianshu.com/p/57f22e371e46)

[https://www.jianshu.com/p/ba42c589cfa2](https://www.jianshu.com/p/ba42c589cfa2)

### **14.查看自带Java的默认安装路径**

命令如下：

```
whereis java
ls -lrt /usr/bin/java
ls -lrt /etc/alternatives/java
```

结果如下：

![](https://i.imgur.com/8FyW8Q9.png)

配置Java环境变量

```
vim /etc/profile
source /etc/profile
echo $JAVA_HOME
```

![](https://i.imgur.com/VRqytq2.png)

![](https://i.imgur.com/o3ZtjSj.png)

设置`CLASSPATH`的目的，在于告诉Java执行环境，在哪些目录下可以找到您所要执行的Java程序所需要的类或者包。



### **15.scp命令使用**

举例：将master001节点上的hosts文件拷贝到slave002上

    scp /etc/hosts root@192.168.1.101:/etc/hosts

![](https://i.imgur.com/QCe85YC.png)

### **16.Linux添加/删除用户操作**

添加用户

 # 2.Hadoop运行环境搭建 # 

### **1.虚拟机网络模式设置为NAT（桥接模式也可以）**

这里设置的是桥接模式。

### **2.克隆虚拟机**

1.克隆前保证虚拟机关机

点击 虚拟机->管理->克隆 ->下一步->虚拟机中当前状态->创建完整克隆->给虚拟机命名并设置存放位置。完成克隆。

2.克隆完成后打开克隆的虚拟机，修改IP和hostname即可。 

3.这里一定要修改MAC地址，首先在虚拟机设置中找到生成的MAC地址

打开 `/etc/sysconfig/network-scripts/ifcfg-ens33` 将复制的MAC地址添加进去

```
HWADDR=MAC address
```

否则会出现克隆的虚拟机不独立，修改一台上的文件，另一台也会修改。

### **3.修改为静态IP**

准备工作中已完成。

### **4.修改主机名**

准备工作中已完成。

### **5.关闭防火墙**

准备工作中已完成。

### **6.在opt目录下创建文件**

由于opt目录属于root用户，因此创建文件夹需要用root用户来创建，否则无法创建成功。或者将普通用户提升用户权限。

**root用户创建**

```
mkdir module
mkdir software
```

**普通用户提升权限**

```
vim /etc/sudoers   #打开该文件
```

增加你要提升权限的用户信息（如图）。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/9b7c4684237746f3be312ff7cb30985.png)

**创建完成文件夹后改变文件夹拥有者和用户组**

```
chown cxk:cxk /opt/module /opt/software
```



### **7.安装jdk**

在安装jdk之前，先查看装系统时是否已经安装了openjdk,若安装了则需要卸载掉安装系统时自带的openjdk,否则后期会出现各种bug。

卸载方法：

1.查看系统自带的Java jdk。

```
rpm -qa | grep java
```
![](https://i.imgur.com/xxTLqhP.png)

2.使用root用户卸载标红的四个包(使用`--nodeps`进行强制卸载)。

```
rpm -e --nodeps java-1.8.0-openjdk-headless-1.8.0.181-7.b13.el7.x86_64
rpm -e --nodeps java-1.8.0-openjdk-1.8.0.181-7.b13.el7.x86_64
rpm -e --nodeps java-1.7.0-openjdk-1.7.0.191-2.6.15.5.el7.x86_64
rpm -e --nodeps java-1.7.0-openjdk-headless-1.7.0.191-2.6.15.5.el7.x86_64

```

查看是否卸载成功

![](https://i.imgur.com/7NI3uNg.png)

3.安装Oracle公司的Java8，本教程的安装目录为`/usr/local/java`。

Java8下载地址：[https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

![](https://i.imgur.com/2cgyI4U.png)

4.将下载的安装包放到`/usr/local/java`目录下，使用以下命令解压

    tar -zxvf jdk-8u231-linux-x64.tar.gz

5.配置Java的环境变量，并生效。配置文件为`/etc/profile`,命令如下。

打开配置文件。

```
vim /etc/profile 
```

文件结尾添加如下内容

```
export JAVA_HOME=/usr/local/java/jdk1.8.0_231
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$PATH:$JAVA_HOME/bin
```

![](https://i.imgur.com/fI1IqtE.png)

保存退出，使`/etc/profile`立即生效。

    source /etc/profile

6.查看JAVA_HOME和版本版本信息。

    echo $JAVA_HOME
    java -version

![](https://i.imgur.com/0icUksj.png)

### **8.设置ssh免密登录**

**1.ssh免密登陆原理**

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/179c95fa01502a40ea2e0b3aaead69f.png)

**2.无密钥配置**

- 进入到我的home目录

**注意：**不同用户的home目录不同，配置哪个用户的home目录，哪个用户可以免密登陆。（这里配置的是cxk 用户）

执行以下命令：

```
cd /home/cxk
ls -la
cd .ssh
ll
```

.ssh文件夹下文件的含义

（1）**known_hosts文件：**记录ssh访问过计算机的公钥（public key）。

（2）**id_rsa文件：**生成的私钥。

（3）**id_rsa.pub文件：**生成的公钥。

（4）**authorized_keys文件：**存放授权过得无密钥登陆服务器公钥。

- 生成公钥和私钥

```
ssh-keygen -t rsa    #注意该命令在cxk用户下执行。
```

**如图：**

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/082dc41dd06b78ac55b3dc8f3098723.png)

- 将公钥拷贝到要免密登陆的目标机器上(我这里就两个从几点101和102)

配置从节点免密登陆

```
ssh-copy-id hadoop-slave101
ssh-copy-id hadoop-slave102 
```

配置主节点自己免密登陆（自己也需要免密登陆）

```
cp id_rsa.pub authorized_keys
```



- 测试无密登陆是否成功。

```
ssh hadoop-slave101
```

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/a75a15cf0f40ba7f6d28447a88bd75f.png)

**解释：**此时主节点100 ssh登陆从节点101和102时不需要密码。还需要配置101和102的免密登陆，将上述流程在101和102上在走一遍即可。

### **9.ntp时间同步**

目标：总共三个节点node1（主）、node2（从）、node3（从）。均需要安装ntp服务。设置node2、node3同步node1的时间。

安装步骤：

（1）查看是否安装ntp(这里查看到所有节点均已安装ntp服务)

    rpm -q ntp

![](https://i.imgur.com/myJgWMT.png)

若没有查到，则执行`yum install -y ntp`命令进行安装。如果没有网络时，可以下载ntp离线安装包（[https://pan.baidu.com/s/1bpt3mH5](https://pan.baidu.com/s/1bpt3mH5)），执行其中的install.sh脚本即可安装，配置过程和前面完全一样

（2）查看ntp设置的是否为开机自启动(默认为disable)

    systemctl is-enabled ntpd

![](https://i.imgur.com/oboo3bX.png)

（3）更改ntp服务为开机自启动。

使用`chkconfig ntpd on`或者`systemctl enable ntpd`设置开机自启动。

![](https://i.imgur.com/0cinrwM.png)

（4）开机自启只有下次开机时才会执行，因此此时需要手动开启ntp服务。

    systemctl start ntpd.service

（5）ntp默认使用的端口是123，此时可以查看ntp服务是否正常启动。

通过端口查看ntp服务是否启动

    netstat -an | grep 123

![](https://i.imgur.com/cXl8eeZ.png)

通过命令查看服务是否已启动

    ps -ef | grep ntpd

![](https://i.imgur.com/vq2pZN0.png)

默认情况下ntp是从外网时间服务器来更新时间的，在集群中使用只要保证集群中所有的服务器时间一致即可，所以先配置其中一台服务器为时间服务器，其他服务器(相当于客户端)与时间服务器进行时间同步，从时间服务器上获取时间数据，从而避免联网，可用性更高。

(6)时间服务器配置，这里配置node1（主节点）为时间服务器，当然也可以配置从节点为时间服务器。

    vim /etc/ntp/conf

![](https://i.imgur.com/pCLJSKE.png))

配置完成后，重启ntp服务。

    systemctl restart ntpd.service 

（7）配置客户端，即本集群中的node2、node3从节点。

打开`/etc/ntp.conf`文件，增加一行代码`server 192.168.1.100`，因为这里时间服务器地址为192.168.1.100

![](https://i.imgur.com/ud4ccrA.png)

配置完成后同样重启ntp服务`systemctl restart ntpd.service`。

所有客户端都进行以上配置，都启动之后，集群会自动定期进行服务的同步，这样集群的时间就保持一致了，另外如果想要手动同步某一台机器的时间，那么可以依次执行下面命令实现。

```
systemctl stop ntpd  # 先停止服务，否则ntp socket会被占用
ntpdate 192.168.0.157  # 手动执行同步
systemctl start ntpd  # 继续启动服务
```
遇到问题：

通过`ntpstat`查看同步情况，发现未能正确同步。

修改内容（时间服务器）：

![](https://i.imgur.com/pCLJSKE.png))

将上图中`server 192.168.1.100`和`fudge 192.168.1.100`改为`server 127.127.1.0`和`fudge 127.127.1.0`。

在客户端上执行手动同步命令进行同步

```
systemctl stop ntpd  # 先停止服务，否则ntp socket会被占用
ntpdate 192.168.0.157  # 手动执行同步
systemctl start ntpd  # 继续启动服务
```

客户端状态同步状态

![](https://i.imgur.com/ZMJF5Vw.png)

**遇到问题：**

设置ntp服务为开机自启，可是开机后依然无法自启。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/87ec5a12ab9468287b0aea24dfe609d.png)

问题原因：

查找资料发现，是存在服务和ntp冲突导致开机启动未生效，这个服务是chrony。

解决方法：

使用systemctl  is-enabled chronyd查看chrony的启动状态设置，果然也是开机启动，使用 systemctl disable chronyd 将chrony服务的自启动关闭。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/948a6ab47c760e6e513ec719930430d.png)

https://blog.csdn.net/I_Demo/article/details/99673094

参考文档：
[https://blog.csdn.net/jy02268879/article/details/81387531](https://blog.csdn.net/jy02268879/article/details/81387531)
[https://www.cnblogs.com/freeweb/p/5390552.html](https://www.cnblogs.com/freeweb/p/5390552.html)
[https://www.cnblogs.com/30go/p/11231110.html](https://www.cnblogs.com/30go/p/11231110.html)

### **10.安装Hadoop**（这里安装的是hadoop-3.2.1版本）

1.将安装包解压到 `/opt/module` 下面

```
tar -zxvf hadoop-3.2.1.tar.gz -C /opt/module
```



2.查看是否解压成功，并配置Hadoop中的hadoop-env.sh

hadoop-env.sh配置文件在/opt/module/hadoop-3.2.1/etc/hadoop目录下。

**在hadoop-env.sh文件中配置下JAVA_HOME**

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/f0f418b6583beab1631758dba76a6cf.png)

3.将Hadoop添加到环境变量

```
vim /etc/profile #打开环境变量配置文件
```

增加以下内容：

```
#HADOOP_HOME
export HADOOP_HOME=/opt/module/hadoop-3.2.1
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin
```

## 3.Hadoop运行

### 1.Hadoop有三种运行模式

- 本地运行模式
- 伪分布式运行模式（只有一个节点）
- 完全分布式运行模式（多个节点）

### 2.伪分布式集群配置

- 配置：hadoop-env.sh

获取JAVA_HOME安装路径（echo $JAVA_HOME命令）

配置JAVA_HOME路径（安装hadoop时已配置）

- 配置：core-site.xml

```
<!--指定HDFS中NameNode的地址-->
<property>
	<name>fs.defaultFS</name>
    <value>hdfs://hadoop101:8020</value>
</property>

<!--指定hadoop运行时产生文件的存储目录-->
<property>
	<name>hadoop.tmp.dir</name>
	<value>/opt/module/hadoop-3.2.1/data/tmp</value>
</property>
```

- 配置：hdfs-site.xml

```
<!--指定HDFS副本的数量-->
<property>
	<name>dfs.replication</name>
	<value>1</value>
</property>
```

#### 1.启动伪分布式集群

- 格式化namenode(第一次启动时格式化，以后就不用总格式化了)

所在目录： `/opt/module/hadoop-3.2.1/bin`   ,我们已经加入了path环境变量，因此任何目录均可启动。

```
hdfs namenode -format
```

- 启动namenode

所在目录： `/opt/module/hadoop-3.2.1/sbin`   ,我们已经加入了path环境变量，因此任何目录均可启动。

```
hadoop-daemon.sh start namenode
```

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/423e4a0bda4ab1d77f4f0093e26f9dc.png)

- 启动datanode

所在目录： `/opt/module/hadoop-3.2.1/sbin`   ,我们已经加入了path环境变量，因此任何目录均可启动。

```
hadoop-daemon.sh start datanode
```

#### 2.查看集群

**查看是否启动成功**

```
jps    #查看进程方式
```

**查看产生的log日志**

日志所在文件夹`/opt/module/hadoop-3.2.1/logs`

**web端查看HDFS文件系统**

192.168.1.100：50070

#### 3.操作集群

**在hdfs 上操作伪分布集群**

- 在hdfs文件系统创建一个input文件夹
- 将测试文件内容上传到文件系统上
- 查看上传的文件是否正确
- 在HDFS上运行mapreduce程序
- 查看输出结果
- 将测试文件内容下载到本地
- 删除输出结果

### 3.完全分布式集群搭建

#### 1.集群规划

| hadoop-master100 |       hadoop-slave101       |  hadoop-slave102  |
| :--------------: | :-------------------------: | :---------------: |
|     **HDFS**     |          **HDFS**           |     **HDFS**      |
|     NameNode     |                             | SecondaryNameNode |
|     DataNode     |          DataNode           |     DataNode      |
|     **YARN**     |          **YARN**           |     **YARN**      |
|   NodeManager    | ResourceManager/NodeManager |    NodeManager    |

**解释：**由于namenode 、secondarynamenode、resourcemanger消耗内存所以将他们分配到不同节点，降低集群压力。

#### 2.将所有节点都安装上hadoop-3.2.1 

安装方法参考“安装"Hadoop"

#### 3.配置集群

- 配置core-site.xml文件

```
<!--指定HDFS中NameNode的地址-->
<property>
	<name>fs.defaultFS</name>
    <value>hdfs://hadoop-master100:8020</value>
</property>

<!--指定hadoop运行时产生文件的存储目录-->
<property>
	<name>hadoop.tmp.dir</name>
	<value>/opt/module/hadoop-3.2.1/data/tmp</value>
</property>
```

配置完成如图：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/4ae5e4f61b88f5dfcdda9059102275b.png)

- hdfs需要配置以下文件：hadoop-env.sh、hdfs-site.xml、slaves。

hadoop-env.sh中配置JAVA_HOME（安装hadoop-3.2.1时已配置）

hdfs-site.xml中配置副本数和secondarynamenode

```
<!--指定HDFS副本的数量-->
<property>
	<name>dfs.replication</name>
	<value>3</value>
</property>

<!--指定secondarynamenode所在节点-->
<property>
	<name>dfs.namenode.secondary.http-address</name>
	<value>hadoop-slave102:50090</value>
</property>
```

配置完如图：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/8a592d1d02afd8251c2f27cfa56424a.png)

slaves配置的是datanode存放在那几个节点上

**一般配置了namenode的节点就不再配置datanode了，由于机器有限，这里三台虚拟机都配置了datanode。**

**注意：**在hadoop2中是配置文件中的slaves，hadoop3中变成了workers。

```
hadoop-master100
hadoop-slave101
hadoop-slave102
```

- yarn需要配置以下文件：yarn-env.sh、yarn-site.xml。

yarn-env.sh中配置JAVA_HOME

```
/usr/local/java/jdk1.8.0_231
```

**解释：**在hadoop-3.2.1版本yarn-env.sh文件中并没有找到配置JAVA_HOME的地方，此处没有配置。（可能是Hadoop3版本无需配置的缘故）

yarn-site.xml中配置NodeManager和ResourceManager信息。

```
<!--reducer获取数据的方式-->
	<property>
		<name>yarn.nodemanager.aux-services</name>
		<value>mapreduce_shuffle</value>
	</property>
<!--指定YARN上ResourceManager的地址-->
	<property>
		<name>yarn.resourcemanager.hostname</name>
		<value>hadoop-slave101</value>
	</property>
```

配置完成如图

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/5dc71988068c40ae1c837a443d966ad.png)

- mapreduce需要配置以下文件：mapred-env.sh、mapred-site.xml。

mapred-env.sh中配置JAVA_HOME

```
/usr/local/java/jdk1.8.0_231
```

**解释：**在hadoop-3.2.1版本mapred-env.sh文件中并没有找到配置JAVA_HOME的地方，此处没有配置。（可能是Hadoop3版本无需配置的缘故）

mapred-site.xml配置mapreduce运行在yarn上，默认的是local。

```
<!--指定mapreduce运行在yarn上-->
	<property>
		<name>mapreduce.framework.name</name>
		<value>yarn</value>
	</property>
```

配置完成如图：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/acf0bd5b6ec387f031a4b263423d40d.png)

**配置完100后，同样的方法配置101和102。**

### 4.启动集群

- 第一次启动集群的时候必须先格式化一下namenode。

命令如下：

```
hdfs namenode -format
```

格式化成功图片

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/406e2ab5a81470fcb7a6b0e006b547b.png)

**解释：**namenode在100上，因此该命令最好在100上操作。

- 启动namenode/datanode/secondarynamenode

```
start-dfs.sh
```

启动完成图片：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/f404ed78b0d771f972d2c754016ca49.png)

通过 `jps`命令可以查看到如下结果

| hadoop-master100 | hadoop-slave101 | hadoop-slave102        |
| ---------------- | --------------- | ---------------------- |
| 21698 DataNode   | 9726 Jps        | 9821 SecondaryNameNode |
| 21526 NameNode   | 9647 DataNode   | 9917 Jps               |
| 21943 Jps        |                 | 9743 DataNode          |

结果与集群规划一致。

**解释：**start-dfs.sh /stop-dfs.sh启动/停止hdfs相关的；start-yarn.sh/stop-dfs.sh启动yarn相关的; start-all.sh /stop-all.sh启动所有。**但是，namenode和resourcemanager服务必须在自己服务器上启动。否则启动不起来。**

**遇到问题：**

问题1：

将datanode配置到slaves中，启动集群时无法启动。查资料发现Hadoop3中的datanode是配置在workers文件中的。

问题2：

发现启动完成后，hadoop-slave101和hadoop-slave102上都有secondarynamenode进程，而我并没有把secondarynamenode配置到在hadoop-slave101上。

进一步发现，当我修改slave1上的文件时slave2文件也会修改。

这才想到由于slave2是slave1的克隆机器，当时并没有修改slave2的MAC地址，因此slave2和slave1不是独立的。这时要关掉一台机器，配置另一台的MAC地址。否则会导致两台机器MAC同时修改。

问题3：

secondarynamenode 进程无法启动，发现是权限问题。即mater100主机无法ssh免密登陆slave2。重新配置了一遍可以了。

问题4：

web端无法访问namenode(端口50070)

通过查询官方文档发现，hadoop3 的namenode的默认访问端口为9870，而不是所谓的50070，也不用修改所谓的hdfs-site.xml文件（我修改过没测试成功）。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/c4b397351c5cbb57f83795bd49e8a91.png)

同理，web端访问ResourceManager的默认端口是8088.

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/2e98462179861287ca052a39527b3d1.png)

### 5.操作集群

#### 1.上传文件

- 查看集群有没有东西命令。

```
hdfs dfs -ls /     #查询根目录下所有文件 
hdfs dfs -lsr /    #递归查询根目录
```

- 在根目录下创建文件夹

```
hdfs dfs -mkdir /user
hdfs dfs -mkdir -p /user/cxk         #递归创建
hdfs dfs -mkdir -p /user/cxk/input    
```

- 在100的hadoop-3.2.1目录下创建一个input文件夹，并在input文件夹下创建一个文件。

```
mkdir input
touch yangguo
```

- 将创建好的文件上传到hdfs上。

```
hdfs dfs -put input/yangguo /user/cxk/input     #上传小文件测试
hdfs dfs -put /opt/software/hadoop-3.2.1.tar.gz /user/cxk/input  #上传大文件测试
```

**上传成功图片：**

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/e796861bb2509f7e27d15ede09f3824.png)

查看文件上传位置：

进入 `/opt/module/hadoop-3.2.1/data/tmp/dfs`目录 下面有一个`data`和一个`name`目录。

hadoop存放原始数据的目录深度

```
/opt/module/hadoop-3.2.1/data/tmp/dfs/data/current/BP-1201586042-192.168.1.100-1581047051365/current/finalized/subdir0/subdir0
```

**在该目录下，可以通过把块拼接起来的方法回复出原始数据。（采用重定向方法）**

####  2.下载文件

命令

```
hdfs dfs -get /user/cxk/input/hadoop-3.2.1.tar.gz ./
```

#### 3.集群启动方式总结

- 各个服务器组件逐一启动

```
hadoop-daemon.sh start|stop namenode|datanode|secondarynamenode
```

```
yarn-daemon.sh start|stop resourcemanager|nodemanager
```

一般在集群增加节点、退出节点时候用。

- 各个模块分开启动（配置ssh是前提）常用。

（1）整体启动/停止hdfs

```
start-dfs.sh 
stop-dfs.sh
```

（2）整体启动/停止yarn

```
start-yarn.sh
stop-yarn.sh
```

- 全部启动（不建议使用）

```
start-all.sh
stop-all.sh
```

#### 4.HDFS相关内容

- HDFS概念：

  HDFS系统适合一次写入，多次读出的场景，且不支持文件的修改，适合用来做数据分析，不适合做网盘应用。

  HDFS由Namenode、Datanode、SecondaryNamenode组成。

  HDFS数据库大小128MB，块的大小可以通过dfs.blocksize来规定，一般不配置。

  块的大小由寻址时间而定：假如寻址时间10ms，传输速率100MB/s，为了使寻址时间仅占传输时间的1%。此时块的大小：100ms×100×100M/s=100M。

-  HDFS命令行操作

hdfs dfs 查看所有命令

```
hdfs dfs -help [命令]
hdfs dfs -ls /  #查询
hdfs dfs -mkdir #创建文件夹
hdfs dfs -mkdir -p  #深层递归创建
hdfs dfs -moveFromLocal [本地目录] [hdfs目录] #把本地文件移动到hdfs
hdfs dfs -moveToLocal [hdfs目录] [本地目录]    #hdfs文件移动到本地，该命令已经取消了。
hdfs dfs -appendToFile [本地文件] [hdfs文件]   #追加一个文件到已经存在文件的末尾
hdfs dfs -cat [hdfs上文件名]    #查看文件
hdfs dfs -tail [hdfs上文件名]   #直接查看文件末尾
hdfs dfs -text [hdfs上文件名]   #以字符形式打印一个文件内容
hdfs dfs -chgrp | -chmod | -chown  #和Linux用法一样，修改权限。
hdfs dfs -copyToLocal [hdfs路径] [本地路径] #从hdfs拷贝到本地
hdfs dfs -copyFromLocal [本地路径] [hdfs路径] #从本地拷贝到hdfs
hdfs dfs -cp [hdfs路径1] [hdfs路径2]  #hdfs上文件从一个路径拷贝到另一个路径
hdfs dfs -mv [hdfs路径1] [hdfs路径2]  #hdfs上文件从一个路径移动到另一个路径
hdfs dfs -get [hdfs路径] [本地路径]  #等同于copyToLocal
hdfs dfs -getmerge [hdfs上多个文件] [本地合并后的文件路径及名称]  #合并下载多个文件
hdfs dfs -put [本地路径] [hdfs路径]  #等同于copyFromLocal
hdfs dfs -rm  #删除文件或文件夹
hdfs dfs -rmdir   #删除空目录
hdfs dfs -df -h   #统计文件系统可用信息
```

#### 5.HDFS 客户端操作

- IDEA环境准备
- 通过API操作HDFS
- 通过IO流操作HDFS

## 4.Zookeeper学习

### 1.zookeeper理论篇

**1.概述**

从设计角度理解，zookeeper是一个基于观察者模式设计的分布式服务管理框架，负责存储和管理大家关心的数据，然后接受观察者注册，一旦这些数据状态发生变化，zookeeper就将负责同志已经在zookeeper上注册的那些观察者做出相应的反应，从而实现集群中类似Master/Slave管理模式。

Zookeeper = 文件系统 + 通知机制

存储一些核心关键数据。

**2.应用场景**

提供的服务包括：分布式消息同步和协调机制、服务器节点动态上下线，统一配置管理、负载均衡、集群管理等。

典型应用场景

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/b02b6370b152ebac9e63d5a1c68b310.png)



![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/a402faa43fc314766ff49307f82be3c.png)

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/ed49d08be1a9deba71a1dc5d0a06272.png)

### 2.zookeeper安装

**0.集群规划**

zookeeper安装在hadoop-master100、hadoop-slave101、hadoop-slave102上。

**1.官网下载安装包**：http://zookeeper.apache.org/releases.html#download

我这里下载的是3.5.6稳定版（apache-zookeeper-3.5.6.bin.tar.gz）

**2.将安装包复制到/opt/software下，并使用以下命令解压到/opt/module下**

```
tar -zxvf apache-zookeeper-3.5.6.bin.tar.gz -C /opt/module
```

**3.修改配置信息**

1.将/opt/module/zookeeper-3.5.6/conf路径下的zoo_sample.cfg修改为zoo.cfg

2.进入zoo.cfg文件：`vim zoo.cfg`

3.在/opt/module/zookeeper-3.5.6/目录下创建data/zkData，并修改dataDir路径

```
dataDir=/opt/module/zookeeper-3.5.6/data/zkData
```

若是配置集群还需要增加以下配置信息

```
##########################cluster#####################
server.100=hadoop-master100:2888:3888
server.101=hadoop-slave101:2888:3888
server.102=hadoop-slave102:2888:3888
```

**解释：**

server.1/server.2/server.3 代表集群中第几号设备,有几个server就代表有几个服务器节点。

集群模式下配置一个myid，这个文件在dataDir目录下，这个文件里面有这个数据，Zookeeper启动时读取此文件，拿到里面的数据与zoo.cfg里面的配置信息比较从而判断是哪个server。

hadoop-master100/hadoop-slave101/hadoop-slave102代表主机名或者IP。

2888：leader和flower通信的端口号。

3888：leader挂掉以后重新选举leader时，leader和follower通信的端口号。选举要求所有follower参与。

配置完成如下：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/12054d48d2cb18eb4bd0bcb7154c4eb.png)

在`/opt/module/apache-zookeeper-3.5.6/data/zkData`下创建myid文件。

```
touch myid
```

在myid里面为节点编号，该编号由 server.0/server.1/server.2 中的 0 1 2 确定。

haoop-master100的编号如下所示

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/873f3bbc95694a8fe91f67578a3c996.png)

同样方法配置其它节点。

**4.zookeeper启动与停止**

为了方便后面启动最好把zookeeper配置到环境变量中。/etc/profile 文件增加以下内容

```
#ZOOKEEPER_HOME
export ZOOKEEPER_HOME=/opt/module/apache-zookeeper-3.5.6-bin
export PATH=$PATH:$ZOOKEEPER_HOME/bin
```



服务端启动与停止

```
zkServer.sh start       #启动
zkServer.sh stop        #停止
zkServer.sh restart     #重启
zkServer.sh status      #状态
```

客户端启动与停止

```
zkCli.sh         #启动
quit             #退出客户端
```

启动报错1：

```
错误: 找不到或无法加载主类 org.apache.zookeeper.server.quorum.QuorumPeerMain
```

解决方法：

总结：原来从目前的最新版本3.5.5开始，带有bin名称的包才是我们想要的下载可以直接使用的里面有编译后的二进制的包，而之前的普通的tar.gz的包里面是只是源码的包无法直接使用。

好想吐槽下啊，Zookeeper的包的变动，源码的包就不能向其他的安装包一样加个src的标识吗？见名知意多好，以避免误下载。

所以同学们，如果下载3.5.5以后的版本的Zookeeper安装包，我们乖乖的下载带有bin标识的包就不会有问题了。

启动报错2：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/1e8ca8aca3410b82d1e6c4b94479f4d.png)

问题原因：

配置文件中增加Cluster 时不小心加入了空格 ，**切记配置文件一定不能有空格等特殊字符**。

**5.zookeeper配置文件的参数解读**

（1）tickTime:通信心跳数

默认2000ms。

（2）initLimit:Leader与Follower初始通信时限

默认10个心跳

（3）syncLimit:Leader与Follower同步通信时限

默认5个心跳

（4）dataDir:数据文件目录+数据持久化路径

（5）clientPort:客户端连接端口

默认2181端口

### 3.zookeeper相关知识

**1.数据结构**

Zookeeper数据模型的结构与Unix文件系统很类似，整体可以看作是一棵树，每个节点称作ZNode,每个znode默认能够存储1MB的数据，每个znode可以通过其路径唯一标识

**2.节点类型**

leader  follower

**3.选举机制**

（1）半数机制：集群中半数以上机器存活，集群可用。所以zookeeper适合装在奇数台机器上。

（2）zookeeper虽然在配置文件中没有指定master和slave。但是，zookeeper工作时，会有一个节点为leader，其它为follwer。leader通过内部选举机制产生。

**4.zookeeper的特点**

- 一个leader 多个follower组成集群
- leader复制进行投票的发起和决议，更新系统状态
- follower用于接收客户端请求，并向客户端返回结果。在选举leader中参与投票
- 集群中只要有半数以上节点存活，zookeeper就能正常服务
- 全局数据一致，每个server保存一份相同的数据副本，client无论连接到哪个server，数据均一致
- 更新请求顺序进行，来自同一个client的更新请求按其发送顺序依次进行
- 数据更新原子性，要么成功，要么失败
- 实时性，在一定时间范围内，client能读到最新数据

### 4.命令行操作zookeeper集群

(1)启动服务端

```
zkServer.sh start  # 这里三台机器都要启动
```

（2）启动客户端

```
zkCli.sh   #启动客户端，在三台上任意一台启动就行
```

```
quit    #退出客户端
```

（3）常用命令（help获取所有命令）

- 查看当前znode中包含的内容

```
ls /
ls2 /       #查看详情
```

- 创建普通节点

```
create /节点名  data    # data 用来对节点名进行描述。在创建节点时，data已经位于节点上了。
```

- 获得节点的值

```
get /xiyou     #获得xiyou节点所有的信息
```

- 创建短暂节点

```
create -e /节点名 data
```

- 创建带序号的节点

```
create -s /节点名 data
```

- 修改节点的数据值

```
set /节点名 改后data
```

- 节点值变化的监听

```
get /节点名 watch   #监听事件注册一次只生效一次。在次生效，在次注册。
```

- 路径上的子节点变化监听(增加节点，删除节点)

```
ls /路径   watch    #监听事件注册一次只生效一次。在次生效，在次注册。
```

- 删除节点

```
delete /节点名       #只能删除单一节点
```

- 递归删除节点（如：节点的整个目录）

```
rmr /节点路径       #下面节点全部删除
```

- 节点状态

```
stat /节点名
```



**解释：**znode有两种类型

短暂（ephemeral）:客户端和服务器断开连接后，创建的节点自己删除

持久（persistent）:客户端和服务器断开连接后，创建的节点不删除

**znode有四种形式的节点（默认创建持久节点（persistent））**

（1）持久化目录节点

需要记住节点名，避免重复。

（2）持久化顺序编号目录节点

每个节点编号，且编号不重复。

（3）临时目录节点

（4）临时顺序编号目录节点

## 5.HBase学习

### 1、HBase部署

HBase依赖于 HDFS 、YARN、Zookeeper。因此这三项启动成功后才能启动HBase。

**1.下载安装包：**下载地址：https://hbase.apache.org/downloads.html 这里我下载的是2.2.3版本。

**2.解压到`/opt/module`目录下**

```
tar -zxvf hbase-2.2.3-bin.tar.gz -C /opt/module/
```

**3.定位到 `/opt/module/hbase-2.2.3/conf`目录下。**

```
drwxr-xr-x. 2 cxk cxk  208 11月 19 23:28 .
drwxrwxr-x. 7 cxk cxk  182 2月  13 16:20 ..
-rw-r--r--. 1 cxk cxk 1811 11月 19 23:27 hadoop-metrics2-hbase.properties
-rw-r--r--. 1 cxk cxk 4284 11月 19 23:27 hbase-env.cmd
-rw-r--r--. 1 cxk cxk 7351 11月 19 23:28 hbase-env.sh
-rw-r--r--. 1 cxk cxk 2257 11月 19 23:27 hbase-policy.xml
-rw-r--r--. 1 cxk cxk  934 11月 19 23:27 hbase-site.xml
-rw-r--r--. 1 cxk cxk 1168 11月 19 23:27 log4j-hbtop.properties
-rw-r--r--. 1 cxk cxk 4977 11月 19 23:27 log4j.properties
-rw-r--r--. 1 cxk cxk   10 11月 19 23:27 regionservers
```

**解释：**hbase-env.cmd为Windows下配置文件，用不着可以删除掉。

修改配置文件：

`hbase-env.sh` 文件修改内容

这里面需要配置两个地方

- JAVA_HOME =/usr/local/java/jdk1.8.0_231
- `export HBASE_MANAGES_ZK=false`  不使用自带的zookeeper，设置使用外部的zookeeper。

`hbase-site.xml`  配置文件

打开hbase参考文档

```xml
<configuration>
#配置namenode所在节点，这里由于namenode没有配置高可用，因此使用了具体节点。
 <property>
  <name>hbase.rootdir</name>
  <value>hdfs://hadoop-master100:8020/hbase</value>
 </property>
#配置支持分布式
 <property>
  <name>hbase.cluster.distributed</name>
  <value>true</value>
 </property>
#配置端口。value若只写端口号意味着高可用，我这里不配置高可用。
 <property>
  <name>hbase.master</name>
  <value>hdfs://haoop-master100:60000</value>
 </property>
#配置zookeeper
 <property>
  <name>>hbase.zookeeper.quorum</name>
  <value>haoop-master100:2181,haoop-slave101:2181,haoop-slave102:2181</value>
 </property>
#配置属性文件的缓存地址
 <property>
  <name>hbase.zookeeper.property.dataDir</name>
  <value>/opt/module/hbase-2.2.3/dataDir</value>
 </property>
#禁止检查流功能     （注：不配置它HMaster总是启动失败）
 <property>
  <name>hbase.unsafe.stream.capability.enforce</name>
  <value>false</value>
 </property>
</configuration>
```

`regionservers` 配置文件

```
hadoop-master100
hadoop-slave101
hadoop-slave102
```

到这里配置文件已经修改完成了。

**4.修改hbase lib下面Hadoop 和zookeeper  jar包。（刚开始安装2.1.8版本，替换后HRegionserver,起来后就，几秒就被杀死。换成了2.2.3版本的包，且没有操作这一步。）**

一般情况下hbase自带的jar包和安装的Hadoop版本不兼容。因此这里需要修改。若修改完配置后，hbase能正常启动则不需要替换。

需要替换的jar 包，位于/opt/module/hbase-2.1.8/lib目录下。

```
#需要替换所有的Hadoop包
hadoop-annotations-2.7.7.jar 
hadoop-auth-2.7.7.jar
hadoop-client-2.7.7.jar     #hadoop3.2.1下未找到。
hadoop-common-2.7.7.jar
hadoop-common-2.7.7-tests.jar
hadoop-distcp-2.7.7.jar
hadoop-hdfs-2.7.7.jar 
hadoop-hdfs-2.7.7-tests.jar
hadoop-mapreduce-client-app-2.7.7.jar
hadoop-mapreduce-client-common-2.7.7.jar
hadoop-mapreduce-client-core-2.7.7.jar
hadoop-mapreduce-client-hs-2.7.7.jar
hadoop-mapreduce-client-jobclient-2.7.7.jar
hadoop-mapreduce-client-shuffle-2.7.7.jar
hadoop-minicluster-2.7.7.jar        #hadoop3.2.1下未找到
hadoop-yarn-api-2.7.7.jar
hadoop-yarn-client-2.7.7.jar
hadoop-yarn-common-2.7.7.jar
hadoop-yarn-server-applicationhistoryservice-2.7.7.jar
hadoop-yarn-server-common-2.7.7.jar
hadoop-yarn-server-nodemanager-2.7.7.jar
hadoop-yarn-server-resourcemanager-2.7.7.jar
hadoop-yarn-server-tests-2.7.7-tests.jar
hadoop-yarn-server-web-proxy-2.7.7.jar
#需要替换的zookeeper包
zookeeper-3.4.10.jar
```

查找方法：

定位到hadoop安装目录/opt/module/hadoop-3.2.1

```
find -name *.jar
cp *.jar ./cxk      #复制到自己创建的文件夹下
```

两个未找到的包解决方法：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/18dbaa3abd28e98f81b86a91e6dfb78.png)

有可能时hadoop3 .X  和 hadoop2.X  中包有所调整，上图是我在hadoop3.2.1中查到的。于是复制了查到的三个包。

**5.配置HBASE_HOME环境变量**

为了方便启动，我们这里配置一下HBASE_HOME。

打开/etc/profile

```
sudo vim /etc/profile
```

添加以下内容

```
#HBASE_HOME
export HBASE_HOME=/opt/module/hbase-2.2.3
export PATH=$PATH:$HBASE_HOME/bin
```

使环境变量生效

```
source /etc/profile
```



**问题描述：**

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/f1e525058812ac04db5dd0f81e4b14f.png)

从上面描述可以看出是由于`/opt/module/hadoop-3.2.1/share/hadoop/common/lib`和`/opt/module/hbase-2.1.8/lib/client-facing-thirdparty`下有一个包冲突引起的 我这里选择删除`/opt/module/hbase-2.1.8/lib/client-facing-thirdparty`下的`slf4j-log4j12-1.7.25.jar`

删掉之后又提示我缺包，然后在拷回来。

3.Google了好几个帖子找到解决方法.
修改hbase项目下/conf/hbase-env.sh这个文件.添加下面这行
看key名字就应该能明白:Hbase禁用查找Hadoop的Classs=True

```
export HBASE_DISABLE_HADOOP_CLASSPATH_LOOKUP=true #这行是添加的，而不是修改的
```

这次启动不再报冲突，如图：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/41878a0a71c5ba6ce27476d6aed06a1.png)

### 2、启动HBASE

```
start-hbase.sh
```



## 6、安装MySQL数据库

MySQL数据库安装在了hadoop-master和hadoop-slave1上 登陆密码与系统密码相同root@123。

**1、先检查系统是否已经安装MySQL**

```
rpm -qa | grep mysql
```

返回值为空，说明没有安装。

这里执行安装命令是无效的，因为centos-7默认是Mariadb，所以执行以下命令只是更新Mariadb数据库。

```
yum install mysql
```

若已安装，删除可用Mariadb

```
yum remove mysql
```

**2、下载MySQL的repo源**

```
wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm
```

安装mysql-community-release-el7-5.noarch.rpm包

```
# sudo rpm -ivh mysql-community-release-el7-5.noarch.rpm
```

安装这个包后，会获得两个mysql的yum repo源：/etc/yum.repos.d/mysql-community.repo，/etc/yum.repos.d/mysql-community-source.repo。可以定位到/etc/yum.repos.d目录下查看。

**3、安装MySQL**

```
sudo yum install mysql-server
```

根据步骤安装就可以了，不过安装完成后，没有密码，需要重置密码。

安装后再次查看mysql

```
rpm -qa | grep mysql
```

**4、重置密码和授权远程登陆**

重置密码

```
#登陆MySQL
mysql -u root  #第一次登陆不需要密码，若没有登陆成功，查看MySQL服务是否正常启动，syatemctl status mysqld
#使用MySQL数据库
use mysql;
#更新密码
mysql > update user set password=password('你要设的密码') where user='root';
```

授权远程登陆

```
#授权远程登陆
GRANT ALL PRIVILEGES ON *.* TO root@"%" IDENTIFIED BY "远程登陆密码";　
#退出数据库
exit;
```

**5、重启MySQL数据库**

```
#重启MySQL
systemctl restart mysql
#查看MySQL状态
systemctl status mysql
```

**6、测试密码登陆和远程登陆**

若能进行远程登陆，登陆成功后查看数据库编码格式。

```
#查看数据库编码格式
show variables like "%char%";
#设置编码格式
set names utf8;
```

## 7、安装Hive

**安装须知：**

安装hive前提是要先安装hadoop集群，并且hive只需要再hadoop的namenode节点集群里安装即可(需要再所有namenode上安装)，可以不在datanode节点的机器上安装。另外还需要说明的是，虽然修改配置文件并不需要你已经把hadoop跑起来，但是本文中用到了hadoop命令，在执行这些命令前你必须确保hadoop是在正常跑着的，而且启动hive的前提也是需要hadoop在正常跑着，所以建议你先将hadoop跑起来在按照本文操作。

**1、下载hive**

**官网：**http://hive.apache.org/downloads.html

这里下载的版本是3.1.2，因为我安装的Hadoop版本是 3.2.1。这里需要注意的是安装的hive版本要兼容你安装的Hadoop版本。

**2、安装hive**

这里hive只安装 hadoop-master100节点，其它节点暂时并没有安装。

**将下载好的hive安装包解压到/opt/module目录下**

```
tar -zxvf apache-hive-3.1.2-bin.tar.gz -C /opt/module/
```

**配置hive环境变量**

```
sudo vim /etc/profile
```

添加以下内容

```
#HIVE_HOME
export HIVE_HOME=/opt/module/apache-hive-3.1.2-bin
export HIVE_CONF_DIR=/opt/module/apache-hive-3.1.2-bin/conf
export PATH=$PATH:$HIVE_HOME/bin
```

使环境变量生效

```
source /etc/profile
```

**1、配置hive-site.xml**

```
#进入目录
cd /opt/module/apache-hive-3.1.2-bin/conf
#拷贝hive-default.xml.template文件
cp hive-default.xml.template hive-site.xml
#编辑 hive-site.xml 
vim hive-site.xml
```

**由于hive-site.xml中需要配置以下内容，因此在Hadoop集群上创建相应的目录**

配置内容(下面显示的是hive默认配置)

```xml
  <name>hive.metastore.warehouse.dir</name>
  <value>/user/hive/warehouse</value>
   <name>hive.exec.scratchdir</name>
  <value>/tmp/hive</value>
```

创建目录（与配置文件中默认配置保持一致）

```
hadoop fs -mkdir -p /user/hive/warehouse
hadoop fs -chmod -R 777 /user/hive/warehouse
hadoop fs -mkdir -p /tmp/hive/
hadoop fs -chmod -R 777 /tmp/hive
//使用以下命令检查目录是否创建成功
hadoop fs -ls /user/hive
hadoop fs -ls /tmp/hive
```

**修改hive-site.xml中的临时目录**

将hive-site.xml文件中的${system:java.io.tmpdir}替换为hive的临时目录，例如我替换为/usr/local/apache-hive-2.1.1/tmp/，该目录如果不存在则要自己手工创建，并且赋予读写权限。

创建目录

```
cd /opt/moduleapache-hive-3.1.2-bin
mkdir tmp
chmod -R 775 tmp/
```

修改hive-site.xml

原内容：

```xml
<property>
  <name>hive.downloaded.resources.dir</name>
  <value>${system:java.io.tmpdir}/${hive.session.id}_resources</value>
  <description>Temporary local directory for added resources in the remote file system.</description>
  </property>
```

替换后内容：

```xml
<property>
  <name>hive.downloaded.resources.dir</name>
  <value>/opt/moduleapache-hive-3.1.2-bin/tmp/${hive.session.id}_resources</value>
  <description>Temporary local directory for added resources in the remote file system.</description>
  </property>
```



同样的方法将hive-site.xml中的${system:user.name}都替换为root。

原内容：

```xml
<property>
  <name>hive.server2.logging.operation.log.location</name>
  <value>${system:java.io.tmpdir}/${system:user.name}/operation_logs</value>
  <description>Top level directory where operation logs are stored if logging functionality is enabled</description>
  </property>
```

替换后内容：

```xml
<property>
  <name>hive.server2.logging.operation.log.location</name>
  <value>/opt/moduleapache-hive-3.1.2-bin/tmp/root/operation_logs</value>
  <description>Top level directory where operation logs are stored if logging functionality is enabled</description>
  </property>
```



**注意：** 以上两处内容在文件中出现不止一次，应全部替换。

**在hive-site.xml中配置MySQL**

- #####  javax.jdo.option.ConnectionDriverName，将该name对应的value修改为MySQL驱动类路径：

```xml
<property
  <name>javax.jdo.option.ConnectionDriverName</name>
  <value>com.mysql.jdbc.Driver</value>
</property>  

```

- #####  javax.jdo.option.ConnectionURL，将该name对应的value修改为MySQL的地址：

```xml
 <name>javax.jdo.option.ConnectionURL</name>
 <value>jdbc:mysql://192.168.1.100:3306/metastore?createDatabaseIfNotExist=true</value>
```

- ##### javax.jdo.option.ConnectionUserName，将对应的value修改为MySQL数据库登录名：

```xml
<name>javax.jdo.option.ConnectionUserName</name>
<value>root</value>
```

- ##### javax.jdo.option.ConnectionPassword，将对应的value修改为MySQL数据库的登录密码：

```xml
<property>
  <name>javax.jdo.option.ConnectionPassword</name>
  <value>root@123</value>
  <description>password to use against metastore database</description>
</property>
```

- ##### 将MySQL驱动包上载到Hive的lib目录下

```shell
cp mysql-connector-java-5.1.48* /opt/module/apache-hive-3.1.2-bin/lib/
```

**2、更改log存放目录**

在/opt/module/apache-hive-3.1.2-bin 目录下新建logs 文件夹，将log存放在该目录下。

进入/opt/module/apache-hive-3.1.2-bin/conf 目录执行以下操作

```shell
cp hive-log4j2.properties.template hive-log4j2.properties
vim hive-log4j2.properties
```

更改如下内容：

```properties
property.hive.log.dir = /opt/module/apache-hive-3.1.2-bin/logs
```

如图所示

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/7a46cc2bd5b1cab61ad57f5e36773b5.png)

**3、新建hive-env.sh文件并进行修改**

```shell
cd /opt/moduleapache-hive-3.1.2-bin/conf
cp hive-env.sh.template hive-env.sh
vim hive-env.sh
```

修改内容如下：

```sh
HADOOP_HOME=/opt/module/hadoop-3.2.1
export HIVE_CONF_DIR=/opt/module/apache-hive-3.1.2-bin/conf
export HIVE_AUX_JARS_PATH=/opt/module/apache-hive-3.1.2-bin/lib
```



**启动hive**

```
schematool -dbType mysql -initSchema   #初始化，首次运行时需要进行初始化
hive                                   #启动hive
```



![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/6d59fcfba2c0ae1f8e1d19150b0bfd2.png)

**配置hive查表时何时以mapreduce任务的方式进行查询**

修改hive-site.xml 文件的内容。

```xml
<property>
    <name>hive.fetch.task.conversion</name>
    <value>more</value>
    <description>
      Expects one of [none, minimal, more].
      Some select queries can be converted to single FETCH task minimizing latency.
      Currently the query should be single sourced not having any subquery and should not have any aggregations or distincts (which incurs RS), lateral views and joins.
      0. none : disable hive.fetch.task.conversion
      1. minimal : SELECT STAR, FILTER on partition columns, LIMIT only
      2. more    : SELECT, FILTER, LIMIT only (support TABLESAMPLE and virtual columns)
    </description>
  </property>
```

**解释：** 根据描述可以看出配置 value 值 配置为more 

**配置hive显示当前所在数据库和配置查询字段时显示字段值对应的名称**

修改hive-site.xml 文件内容

```xml
 <property>
    <name>hive.cli.print.header</name>
    <value>true</value>
    <description>Whether to print the names of the columns in query output.</description>
  </property>
  <property>
    <name>hive.cli.print.current.db</name>
    <value>true</value>
    <description>Whether to include the current database in the Hive prompt.</description>
  </property>
```



**创建数据库和数据库表**

```
create database 数据库名；
create table t1(eid int, name string, sex string) row format delimited fields terminated by '\t';  # 创建数据库表以 \t 为行分隔符。 （默认为\t 分割。）

desc formatted t1;   # 看表t1 的详细信息。
```

**导入数据**

- 从本地导入

```
load data local inpath '文件路径' into table 数据库名.表名;
```

- 从HDFS导入

```
load data inpath 'HDFS路径' into table 数据库名.表名;
```

**hive的元数据库备份与还原**

元数据库丢了，hive就没办法用了。

常见错误：启动hive时，无法初始化metastore数据库，无法创建连接。

​	可能原因分析：

​		1.hive的metastore数据库丢失了，比如drop, 比如文件损坏。

​		2.metastore版本号不对。

​		3.远程表服务

- 备份：

```
mysqldump -uroot -p metastore > metastore.sql       #备份成sql文件。
```

- 还原：

```
mysql -uroot -p metastore < metastore.sql
```

**hive 操作hql语法的两个参数**

```
hive -e "命令"
hive -f '文件.hql'
```



**hive 中内部表外部表的概念**

- 内部表

删除表数据时，连同数据源以及元数据信息同时删除

- 外部表

1.只会删除元数据信息

2.共享数据，外部表相对而言更加方便和安全。

- 相同之处

如果导入数据时，操作于HDFS上，则会将数据进行迁移，并在metastore留下记录，而不是copy数据源。

**hive 分区表**



**问题1：**

启动hive 报以下错误。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200326103825.png)

问题原因：

该问题的意思是有多个SLF4J绑定，分别位于hadoop-3.2.1和hive-3.1.2目录下面。

解决方法：

```
cd /opt/module/apache-hive-3.1.2-bin/lib #进入hive的lib目录
mv log4j-slf4j-impl-2.10.0.jar log4j-slf4j-impl-2.10.0.jar.bak  #将此jar包改为.bak备份jar包。
```

**解释：**这样做的目的是删除log4j-slf4j-impl-2.10.0.jar这个jar包，之所以没有直接删除而是改为.bak 后缀，目的是以防删除后报其它错误。这样当报其它错误相关错误时，方便把修改变回来。

**问题2：**

启动hive报以下错误：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/a2dedc2a89752e9d52d8987db4f026c.png)

问题原因：

hadoop安装目录下的guava版本与hive安装目录下的guava版本不一致，下面是各自对应的版本。

guava-27.0-jre.jar  /opt/module/hadoop-3.2.1/share/hadoop/common/lib/

guava-19.0.jar       /opt/module/apache-hive-3.1.2-bin/lib/

解决方法：

将hive下较低版本的guava删除掉，替换为与hadoop安装目录下相同的guava版本。

https://stackoverflow.com/questions/58903865/i-installed-hadoop-3-2-1-and-top-of-hadoop-installed-hive-on-centos7-and-getting

**问题3：**

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200326145355.png)

问题原因：

hive-site.xml 的第3215行，96个字符为特殊字符。

解决方法：

删除文件中特殊字符即可。



![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200326150005.png)

**问题4**

执行  `hive`命令时 提示未初始化。

解决方法：进入hive下的bin目录，执行 `schematool -dbType mysql -initSchema`

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/6d59fcfba2c0ae1f8e1d19150b0bfd2.png)

**问题5**

当进入hive 或使用 show tables; 等命令时，打印好多INFO日志，看起来比较凌乱。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200326162703.png)

解决方法：为解决









## 8、安装Spark

**spark环境要求：**

jdk、Scala

**spark安装规划：**

100为主  101 102 为从

### 1、安装jdk

本集群安装的是jdk8，详细步骤在前面已经介绍。

### 2、安装并配置Scala

本集群安装的是 scala-2.12.11 版本。

**1、Scala下载地址**

所有可用Scala版本的下载地址： https://www.scala-lang.org/download/all.html

**注意安装的Scala版本和安装的jdk版本要兼容。**

**2、安装Scala**

将下载好的Scala解压到 `/opt/software` 目录下。

```
tar -zxvf /opt/software/scala-2.12.11.tgz  -C /opt/module/
```

**3、配置scala**

```
sudo vim /etc/profile
```

增加以下内容：

```
#SCALA_HOME
export SCALA_HOME=/opt/module/scala-2.12.11
export PATH=$PATH:$SCALA_HOME/bin
```

使环境变量生效

```
source /etc/profile
```

验证是否安装成功：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20200323095611.png)

### 3、安装spark

**1、下载spark**

官网下载地址：https://spark.apache.org/downloads.html

**注意：下载的spark 版本要和Hadoop版本匹配**

**2、安装spark**

将下载的spark解压到 `/opt/module`  目录。

```
tar -zxvf /opt/software/spark-3.0.0-preview2-bin-hadoop3.2.tgz -C /opt/module/
```

**3、配置spark**

（1）环境变量配置：

```
sudo vim /etc/profile
```

添加以下内容：

```
#SPARK_HOME
export SPARK_HOME=/opt/module/spark-3.0.0-preview2-bin-hadoop3.2
export PATH=$PATH:$SPARK_HOME/bin
export PATH=$PATH:$SPARK_HOME/sbin
```

使环境变量生效：

```
source /etc/profile
```

（2）对spark配置文件进行设置

```
cd /opt/module/spark-3.0.0-preview2-bin-hadoop3.2/conf
cp spark-env.sh.template spark-env.sh
cp slaves.template slaves
vim spark-env.sh
```

在spark-env.sh中添加以下内容:

```
export JAVA_HOME/usr/local/java/jdk1.8.0_231
export SCALA_HOME/opt/module/scala-2.12.11
export HADOOP_CONF_DIR/opt/module/hadoop-3.2.1/etc/hadoop
export SPARK_MASTER_IP=192.168.1.100
export SPARK_WORKER_CORES=2
export SPARK_WORKER_MEMORY=2g
```

在slave中增加以下内容

```
hadoop-slave101
hadoop-slave102
```

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/d841ad26ea9689f446dcf400cacad30.png)



## 9、启动大数据集群

100上执行(启动Hadoop)

```
start-dfs.sh
```

101上执行（启动Hadoop）

```
start-yarn.sh
```

100 101 102 上执行（启动zookeeper）

```
zkServer.sh start
```

100 上执行（启动hbase）

```
start-hbase.sh
```

100上启动spark-master

在spark的sbin目录执行

```
./start-master.sh
```

**问题1：**

当输入192.168.1.100:8080访问spark master 的web ui 时报错，使用192.168.1.101：8081访问spark worker时可以正常访问。

这里猜测是端口冲突。于是修改spark sbin 目录下的start-master.sh文件。将master的8080端口改为了8001。

修改完成后，重启spark，问题解决。

```
./stop-all.sh
./start-all.sh
```

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/511b378a946037b4e3ecc601386f869.png)