# 在Google cloud 下安装ssr（本教程所选操作系统为ubuntu 18.04） #
## (1)  ssh 进远程服务器 ##
    ssh root@ip
初次登录最好使用passwd 命令修改登录密码
    `passwd`
## (2)执行代码(安装过程中对ssr进行配置)。 ##

**Ubuntu执行：**

    wget --no-check-certificate -O shadowsocks-all.sh https://raw.githubusercontent.com/teddysun/shadowsocks_install/master/shadowsocks-all.sh
    chmod +x shadowsocks-all.sh
    ./shadowsocks-all.sh 2>&1 | tee shadowsocks-all.log
**CentOS7/8执行：**

**CentOS 7/8系统ShadowsocksR/SSR一键脚本：centos_install_ssr.sh**

```
bash <(curl -sL https://raw.githubusercontent.com/hijkpw/scripts/master/centos_install_ssr.sh)
```

1. 如果脚本执行过程中断线了，没有关系，重新连接然后运行`bash <(curl -sL https://raw.githubusercontent.com/hijkpw/scripts/master/centos_install_ssr.sh)`命令就可以了。

2. 查看程序是否运行请用命令：`netstat -ntlp | grep 端口号`（端口号替换成你设置的端口号）。SSR的配置文件在`/etc/shadowsocksR.json`，命令行查看配置请用命令：`cat /etc/shadowsocksR.json`。如果你修改了配置文件，请用命令： `systemctl restart shadowsocksR` 重启程序。
3. 可以通过以下命令卸载SSR程序。

```
bash <(curl -sL https://raw.githubusercontent.com/hijkpw/scripts/master/centos_install_ssr.sh) uninstall
```



**CentOS 7/8系统Shadowsocks/SS一键脚本：centos_install_ss.sh**

```
bash <(curl -sL https://raw.githubusercontent.com/hijkpw/scripts/master/centos_install_ss.sh)
```

**解释：**

1. 可以通过以下命令卸载SS

```
bash <(curl -sL https://raw.githubusercontent.com/hijkpw/scripts/master/centos_install_ss.sh) uninstall
```

​    2.查看程序是否运行请用命令：`netstat -ntlp | grep ss-server`。SS的配置文件在`/etc/shadowsocks-libev/config.json`，命令行查看配置请用命令：`cat /etc/shadowsocks-libev/config.json`。如果你修改了配置文件，请用命令： `systemctl restart shadowsocks-libev` 重启程序。

   3.如果脚本执行过程中断线了，没有关系，重新连接然后运行`bash <(curl -sL https://raw.githubusercontent.com/hijkpw/scripts/master/centos_install_ss.sh)`命令就可以了。

**CentOS 7/8系统V2Ray一键脚本：centos_install_v2ray.sh**

```
bash <(curl -sL https://raw.githubusercontent.com/hijkpw/scripts/master/centos_install_v2ray.sh)
```

V2ray一键脚本做了如下事情：

1. 更新系统到最新版
2. 安装bbr加速模块
3. 安装了迷惑性的nginx软件，通过80端口可以直接访问
4. 交互式安装v2ray

**解释：**

​	1.如果脚本执行过程中断线了，没有关系，重新连接然后运行上面的安装命令就可以了。

2. 查看程序是否运行请用命令：`netstat -ntlp | grep v2ray`。v2ray的配置文件在`/etc/v2ray/config.json`，命令行查看配置请用命令：`cat /etc/v2ray/config.json`，也可以用bitvise下载下来看。如果你修改了配置文件，请用命令： `systemctl restart v2ray` 重启程序。

**CentOS 7/8系统带伪装V2Ray一键脚本：centos_install_v2ray2.sh**

```
bash <(curl -sL https://raw.githubusercontent.com/hijkpw/scripts/master/centos_install_v2ray2.sh)
```

按回车键，屏幕上开始滚动各种看得懂看不懂的东西。紧盯着屏幕，直到屏幕出现“**确认满足按y，按其他退出脚本：**”，确认条件满足，按y回车，然后**输入你域名的主机名**（注意是主机名，比如www.hijk.pw，**最好不要填裸域名**hijk.pw！），以及设置一个**伪装路径（不能是/）**，例如/abcedf（强烈建议设置一个复杂的、别人猜不到的路径）。接下来脚本会自动疯狂运行，直到屏幕上出现安装成功的提示。**如果安装过程卡住，请耐心等待几分钟；如果期间网络断开（windows上表现为黑框框中或者顶部标题出现disconnected字样，mac表现为终端出现“closed by remote host”或”broken pipe”），请重新连接后再次执行命令**。脚本运行成功会输出配置信息。

**到此服务端配置完毕**，服务器可能会自动重启，windows终端出现“disconnected”，mac出现“closed by remote host”说明服务器成功重启了，**如果没提示重启则不需要**。

1. 多次运行一键脚本，安装过程中会出现如下提示：

```
What would you like to do?
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
1: Keep the existing certificate for now
2: Renew & replace the cert (limit ~5 per 7 days)
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Select the appropriate number [1-2] then [enter] (press 'c' to cancel):
```

**输入1，回车即可。**

2. 如果提示证书失败，终端出现如下提示：

```
An unexpected error occurred:
There were too many requests of a given type :: Error creating new order :: too many certificates already issued for exact set of domains:test2.hijk.pw: see https://letsencrypt.org/docs/rate-limits/
```

说明这个主机名近期申请过太多次免费证书，请换一个主机名尝试，例如test2.hijk.pw 换成 test3.hijk.pw（需要到dns控制台添加解析）。

**3. 如何判断服务端已经正常运行？**操作如下：

1. 浏览器输入域名，打开的是一个比较精美的模板站；

2. 输入域名加伪装路径，出现bad request。

如果这两个现象都是预期的，说明服务端一切正常，有问题请检查客户端配置。

## (3)安装bbr加速。 ##

    wget --no-check-certificate https://github.com/teddysun/across/raw/master/bbr.sh
## (4)更改bbr权限 ##

    chmod +x bbr.sh ./bbr.sh
或者安装魔改版bbr加速

一键脚本

```
wget -N --no-check-certificate "https://raw.githubusercontent.com/chiakge/Linux-NetSpeed/master/tcp.sh" && chmod +x tcp.sh && ./tcp.sh
```

若重启系统，可以执行当前目录下的 `tcp.sh`再次执行安装。



## (5)查看bbr是否启动成功(如果bbr变成绿色证明启动成功)

    ls -la
![](https://i.imgur.com/6r5GOa3.png)
## (6)重启服务器 ##
    reboot
##(7)在Google cloud platform VM实例中添加防火墙规则##
![](https://i.imgur.com/PMxW0xE.png)
添加的规则如下图所示（注意：图中开放的tcp/udp端口与你ssr中设置的端口要保持一致 ）
![](https://i.imgur.com/OS9EzKd.png)