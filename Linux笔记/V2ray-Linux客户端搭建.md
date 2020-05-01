# linux下搭建v2ray服务端和客户端

由于ssr的越来越不稳定，所以拥抱v2ray成为一个不错的选择。

## 服务端

> 这里假设你已经[购买](https://links.jianshu.com/go?to=https%3A%2F%2Fwww.vultr.com%2F%3Fref%3D7222423)了vps(`centos7`)以及能ssh登录，服务端的操作均在此vps上。

1. 通过一键安装脚本安装



```cpp
%一路默认就行
yum update -y && yum install -y curl && bash <(curl -L -s https://install.direct/go.sh)
```

- 备注：v2ray 是通过systemctl来管理的，通过`systemctl start v2ray`来启动服务端的服务。

1. 安装bbr

在centos上的v2ray默认是没有bbr加速的，如果不安装的话会非常慢。v2ray已经提供了安装脚本，只需执行 `v2ray bbr`,按照提示安装就行。



```css
[root@vultr ~]# v2ray bbr

 1. 安装 BBR

 2. 安装 LotServer(锐速)

 3. 卸载 LotServer(锐速)

请选择 [1-3]:1
```

3.开放端口，这里假设用的63064，然后重启



```csharp
firewall-cmd --zone=public --add-port=63064/tcp --permanent
```

4.查看v2ray配置



```csharp
[root@vultr ~]# v2ray 


........... V2Ray 管理脚本 v3.13 by 233v2.com ..........

## V2Ray 版本: v4.20.0  /  V2Ray 状态: 正在运行 ##

  ...

  1. 查看 V2Ray 配置

  2. 修改 V2Ray 配置

  ...

温馨提示...如果你不想执行选项...按 Ctrl + C 即可退出

请选择菜单 [1-11]:1
---------- V2Ray 配置信息 -------------

 地址 (Address) = 149.28.**.**

 端口 (Port) = 63064

 用户ID (User ID / UUID) = *****-42a6-4e42-9f5b-1779eae25a3d

 额外ID (Alter Id) = 233

 传输协议 (Network) = tcp

 伪装类型 (header type) = none

---------- END -------------
```

当端口开放之后，记着配置，然后在本机上配置客户端

## 客户端-linux

> 同样的，这里介绍linux下的客户端配置过程

1. 安装客户端
   - 如果你之前使用的ssr，要将其关闭，不然1080端口会冲突。
   - 获取一键安装脚本
      `curl -L -s https://install.direct/go.sh`
   - 由于该脚本默认执行时，需要访问国外的服务器下载`v2ray-linux-64.zip`文件，如果不走代理肯定会下载失败。这里我通过坚果云分享给大家[坚果云](https://links.jianshu.com/go?to=https%3A%2F%2Fwww.jianguoyun.com%2Fp%2FDe0ZE0UQsOSjBxjLm_QB)
   - 下载完成后，把`go.sh`与`v2ray-linux-64.zip`放在同一个文件夹，在终端下执行
      `sudo bash go.sh --local ./v2ray-linux-64.zip`
2. 配置客户端
   - `sudo gedit /etc/v2ray/config.json`，将以下内容粘贴进去，注意要按照服务端的配置信息修改对应的位置。



```json
{
  "inbounds": [{
    "port": 1080,  // SOCKS 代理端口，在浏览器中需配置代理并指向这个端口
    "listen": "127.0.0.1",
    "protocol": "socks",
    "settings": {
      "udp": true
    }
  }],
  "outbounds": [{
    "protocol": "vmess",
    "settings": {
      "vnext": [{
        "address": "149.28.**.**", // 服务器地址，请修改为你自己的服务器 ip 或域名
        "port": 63064,  // 服务器端口
        "users": [{ "id": "*****-42a6-4e42-9f5b-1779eae25a3d" }]//要与服务端保持一致
      }]
    }
  },{
    "protocol": "freedom",
    "tag": "direct",
    "settings": {}
  }],
  "routing": {
    "domainStrategy": "IPOnDemand",
    "rules": [{
      "type": "field",
      "ip": ["geoip:private"],
      "outboundTag": "direct"
    }]
  }
}
```

- 这样，客户端就配置好了。使用`sudo service v2ray restart`,重启服务。

1. 安装SwitchyOmga
    这是一个浏览器插件，用于修改浏览器访问internet时的代理。具体怎么配置的不是这里的重点，就不讲了。

