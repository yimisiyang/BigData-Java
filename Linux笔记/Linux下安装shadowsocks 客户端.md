# Linux下安装shadowsocks 客户端

## 1.安装准备

- Ubuntu18.04（该系统运行在VMWare 虚拟机下，网络连接方式为NAT）

## 2.root用户下执行以下命令。

**Ubuntu：**

```
apt-get update
apt-get upgrade
apt-get install python3-pip
pip install shadowsocks
```

**CentOS:**

```
yum install python-setuptools && easy_install pip
pip install shadowsocks
```



注释：**执行完上面两条命令后，shadowsocks-2.8.2版本便安装好了。

可以执行 `sslocal -h` 查看帮助文档，以及 `whereis sslocal`查看 shadowsocks的安装位置。

## 3.编写配置文件shadowsocks_config.json

```
vim /home/cxk/shadowsocks_config.json
```

在新建的.json文件中增加以下内容

```
{
  "server":"my_server_ip",
  "local_address": "127.0.0.1",
  "local_port":1080,
  "server_port":my_server_port,
  "password":"my_password",
  "timeout":300,
  "method":"aes-256-cfb"
}
```

- `my_server_ip`改为自己的服务器IP
- `my_server_port`改为自己的服务器端口
- `my_server_password`改为自己的密码
- `method`的值改为自己的加密方式，一般是`aes-256-cfb`或者`rc4-md5`

**将文件修改完成后保存退出**

详细配置说明：

|     Name      | 说明                                            |
| :-----------: | :---------------------------------------------- |
|    server     | 服务器地址，填ip或域名                          |
| local_address | 本地地址                                        |
|  local_port   | 本地端口，一般1080，可任意                      |
|  server_port  | 服务器对外开的端口                              |
|   password    | 密码，可以每个服务器端口设置不同密码            |
| port_password | server_port + password ，服务器端口加密码的组合 |
|    timeout    | 超时重连                                        |
|    method     | 默认: “aes-256-cfb”，其它加密方式自行百度。     |
|   fast_open   | 开启或关闭（true/false）                        |

**解释：配置文件自己找地方放就好了，一般放在/etc下面。因为我经常需要修改，因此我放在了/home目录下了。**

## 4.启动sslocal客户端

**命令：**

```
sslocal -c /home/cxk/shadowsocks.json -d start    #启动
sslocal -c /home/cxk/shadowsocks.json -d stop     #停止
sslocal -c /home/cxk/shadowsocks.json -d restart  #重启
```

**出现问题1**

执行启动命令后出现如下问题：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/48661947fed281767f05ada16e49764.png)

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/d5903de36106549f9828198ca5847bb.png)



**问题原因：**

这个问题是由于在openssl1.1.0版本中，废弃了EVP_CIPHER_CTX_cleanup函数。

**解决方法：**

用vim打开文件：vim /usr/local/lib/python3.6/dist-packages/shadowsocks/crypto/openssl.py (该路径请根据自己的系统情况自行修改，如果不知道该文件在哪里的话，可以使用find命令查找文件位置)

进入编辑模式
将第52行**libcrypto.EVP_CIPHER_CTX_cleanup.argtypes = (c_void_p,)**
改为**libcrypto.EVP_CIPHER_CTX_reset.argtypes = (c_void_p,)**
再次搜索cleanup（全文件共2处，此处位于111行），将**libcrypto.EVP_CIPHER_CTX_cleanup(self._ctx)**
改为**libcrypto.EVP_CIPHER_CTX_reset(self._ctx)**

**修改完成后保存退出**

再次执行命令 , sslocal可以实现正常启动。

```
sslocal -c /home/cxk/shadowsocks.json -d start
```

## 5.开启系统代理

在Linux系统中设置全局代理

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/image-20200111165931705.png)

**注释：**全局代理方式连接国内网站时可能速度较慢，因此我们可以采用下面这种方式。

在chrome浏览器或者Firefox浏览器中安装SwitchyOmega 插件。

**配置 Proxy**

- `Server`填写`shadowsocks.json`配置中的`local_address`
- `Port`填写`shadowsocks.json`配置中的`local_port`
- 左边`Apply changes`保存。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/SwitchyOmegaProxy.png)

**配置 Auto Switch**

添加要使用代理的网站和不使用代理的网站。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/SwitchyOmegaAutoSwitch.png)

**启用 SwitchyOmega**

启用 SwitchyOmega 插件，选择 Auto Switch 模式就可以了。

**注释：**此时在浏览器中便可以愉快的Google了，但是在终端使用ping www.google.com 依然无法ping通，因为不影响使用所以这里就先不折腾了。

**---------------------若以上步骤均无误，但是依然无法Google，可以查看防火墙是否处于开启状态，将防火墙关闭或者添加你要允许端口的规则。**

## 6.设置开机自启

若每次开机都需要启动sslocal比较麻烦，可以设置成开机自启。

设置方法（root用户）：

```
vim /etc/systemd/system/shadowsocks.service
```

在里面填写如下内容：

```
[Unit]
Description=Shadowsocks Client Service
After=network.target
After=network-online.target

[Service]
#Type=simple
Type=forking
User=root
ExecStart=/usr/local/bin/sslocal -c /home/xx/shadowsocks_config.json -d start

[Install]
WantedBy=multi-user.target
```

**解释**

- 把`/home/xx/Software/ShadowsocksConfig/shadowsocks_config.json`修改为你的`shadowsocks.json`路径，如：`/etc/shadowsocks_config.json`
- Service模块的Type必须使用forking，因为指令`/usr/local/bin/sslocal -c /home/xx/shadowsocks_config.json -d start`执行完后不会一直运行，创建完守护线程后很快会退出，最后Service发现指令已经执行完，于是service就也退出了。但是sslocal这时的守护线程是挂载在service上的，所以service退出，守护线程立马也kill了。这就导致最终你的sslocal没开启，就和走了一次片场一样。所以必须要规定Type=forking，因为forking模式下，Service会将自己的所有守护线程移交给os，那就没问题了，sslocal会在os下继续运行！

配置生效：

```
systemctl enable /etc/systemd/system/shadowsocks.service
```

输入管理员密码就可以了。

现在你可以马上重启试试，或先在后台启动，等下次重启再看看！



**有关Systemd管理的指令介绍**

1. 当你因为某些原因要修改shadowsocks.service的内容，比如字母打错了。修改完后必须调用`systemctl enable /etc/systemd/system/shadowsocks.service`来使其重新生效
2. sslocal提供log打印到文件的功能，使用-help查看具体帮助就可以翻阅到如何使用了
3. 启动（关闭）service服务，使用指令`systemctl start(stop) shadowsocks.service`