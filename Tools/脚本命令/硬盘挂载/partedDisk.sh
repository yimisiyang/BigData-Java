#��/bin/bash
#--------���ڶ�����Ŀ����ڵ���������Ҫ���أ��洢�ڵ���ʮ����Ҫ����
#�½�/dev/sda�Ĵ��̱�ǩ����ΪGPT
parted /dev/sda mklabel gpt

#��/dev/sda/�����ռ�����ͬһ������
parted /dev/sda mkpart primary 0 100% ignore

#��ʽ������
mkfs -t ext4 /dev/sda1

#---------------------------------------------------
#�½�/dev/sdb�Ĵ��̱�ǩ����ΪGPT
parted /dev/sdb mklabel gpt

#��/dev/sda/�����ռ�����ͬһ������
parted /dev/sdb mkpart primary 0 100% ignore

#��ʽ������
mkfs -t ext4 /dev/sdb1

#---------------------------------------------------
#�½�/dev/sdc�Ĵ��̱�ǩ����ΪGPT
parted /dev/sdc mklabel gpt

#��/dev/sda/�����ռ�����ͬһ������
parted /dev/sdc mkpart primary 0 100% ignore

#��ʽ������
mkfs -t ext4 /dev/sdc1

#---------------------------------------------------
#�½�/dev/sdd�Ĵ��̱�ǩ����ΪGPT
parted /dev/sdd mklabel gpt

#��/dev/sda/�����ռ�����ͬһ������
parted /dev/sdd mkpart primary 0 100% ignore

#��ʽ������
mkfs -t ext4 /dev/sdd1

#---------------------------------------------------
#�½�/dev/sde�Ĵ��̱�ǩ����ΪGPT
parted /dev/sde mklabel gpt

#��/dev/sda/�����ռ�����ͬһ������
parted /dev/sde mkpart primary 0 100% ignore

#��ʽ������
mkfs -t ext4 /dev/sde1

#---------------------------------------------------
#�½�/dev/sdb�Ĵ��̱�ǩ����ΪGPT
parted /dev/sdf mklabel gpt

#��/dev/sda/�����ռ�����ͬһ������
parted /dev/sdf mkpart primary 0 100% ignore

#��ʽ������
mkfs -t ext4 /dev/sdf1

