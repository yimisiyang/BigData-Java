# 1.Git基本使用 #
**1.设置用户名：**

    git config --global user.name "用户名"
**2.设置邮箱：**

    git config --global user。email "邮箱"
**3.初始化本地仓库：**

    git init
将远程仓库拷贝到本地

    git clone + 远程仓库的地址
**4.git 三个重要的分区：**

工作区、暂存区、版本库。工作时只是拉取其中的一个版本进行修改，修改完以后添加到暂存区。

**5.将当前修改版本放到暂存区（文件名代表一个文件，‘.’代表所有修改文件。）：**

    git add 文件名 / .

当有多个文件夹里的文件都进行了修改，此时只想添加其中一个文件夹里的所有文件到暂存区，具体操作如下。

    cd ./要提交的文件夹
    git add ./

或者

    git add ./要提交的文件夹/

**6.对当前修改添加文字说明并提交到版本库：**

    git commit -m "说明信息"

查看提交的日志信息

    git log

查看仓库状态

    git status

**7.将本地仓库和远程仓库关联并推送到远程仓库：**
**关联：**如我的远程仓库地址为https://github.com/yimisiyang/BigData-java.git

    git remote add origin https://github.com/yimisiyang/BigData-java.git

**推送到远程仓库：**

    git push -u origin master

注意： 在推送过程中出现的问题

![](https://i.imgur.com/ATWplew.png)

该问题是由于本地目录和远程目录不一致引起的。可以通过以下命令解决：

    git pull --rebase origin master

该命令是将远程仓库的文件拉下来覆盖本地文件，再次进行提交。

    git push -u origin master 
或者 

    git push origin master -f  //强制提交并覆盖远程仓库文件

# 2.git 分支冲突解决 #

master分支：主分支（即稳定的分支），一般由公司维护，我们一般在自己的分支上干活，等调试完成后，在合并到主分支。


**1.查看当前所在分支以及创建新的分支**
查看当前所在分支

    git branch

创建新的分支(例：创建新的分支名称为cxk)

    git branch cxk
或者

    git checkout -b cxk

两者区别在于用`git branch`创建完成后并不会切到新创建的分支；而采用`git checkout -b cxk`创建完成后会自动切换到新创建的分支。

**2.将子分支合并到master分支：**
（1）切换到主分支

    git checkout master

（2）查看是否切换成功

    git branch

（3）查看当前主分支上的文件，发现并没有子分支上刚刚修改的文件。

    ls

（4）将子分支合并到master主分支（例：这里合并的分支名为cxk）

    git merge cxk  

合并完成后再次用`ls`和`git log`查看是否合并成功和合并日志。

**3.分支冲突产生的原因：**
即在主分支和子分支上对同一个文件进行了修改，并且主分支和子分支都提交成功了。此时若将子分支合并到master主分支，这时会产生分支冲突。

解决方法：
在master分支上找到产生分支冲突的文件，将里面产生的冲突代码去掉后，再次提交。

# 3.删除github仓库中的某一个文件或文件夹 #
在github上只能删除仓库，无法删除文件夹或文件，因此只能通过命令来解决。
**1.将远程仓库里面的项目拉下来**

    git pull origin master

**2.查看分支上的上的文件夹和文件，并确定自己是不是在要修改的分支上**

    dir  //查看文件
    git branch  //查看所在分支

**3.开始手动删除文件和文件夹**

    rm -rf 文件/文件夹  //此时本地仓库中的文件/文件将被删除
 
**4.再次上传**

```
git add .
git commit -m "提示信息"
git push -u origin master
```

# 4.下载别人的仓库然后再上传到自己仓库时遇到的问题 #
问题如图：

![](https://i.imgur.com/QJU58yc.png)

**问题原因：**这是由于克隆别人库的时候引入了新的git库。git不知道这两个库的嵌套关系所导致的。

**解决方法：**
**方案一**
进入到下载别人库的目录下，将隐藏文件功能打开，删掉隐藏文件'.git'即可解决。

再次执行`git add .`命令后，进行状态查看`git status`发现成功将修改该的文件加入到了暂存区。

![](https://i.imgur.com/ktewfel.png)

接着执行`git commit -m "提示信息"`和`git push -u origin master`即可。

**方案二**
submodule 方式 ，有兴趣的可以自己研究。这里不再叙述。