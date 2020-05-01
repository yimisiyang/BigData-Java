#！/bin/bash
#--------对于鼎新项目计算节点有六块盘要挂载，存储节点有十块盘要挂载
#新建/dev/sda的磁盘标签类型为GPT
parted /dev/sda mklabel gpt

#将/dev/sda/整个空间分配给同一个分区
parted /dev/sda mkpart primary 0 100% ignore

#格式化分区
mkfs -t ext4 /dev/sda1

#---------------------------------------------------
#新建/dev/sdb的磁盘标签类型为GPT
parted /dev/sdb mklabel gpt

#将/dev/sda/整个空间分配给同一个分区
parted /dev/sdb mkpart primary 0 100% ignore

#格式化分区
mkfs -t ext4 /dev/sdb1

#---------------------------------------------------
#新建/dev/sdc的磁盘标签类型为GPT
parted /dev/sdc mklabel gpt

#将/dev/sda/整个空间分配给同一个分区
parted /dev/sdc mkpart primary 0 100% ignore

#格式化分区
mkfs -t ext4 /dev/sdc1

#---------------------------------------------------
#新建/dev/sdd的磁盘标签类型为GPT
parted /dev/sdd mklabel gpt

#将/dev/sda/整个空间分配给同一个分区
parted /dev/sdd mkpart primary 0 100% ignore

#格式化分区
mkfs -t ext4 /dev/sdd1

#---------------------------------------------------
#新建/dev/sde的磁盘标签类型为GPT
parted /dev/sde mklabel gpt

#将/dev/sda/整个空间分配给同一个分区
parted /dev/sde mkpart primary 0 100% ignore

#格式化分区
mkfs -t ext4 /dev/sde1

#---------------------------------------------------
#新建/dev/sdb的磁盘标签类型为GPT
parted /dev/sdf mklabel gpt

#将/dev/sda/整个空间分配给同一个分区
parted /dev/sdf mkpart primary 0 100% ignore

#格式化分区
mkfs -t ext4 /dev/sdf1

