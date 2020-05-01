# CentOS 上搭建trojan 实现科学上网

所需工具

- 一台vps（系统为CentOS7以上，最好是8。内核自动开启了bbr加速。）
- 一个域名（将vps的ip地址和域名绑定，具体方法是添加一条a记录。）
- 一键脚本

1.一键脚本

```
curl -O https://raw.githubusercontent.com/atrandys/trojan/master/trojan_centos7.sh && chmod +x trojan_centos7.sh && ./trojan_centos7.sh
```

安装过程中需要输入VPS域名：如图。

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/d7f3e5c1070f162b5dc55d807b639dc.png)

安装完成如图所示：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/521b08ca658e85445369fa532afeaf9.png)

2.搭建完成后需要在浏览器中安装switchomega插件，这里不再累述。