# 一、sqoop简介 #

## 1.介绍 ##

Sqoop是一种在hadoop与关系型数据库（例如：mysql、Oracle）或大数据集群（HDFS）之间传输数据的工具，在Hadoop MapReduce中转换数据，然后将数据导出到关系型数据库（RDBMS）。
下面将介绍如何使用Sqoop在数据库和Hadoop之间或大数据集群到Hadoop之间迁移数据。

## 2.先决条件 ##

**学习Sqoop应具有以下基本知识**



- 基本电脑知识和术语。
- 熟系命令行工具例如bash.
- 熟系关系型数据库管理系统。
- 基本了解Hadoop的运行过程。

## 3.基础使用 ##

使用Sqoop，可以从关系型数据库或者大数据集群中导入数据到HDFS。导入过程输入数据是数据库表或者大型数据集。导出过程是包含导入数据库表或者数据集副本的一系列文件。导入过程是并行执行的，因此输出为多种文件。这些文件可能是被逗号或者tabs分割的文本文件、也可能是二进制文件、也可能是包含序列化记录数据的SequenceFiles(hadoop中存储二进制key-value对的文件)。

## 4.sqoop中常用的命令 ##

    sqoop help

用法：`sqoop COMMAND [ARGS]`

**常用命令：**

- `codegen` 生成代码和数据库记录进行交互。

- `create-hive-table` 将表的定义导入到Hive。
 
- `eval` 评估一条SQL语句并显示出结果。

- `export` 导出HDFS目录到数据库表。
 
- `help` 列出可用的sqoop命令。
- 
- `import` 将一个表从数据库导入到HDFS。
 
- `import-all-tables` 将所有表从数据库导入到HDFS。
 
- `import-mainframe` 将集群中的数据集导入到HDFS。
 
- `list-databases` 列出服务器上所有可用的数据库。
 
- `list-tables` 列出数据库中所有的数据表。

- `version` 显示版本信息。

## 5.使用通用参数和特定参数 ##

` sqoop help import`

**用法：** `sqoop import [GENERIC-ARGS] [TOOL-ARGS]`

```
常用参数:
   --connect <jdbc-uri>     Specify JDBC connect string
   --connect-manager <class-name>     Specify connection manager class to use
   --driver <class-name>    Manually specify JDBC driver class to use
   --hadoop-mapred-home <dir>      Override $HADOOP_MAPRED_HOME
   --help                   Print usage instructions
   --password-file          Set path for file containing authentication password
   -P                       Read password from console
   --password <password>    Set authentication password
   --username <username>    Set authentication username
   --verbose                Print more information while working
   --hadoop-home <dir>     Deprecated. Override $HADOOP_HOME

[...]

通用Hadoop命令行参数:
(must preceed any tool-specific arguments)
Generic options supported are
-conf <configuration file>     specify an application configuration file
-D <property=value>            use value for given property
-fs <local|namenode:port>      specify a namenode
-jt <local|jobtracker:port>    specify a job tracker
-files <comma separated list of files>    specify comma separated files to be copied to the map reduce cluster
-libjars <comma separated list of jars>    specify comma separated jar files to include in the classpath.
-archives <comma separated list of archives>    specify comma separated archives to be unarchived on the compute machines.

The general command line syntax is
bin/hadoop command [genericOptions] [commandOptions]
```
**注意：** 通用参数以单破折号（-）开头，非单字符的特定参数以双破折号（--）开头，单字符的特定参数以单破折号（-）开头。

# 二、sqoop工具的使用 #

**sqoop中所有工具的共用参数**

    `--connect <jdbc-uri>` 指定一个jdbc连接字符串。

    `--connection-manager <class-name>` 指定要使用的连接管理器类。

    `--driver <class-name>` 手动添加要使用的jdbc驱动程序类。

    `--hadoop-mapred-home <dir>` 覆盖$HADOOP_MAPRED_HOME的$PATH路径。

	`--help` 打印帮助信息

	`--password-file` 从文件中读取所使用用户名的密码。

	`-P` 从控制台读取密码。

	`--password <password>` 读取密码。

	`--username <username>` 读取用户名

	`--verbose` 打印详细的执行过程信息。

	`--connection-param-file <filename>` 可选，指定存储数据库连接参数的属性文件

	`--relaxed-isolation` 将连接事务隔离设置为读取未提交的映射器。

