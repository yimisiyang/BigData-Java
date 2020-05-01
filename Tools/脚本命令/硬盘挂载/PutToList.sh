#!/bin/bash
#----------------------
#设定分区label
e2label /dev/sda1 /data1

#创建挂载目录
mkdir /data1

#临时挂载
mount /dev/sda1 /data1

#开机自动挂载（永久挂载）
echo '/dev/sda1 /data1                           ext4   defaults                0 0'>> /etc/fstab

#----------------------
#设定分区label
e2label /dev/sdb1 /data2

#创建挂载目录
mkdir /data2

#临时挂载
mount /dev/sdb1 /data2

#开机自动挂载（永久挂载）
echo '/dev/sdb1 /data2                           ext4   defaults                0 0'>> /etc/fstab

#----------------------
#设定分区label
e2label /dev/sdc1 /data3

#创建挂载目录
mkdir /data3

#临时挂载
mount /dev/sdc1 /data3

#开机自动挂载（永久挂载）
echo '/dev/sdc1 /data3                           ext4   defaults                0 0'>> /etc/fstab 

#----------------------
#设定分区label
e2label /dev/sdd1 /data4

#创建挂载目录
mkdir /data4

#临时挂载
mount /dev/sdd1 /data4

#开机自动挂载（永久挂载）
echo '/dev/sdd1 /data4                           ext4   defaults                0 0'>> /etc/fstab

#----------------------
#设定分区label
e2label /dev/sde1 /data5

#创建挂载目录
mkdir /data5

#临时挂载
mount /dev/sde1 /data5

#开机自动挂载（永久挂载）
echo '/dev/sde1 /data5                           ext4   defaults                0 0'>> /etc/fstab

#----------------------
#设定分区label
e2label /dev/sdf1 /data6

#创建挂载目录
mkdir /data6

#临时挂载
mount /dev/sdf1 /data6

#开机自动挂载（永久挂载）
echo '/dev/sdf1 /data6                           ext4   defaults                0 0'>> /etc/fstab