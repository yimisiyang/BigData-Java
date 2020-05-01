在windows和Linux 终端中设置代理

# 1.Windows命令行代理

**假设已经安装了ss客户端，本地socks代理为127.0.0.1:1080**

## 1.1设置代理

在CMD窗口输入如下指令设置代理：

```
set http_proxy=socks5://127.0.0.1:1080
set https_proxy=socks5://127.0.0.1:1080
set ftp_proxy=socks5://127.0.0.1:1080
```

测试：(能得到以下返回结果)

```
curl http://www.google.com
```

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/cxk.jpg)

## 1.2取消代理命令

```
set http_proxy=
set https_proxy=
set ftp_proxy=
```

**解释：**设置代理后只对当前命令窗口生效，重新打开CMD需要再次设置代理。

# 2.Linux终端代理设置方式

由于Linux下SS客户端仅代理socks5协议的流量（如果不是这个原因恳请指正）。所以想在LX终端使用代理，需要在SS的socks5流量前再接一个代理，允许http、https、ftp协议流量通过。

**我们也假定本地socks5代理为127.0.0.1:1080**

## 2.1安装polipo

### 2.1.1Debain下直接使用apt命令安装：

```
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install polipo
```

### 2.1.2 编辑配置文件：

```
sudo vim /etc/polipo/config
```

**在配置文件中添加以下内容**

```
# This file only needs to list configuration variables that deviate
# from the default values.  See /usr/share/doc/polipo/examples/config.sample
# and "polipo -v" for variables you can tweak and further information.

logSyslog = true
logFile = /var/log/polipo/polipo.log

proxyAddress = "0.0.0.0"

socksParentProxy = "127.0.0.1:1080"
socksProxyType = socks5

chunkHighMark = 50331648
objectHighMark = 16384

dnsQueryIPv6 = no
```

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/334b427d3c0d7c04dbe16299354c911.png)

**重启polipo服务**

```
service polipo restart
```

## 2.2启用代理

通过 `service polipo status` 命令，我们可以看到新的监听端口为**8123**。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/05bc5358d448f2249c2c0a99989cb32.png)

在Linux终端下启用代理的命令为：

```
export http_proxy=http://127.0.0.1:8123
export https_proxy=http://127.0.0.1:8123
export ftp_proxy=http://127.0.0.1:8123
```

**解释：**同样，直接输入上述命令设置的代理也是临时的，一个比较实用的方法是在~/.bashrc文件中设置环境，之后就不需要再手动设置了。

```
sudo vim ~/.bashrc
```

在文件最后插入上述三条指令，保存退出。

## 2.3 使用wget命令测试

```
wget www.google.com
```

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/7de8f9f3387a077298c0c9fd5fa4311.png)

**出现上述界面代表连接成功。**

# 3.小结

我对CMD/LX终端设置代理的出发点，是为了使用pip命令安装Google的某个包，设置后确实能够成功使用。本文档也是结合网上资源整理，具体原理也不是很懂，如有错误，欢迎大家批评指正。共同进步。