## 1.sqoop-import使用方法 ##

**作用：**将关系型数据库导中的一张表导入到HDFS。关系型数据库表中的每一行在HDFS中都被单独记录。记录形式有文本文件形式，二进制形式如Avro，SequenceFile形式。

**用法：**

- **连接一个数据库服务器**

 `sqoop import --connect jdbc:mysql://database.example.com/employees --username venkatesh -P passwdord`
	
	数据库名：employees、主机名：database.example.com、 用户名：venkatesh、密码：password。Sqoop提供多种读取密码方式这里-P参数是从控制台读取。其它读取方式可参考官方文档。

	如果使用Sqoop和Hadoop分布式集群连接，则不适用本地主机的URL。则应该使用所有远程节点都可以看到的主机名或IP地址。

- **import常用参数参数说明（导入HDFS）**

	`--append` 将数据追加到hdfs中已经存在的dataset中。

    `--as-avrodatafile` 将数据导入到一个Avro数据文件中。

    `--as-sequencefile` 将文件导入到sequence文件中

    `--as-textfile` 将数据导入到一个普通文本文件中，生成该文本文件后，可以在Hive中通过SQL语句查询结果。

    `--columns <col,col,col...>` 指定要导入的字段值。如：-columns id,username。

    `--delete-target-dir` 删除导入目标目录（如果存在）。

    `--direct` 直接导入模式，使用关系数据库自带的导入导出工具。官网说这样更快。

    `--fetch-size <n>` 一次要从数据库中读取的数据条目数。

    `--inline-lob-limit <n>` 设定大对象数据类型的最大值。

    `-m，--num-mappers <n>` 启动n个map并行导入数据，默认为4，设置的数字不要高于集群的节点数。

    `-e，--query <statement>` 从查询结果中导入数据，该参数使用时必须指定--target-dir、--hive-table在查询语句中应包含where条件且在where条件中需要包含$CONDITIONS,例如：`--target-dir /user/hive/warehouse/person --hive-table person`

    `--split-by <column-name>` 该表的列用来拆分工作单元，一半后跟主键ID，不能与`--autoreset-to-one-mapper`参数连用。
	
	`--split-limit <n>` 每个切分快大小的上限，仅适用于Integer和Date列，对于date或时间戳计算单位为秒。

	`--autoreset-to-one-mapper` 如果表即没有主键也没有提供分隔列则使用mapper导入，不可以和`--split-by <col>` 参数一起使用。

    `--table <table-name>` 关系数据库表名，数据从该表中获取。

	`--target-dir <dir>` 指定HDFS的路径。

	`--warehouse-dir <dir>` 不能与`--target-dir`参数同时使用，指定数据导入的目录适用于HDFS导入，不适合导入hive目录。

	`--where <where clause>` 导入时使用where子句。例：--where 'id = 2'

    `--z，--compress` 压缩参数，默认情况下数据是没有被压缩的，通过该参数可以使用gzip压缩算法对数据进行压缩，适用于text文本文件、Avro文件和SequenceFile。

    `--compression-codec <c>` 使用Hadoop编解码默认gzip。

    `--null-string <null-string>` 当字符串列为空时写入的值，默认为null。
    `--null-non-string <null-string>` 非字符串列为空时写入的值，默认为null。

- **输出行格式参数(公共参数import)**

	`--enclosed-by <>` 给字段值前后加上指定的字符。

	`--escaped-by <char>` 设置转义字符。

	`--fields-terminated-by <char>` 设定每个字段以什么符号结尾，默认为逗号。

	`--lines-terminated-by <char>` 设定每行之间的分隔符，默认为\n。

	`--mysql-delimiters` Mysql默认的分隔符设置，字段之间以逗号分隔，行之间以\n分隔，默认转义符为\,字段值以单引号包裹。配合`--direct`参数，可以实现非常快的导入。

	`--optionally-enclosed-by <char>` 给带有双引号或单引号的字段值前后加上指定字符。

