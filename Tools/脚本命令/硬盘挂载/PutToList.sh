#!/bin/bash
#----------------------
#�趨����label
e2label /dev/sda1 /data1

#��������Ŀ¼
mkdir /data1

#��ʱ����
mount /dev/sda1 /data1

#�����Զ����أ����ù��أ�
echo '/dev/sda1 /data1                           ext4   defaults                0 0'>> /etc/fstab

#----------------------
#�趨����label
e2label /dev/sdb1 /data2

#��������Ŀ¼
mkdir /data2

#��ʱ����
mount /dev/sdb1 /data2

#�����Զ����أ����ù��أ�
echo '/dev/sdb1 /data2                           ext4   defaults                0 0'>> /etc/fstab

#----------------------
#�趨����label
e2label /dev/sdc1 /data3

#��������Ŀ¼
mkdir /data3

#��ʱ����
mount /dev/sdc1 /data3

#�����Զ����أ����ù��أ�
echo '/dev/sdc1 /data3                           ext4   defaults                0 0'>> /etc/fstab 

#----------------------
#�趨����label
e2label /dev/sdd1 /data4

#��������Ŀ¼
mkdir /data4

#��ʱ����
mount /dev/sdd1 /data4

#�����Զ����أ����ù��أ�
echo '/dev/sdd1 /data4                           ext4   defaults                0 0'>> /etc/fstab

#----------------------
#�趨����label
e2label /dev/sde1 /data5

#��������Ŀ¼
mkdir /data5

#��ʱ����
mount /dev/sde1 /data5

#�����Զ����أ����ù��أ�
echo '/dev/sde1 /data5                           ext4   defaults                0 0'>> /etc/fstab

#----------------------
#�趨����label
e2label /dev/sdf1 /data6

#��������Ŀ¼
mkdir /data6

#��ʱ����
mount /dev/sdf1 /data6

#�����Զ����أ����ù��أ�
echo '/dev/sdf1 /data6                           ext4   defaults                0 0'>> /etc/fstab