- **输入解析参数（公共参数export）**

	`--input-enclosed-by <char>` 对字段值前后加上指定字符

	`--input-escaped-by <char>` 对含有转义字符的字段进行转义处理

	`--input-fields-terminated-by <char>` 字段之间的分隔符

	`--input-lines-terminated-by <char>` 行之间的分隔符

	`--input-optionally-enclosed-by <char>` 给带有双引号或单引号的字段前后加指定字符

	

- **将数据导入到Hive**

	**导入Hive常用参数**

    `hive-home <dir>` Hive的安装目录，如果有多个版本hive或者hive没在$PATH中，可以通过该参数指定。

    `--hive-import` 将表导入到hive中，sqoop将传递该字段并将记录定界符传递给hive。在使用时如果未指定定界符则使用hive默认的定界符。

    `--hive-overwrite` 覆盖hive表中已经存在的数据。

    `--create-hive-table` 默认是false,如果目标表已经存在了，那么创建任务会失败。

    `--hive-table <table-name>` 当导入数据时设置要创建的hive表，后面跟hive表名。

    `--hive-drop-import-delims` 当导入hive表时删除字段中的行界定符（\n,\r）和列界定符（\01），目的是避免使用sqoop导入数据时由于数据中包含了hive默认的行界定符和列界定符而产生的错误。

    `--hive-delims-replacement` 当导入hive表时使用用户定义的字符串替代字段中的\n,\r,\01，与`--hive-drop-import-delims`作用类似，都是为了避免导入数据时出错。

    `--hive-partition-key` 在使用sqoop导入数据时可以指定分区，key指定分区的键。

    `--hive-partition-value <v>` value指定分区的值。
    
	**注意：** `--hive-partition-key`和`--hive-partition-value`只能指定一个参数，可以使用`--hcatalog-partition-keys`和`--hcatalog-partition-values`选项指定多个分区字段，用逗号分隔。

    `--map-column-hive <map>` 覆盖已配置列的从SQL类型到Hive类型的默认映射。

    `--compress`和`--compression-codec`参数可以将导入到hive中的数据进行压缩，压缩导入hive表的一个缺点是对于并行执行的映射任务许多编码器不支持切分。但是lzop编码器支持，使用这种编码器导入表时，sqoop会自动建立文建索引来切分和配置一个新的hive表。

- **将数据导入HBase**

	**导入HBase常用参数**

    `--column-family <family>` 设置导入目标列的列族。

    `--hbase-create-table` 向新创建HBase表中导入数据。

    `--hbase-row-key <col>` 指定哪一个输入列作为row key,如果输入表中包含复合键，必须使用逗号分隔开。

    `--hbase-table <table-name>` 指定一个HBase表作为导入目标，来替代导入HDFS。

    `--hbase-bulkload` 开启批量加载。

- **import导入例子**

	将crop数据库中的EMPLOYEES表导入hdfs(无需输入用户名和密码登录数据库)。

    `sqoop import --connect jdbc:mysql：//db.foo.com/corp --table EMPLOYEES`

	需要输入数据库用户名和密码的导入方式

    `sqoop import --connect jdbc:mysql：//db.foo.com/corp --table EMPLOYEES --username SomeUser -P [enterpassword(hidden)]`

	从EMPLOYEES表中选择指定的列导入到HDFS

	```
	sqoop import --connect jdbc:mysql://db.foo.com/corp --table EMPLOYEES 
	--columns "employee_id,first_name,last_name,job_title"

	```

	使用多个并行任务的方式进行导入

	```
	sqoop import --connect jdbc:mysql://db.foo.com/corp --table EMPLOYEES 
	-m 8 
	```

	将数据存储为SequenceFiles，并设置生成的类名为com.foocorp.Employee:

	```
	sqoop import --connect jdbc:mysql://db.foo.com/corp --table EMPLOYEES 
	--class-name com.foocorp.Employee --as-sequencefile
	```

	使用文本文件方式导入时指定文件中的分隔符

	```
	sqoop import --connect jdbc://db.foo.com/corp --table EMPLOYEES
	--fields-terminated-by '\t' --lines-terminated-by '\n'
	--optionally-enclosed-by '\"'
	```

	导入数据到hive

	```
	sqoop import --connect jdbc:mysql://db.foo.com/corp --table EMPLOYEES
	--hive-import
	```

	条件导入

	```
	sqoop import --connect jdbc:mysql://db.foo.com/corp --table EMPLOYEES
	--where "start_date > '2010-01-01'"
	```

	从默认值更改拆分列

	```
	sqoop import --connect jdbc:mysql://db.foo.com/corp --table EMPLOYEES
	--split-by dept_id
	```

	在已经导入的100000数据中执行增量导入

	```
	sqoop import --connect jdbc:mysql://db.foo.com/somedb --table sometable
	--where "id > 100000" --target-dir /incremental_dataset --append
	```

## 2.sqoop-import-all-tables ##

导入多个表到HDFS，每个表在HDFS中都以单独的目录进行存储。

使用import-all-tables工具时必须满足以下条件。

- 每个表都应该具有唯一的列主键或者必须使用`--autoreset-to-one-mapper`参数。
- 必须打算导入每个表中的所有列。
- 既不能使用非默认的方式拆分列，也不能使用`WHERE`增加任何条件。

**语法：** 

    `sqoop import-all-tables (generic-args) (import-args)`
	`sqoop-import-all-tables (generic-args) (import-args)`

多表导入的参数和sqoop-import中的参数功能类似，具体参数可查看官网手册，这里不再阐述。[http://sqoop.apache.org/docs/1.4.7/SqoopUserGuide.html#_selecting_the_data_to_import](http://sqoop.apache.org/docs/1.4.7/SqoopUserGuide.html#_selecting_the_data_to_import "Sqoop User Guide (v1.4.7)")

多表导入举例：

	`sqoop import-all-tables --connect jdbc:mysql://db.foo.com/corp`

## 3.sqoop-export ##

**作用：**

    `export`工具的作用是将数据从HDFS导出到关系型数据库。导出成功的前提是关系型数据库已经存在。根据用户指定的分隔符读取输入文件并解析成一组数据。

**语法：**

    `sqoop export (generic-args) (export-args)`
	`sqoop-export (generic-args) (export-args)`

hadoop通用参数必须在export导出参数之前，导出参数的顺序可以不分先后。

- **导出控制参数**

	`--columns <col,col,col...>` 要导出到表格的列。

	`--direct` 直接导出，速到较快。

	`--export-dir <dir>` 导出数据的HDFS源路径。

	`-m,--num-mappers` <n> 使用n个并行任务进行数据导出。

	`--table <table-name>` 要导入的表。

	`--call <stored-proc-name>` 导出数据调用的指定存储过程名。

	`--update-key <col-name>` 更新参考的列名称，多个列名使用逗号分隔。

	`--update-mode <mode>` 指定更新策略，包括：updateonly（默认）、allowinsert

	`--input-null-string <null-string>` 使用指定的字符串，替换字符串类型值为null的列。

	`--input-null-non-string <null-string>` 使用指定字符串，替换非字符串类型值为null的列。

	`--staging-table <staging-table-name>` 在数据导出数据库之前，数据临时存放的表名。

	`--clear-staging-table` 清除工作区中临时存放的数据。

	`--batch` 使用批量模式导出。

**以下原因可能导致导出数据失败：**

- Hadoop集群与数据库之间连接失败（有硬件或软件引起）。
- 尝试插入违反一致性约束的行（例如：插入重复的主键值）。
- 尝试从HDFS源数据中解析不完整或格式不正确的数据记录。
- 尝试使用不正确的分隔符导出数据。
- 容量问题（RAM或磁盘空间不足）。

**导出数据举例：**

- 将数据导出到已存在的表bar中：

```
sqoop export --connect jdbc:mysql://db.example.com/foo --table bar
--export-dir /results/bar_data
```

从/results/bar_data中获取数据导入到da.example.com主机上的foo数据库中的bar表中。表在数据库中必须已经存在。

- 在导出数据时显示详细信息

```
sqoop export --connect jdbc:mysql://db.example.com/foo --table bar
--export-dir /results/bar_data --validate
```

- 在导出数据时将/results/bar_data中的每一条导出记录都指定存储过程名为barproc

```
sqoop export --connect jdbc：mysql：//db.example.com/foo --call barproc
--export-dir /results/bar_data
```

## sqoop-Jobs ##

导入和导出通过相同的命令可以被重复的执行，尤其是在采用增量导入的时候。sqoop允许用户定义saved jobs，saved jobs 记录一个需要执行sqoop命令的配置信息，以便任务重复的被执行。默认情况下，作业描述保存到$HOME/.sqoop中的私有仓库中。可以配置sqoop采用一个共享仓库，这样可以保证集群上的多个用户使用saved jobs。

**语法：**

```
sqoop job (generic-args) (job-args) [-- [subtool-name] (subtool-args)]
sqoop-job (generic-args) (job-args) [-- [subtool-name] (subtool-args)]
```

- **作业管理参数**

	`--create <job-id>` 为saved job 指定一个新的作业名（job-id）。

	`--delete <job-id>` 删除一个以保存作业（saved job）。

	`--exec <job-id>` 后接一个由`--create`定义的任务名，并执行该作业。

	`--show <job-id>` 显示以保存作业的参数

	`--list` 列出所有以保存的作业。

**以保存作业举例**

创建一个名称为myjob的稍后执行的作业，该作业并不立即执行，只是存在了可执行以保存作业的列表中。

```
sqoop job --create myjob --import --connect jdbc:mysql://example.com/db
--table mytable
```

列出所有以保存的作业

```
sqoop job --list
```

显示以保存作业的详细参数

```
sqoop job --show myjob
```

执行以保存的作业

```
sqoop job --exec myjob
```

exec 操作时允许您在 `--` 后面提供以保存作业的参数。如将数据库更改为需要的用户名，可以通过以下命令指定用户名和密码。

```
sqoop job --exec myjob -- --username someuser -P
```

**metastore连接参数**

默认情况下,一个私有metastore在$HOME/.sqoop中实例化。

	`--meta-connect <jdbc-url>` 指定一个连接metastore的JDBC连接字符串。如果已经使用sqoop-metastore工具托管了metastore,便可以使用`--meta-connect argument` 连接到metastore。

## sqoop-merge ##

**目的：**

merge工具用来合并两个数据集，其中一个较新数据集中的条目应覆盖旧数据集中的条目。merge在增量导入中比较常见。

**语法**

```
sqoop merge (generic-args) (merge-args)
sqoop-merge (generic-args) (merge-args)
```

**merge工具中的参数**

    `--class-name <class>` 指定在合并作业期间要使用的类名，该class包含在jar包中。

    `--jar-file <file>` 指定合并时引入的jar包，该jar包通过Codegen工具生成。

    `--merge-key <col>` 指定用作合并键的列名。

    `--new-data <path>` 指定较新数据集的路径。

    `--onto <path>` 指定旧数据集的路径。

    `--target-dir <path>` 指定合并作业的输出路径。

合并作业运行一个MapReduce作业，有两个目录作为输入，MapReduce作业的输出放置在`--target-dir`指定的HDFS目录中。

在合并作业时，假定每个记录中都有唯一的一个主键值，主键的列被`--merge-key`指定。同一数据集中的多个行不应具有相同的主键，否则会导致数据的丢失。

要解析数据集并提取键列，必须使用先前导入的自动生成类，应使用`--class-name`和`--jar-file`指定类名和jar文件。如果不可用，可以使用`codegen`工具进行生成。

合并作业工具通常在以最新修改日期模式增量导入后运行。

**merge工具举例**

```
sqoop merge --new-data newer --onto older --target-dir merged
--jar-file datatypes.jar --class-name Foo --merge-key id
```

上面例子将运行一个MapReduce作业，其中每行id列的值用于连接行。新数据集中的行将优先于旧数据集中的行使用。使用merge合并时要保证旧数据集文件类型要和新数据集文件类型相同。

## sqoop-codegen ##

**目的**

codegen 工具生成了封装和解释导入记录的java类。sqoop在进行每一次的导入任务时，都会调用codegen，生成一个java文件，并编译打包成jar，供mapreduce使用。

**语法**

```
sqoop codegen (generic-args) (codegen-args)
sqoop-codegen (generic-args) (codegen-args)
```

**代码生成参数**

    `--bindir <dir>` 编译对象输出目录。

    `--class-name <name>` 设置生成的类名，这会覆盖`--package-name`。与`--jar-file`连用时，设置输入类。

    `--jar-file <file>` 关闭代码生成，使用指定的jar包。

    `--outdir <dir>` 生成代码的输出目录。

    `--package-name <name>` 将自动生成的类放入此包中。

    `--map-column-java <m>` 覆盖已配置列的从SQL类型到Java类型的默认映射。

**codegen工具举例**

```
sqoop codegen --connect jdbc:mysql://db.example.com/corp
--table employees
```

## sqoop-create-hive-table ##

**目的：**

`create-hive-table`工具基于先前要导入到HDFS或计划要导入到HDFS中的定义来填充hive metastore。这有效执行了`sqoop-import`中的`--hive-import`步骤，而无需运行前面的导入。

如果数据已经加载到了HDFS，可以使用`sqoop-create-hive-table`完成HDFS到hive数据的管道传输。也可以使用该工具创建hive表；然后，可以再用户执行预处理步骤之后将数据导入并填充到目标中。

**语法**

```
sqoop create-hive-table (generic-args) (create-hive-table-args)
sqoop-create-hive-table (generic-args) (create-hive-table-args)
```

**create-hive-table举例**

```
sqoop create-hive-table --connect jdbc:mysql://db.example.com/corp
--table employees --hive-table emps
```

上例中定义了一个名为emps的hive表，该表的定义基于名为employees的数据库表。

## sqoop-eval工具 ##

**目的**

eval工具允许用户针对数据库去快速的运行一个简单的SQL查询，并将结果返回到控制台。这使用户可以预览导入查询，以确保导入所需的数据。

**语法**

```
sqoop eval (generic-args) (eval-args)
sqoop-eval (generic-args) (eval-args)
```

**eval举例**

从employees表中选择10条记录

```
sqoop eval --connect jdbc:mysql://db.example.com/corp
--query "SELECT * FROM employees LIMIT 10"
```

向foo表中插入一行数据

```
sqoop eval --connect jdbc:mysql://db.example.com/corp
-e "INSERT INTO foo VALUES(42,'bar')"
```

## sqoop-list-databases ##

**作用**

列出当前服务器上的数据库信息

**语法**

```
sqoop list-databases (generic-args) (list-databases-args)
sqoop-list-databases (generic-args) (list-databases-args)
```

**sqoop-list-databases举例**

列出mysql服务器上可用的数据库表

```
sqoop list-databases --connect jdbc:mysql://database.example.com information_schema
```

## sqoop-list-tables ##

**作用**

列出当前数据库中所有表。

**语法**

```
sqoop list-tables (generic-args) (list-tables-args)
sqoop-list-tables (generic-args) (list-tables-args)
```

**sqoop-list-tables举例**

列出corp数据库中的所有可用表。

```
sqoop list-tables --connect jdbc:mysql://database.example.com/corp
employees
```

## sqoop-help和sqoop-version ##

显示帮助信息和sqoop版本信息。