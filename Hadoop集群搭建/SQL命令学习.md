# 1. SQL 基础教程（这里使用的是Oracle数据库） #

    su oracle     #切换为oracle用户
    sqlplus       #打开oracle自带的客户端程序

进入以后输入用户名和密码，oracle自带几个用户。

`sys`和`system`都是系统管理员（DBA），拥有最大的权限，密码是安装时设置的。

常用命令：

## 1.1显示当前用户名

```plsql
show user
```

## 1.2关闭oracle数据库

```plsql
shutdown immediate
```

## 1.3 启动数据库

```plsql
startup open;
```

## 1.4以sys用户身份登录数据库

```plsql
conn sys/[password] as sysdba;
```

## 1.5查询表中的所有字段（DOG表为例）

```plsql
select * from cols where table_name='DOG';
```

## 1.6向表中插入一条数据（DOG表为例）

```plsql
insert into DOG(name,age) values('dog_one',3);
```



## 1.7 SQL SELECT 语句

本节学习SELECT 和 SELECT * 语句。

**SELECT语句**

**作用：**SELECT语句用来从表中选取数据，结果被存储在一个结果表中（称为结果集）。

**语法：**

```sql
SELECT [列名] FROM [表名];     #列出指定列名
```

以及

```plsql
SELECT * FROM [表名];        #列出所有列的快捷方式
```

**注意：**SQL语句大小写不敏感，SELECT等价于select。

**举例：**

获取persons 表中列名为“Lastname”和“Firstname”的内容。

```sql
SELECT Lastname,Firstname FROM persons;
```

## 1.8 SQL SELECT DISTINCT 语句

**作用：**在表中，可能包含重复值。`DISTINCT`关键字用来返回列中所有不重复的值。

**语法：**

```sql
SELECT DISTINCT [列名] FROM [表名];
```

**举例：**

获取DOG表中age所有不重复的内容（即相同的age只出现一次）。

```plsql
SELECT DISTINCT age FROM DOG;
```

## 1.9 SQL WHERE 子句

**WHERE子句用于规定选择的标准**

**作用：**

有条件的从表中选取数据。

**语法：**

``` sql
SELECT [列名称] FROM [表名称] WHERE [列] [运算符] [值];
```

**WHERE语句支持的运算符**

| 操作符  |     描述     |
| :-----: | :----------: |
|    =    |     等于     |
|   <>    |    不等于    |
|    >    |     大于     |
|   >=    |   大于等于   |
|    <    |     小于     |
|   <=    |   小于等于   |
| BETWEEN | 在某个范围内 |
|  LIKE   | 搜索某种模式 |

**注意：**

（1）某些SQL版本中，`<>`可以写成`!=`。

（2）SQL使用单引号来环绕文本值（大部分数据库也接受双引号），数值不需要引号。

**举例：**

```sql
SELECT * FROM persons Firstname='Bush';
```

## 1.10 SQL AND&OR运算符

**作用：**

AND&OR 运算符基于一条以上的条件记录进行过滤。

AND&OR运算符可以把WHERE子句中的两个条件结合起来，AND连接时两个条件均成立，则内容显示；OR连接时，其中一个条件成立，则显示。

**举例：**

(1)

```sql
SELECT * FROM Persons WHERE FirstName='Thomas' AND LastName='Carter';
```

(2)

```sql
SELECT * FROM Persons WHERE firstname='Thomas' OR lastname='Carter'
```

## 1.11 SQL ORDER BY 子句

**作用：**

ORDER BY子句用于对结果集进行排序。

ORDER BY 语句用于根据指定的列对结果集进行排序。

ORDER BY 语句默认按照升序对记录进行排序。

如果您希望按照降序对记录进行排序，可以使用 DESC 关键字。

**语法：**

```sql
SELECT [列名] FROM [表名] ORDER BY [列名];
```

**举例：**

升序排列

```sql
SELECT * FROM DOG ORDER BY age;
```

降序排列

```sql
SELECT * FROM DOG ORDER BY age DESC;
```

## 1.12 INSERT INTO 语句

**作用：**

INSERT INTO 用于向表中插入数据。

**语法：**

```sql
INSERT INTO [表名称] VALUES(value1,value2,...);
```

**举例：**

```sql
INSERT INTO persons VALUES('Gates','Bill','xuanwumen 10','Beijing')
```

## 1.13 SQL UPDATE 语句

**作用：**

update语句用来修改表中的数据

**语法：**

```sql
UPDATE [表名] SET [列名] = [新值] WHERE [列名] = [某值];
```

**更新某一行中的一个列值**

我们为lastname是 “Wilson” 的人添加Firstname；

```sql
UPDATE persons SET Firstname='Fred' WHERE Lastname = 'Wilson';
```

**更新某一行中的若干列**

```sql
UPDATE person SET Address = 'zhongshan 23',City = 'Nanjing'
WHERE Lastname='Wilson';
```

## 1.14 SQL DELETE 语句

**作用：**

delete语句用于删除表中的行。

**语法：**

```sql
DELETE FROM [表名] WHERE [列名] = [值];
```

**删除某行**

```sql
DELETE FROM person WHERE lastname = 'Wilson';
```

**删除所有行**

可以在不删除表的情况下删除所有行，这意味着表的结构、属性和索引都是完整的。

```sql
DELETE FROM table_name
```

或者

```sql
DELETE * FROM table_name
```

# 2 SQL 高级教程

## 2.1 SQL TOP子句

**作用：**

TOP子句用于规定要返回的记录的数目。

对于拥有数千条记录的大型表来说，TOP子句是非常有用的。

**注意：**并非所有的数据库系统都支持TOP子句。

**SQL Server的语法：**

```mysql
SELECT TOP number | percent column_name(s) 
FROM table_name;
```

**MySQL 语法：**

```mysql
SELECT column_name(s)
FROM table_name
LIMIT [number];
```

**举例：**

```mysql
SELECT * FROM persons LIMIT 5;
```



**Oracle语法：**

```plsql
SELECT column_names(s)
FROM table_name
WHERE ROWNUM <= [number];
```

```plsql
SELECT *
FROM persons
WHERE ROWNUM <= 5;
```

**SQL  TOP 举例**

我们希望从persons 表的上面选取头两条数据。

**语句如下：**

```plsql
SELECT TOP 2 * FORM persons;
```

```plsql
SELECT TOP 50 PERCENT * FROM persons;
```

## 2.2 SQL LIKE 操作符

**作用：** 

LIKE 操作符用于在WHERE子句中搜索列中的指定模式。

**语法：**

```plsql
SELECT column_name(s)
FROM table_name
WHERE column_name LIKE pattern
```

**举例：**

现在，我们希望选取上面persons 表中居住在以N开始的城市里的所有人。

```mysql
SELECT * FROM persons WHERE City LIKE 'N%';
```

**提示：**”%“可用于定义通配符（模式中缺少的字母）。

接下来，我们希望选取persons表中居住在以g结尾的城市里的所有人。

```mysql
SELECT * FROM persons WHERE City LIKE '%g';
```

接下来，我们希望从 persons 表中选取居住在包含 "lon" 的城市里的人。

```mysql
SELECT * FROM persons WHERE City LIKE '%lon%';
```

通过使用 NOT 关键字，我们可以从 persons 表中选取居住在*不包含* "lon" 的城市里的人：

```mysql
SELECT * FROM persons WHERE City NOT LIKE '%lon%';
```

## 2.3 通配符

**作用**

在搜索数据库中的数据时，SQL通配符可以替代一个或多个字符。

SQL通配符必须与LIKE运算符一起使用。

**SQL中支持的通配符包含以下几个。**

|          通配符          |            描述            |
| :----------------------: | :------------------------: |
|            %             |     替代一个或多个字符     |
|            _             |       仅替代一个字符       |
|        [charlist]        |   字符列中的任意单一字符   |
| [^charlist]或[!charlist] | 不在字符列中的任意单一字符 |

**使用[charlist]通配符**

**举例1**

我们希望从上面的 persons 表中选取居住的城市**以** "A" 或 "L" 或 "N" 开头的人。

```mysql
SELECT * FROM persons
WHERE City LIKE '[ALN]%';
```

**举例2**

我们希望从上面的 persons 表中选取居住的城市**不以** "A" 或 "L" 或 "N" 开头的人。

```mysql
SELECT * FROM persons
WHERE City LIKE '[!ALN]%';
```

## 2.4 SQL IN 操作符

**作用：**

IN 操作符允许我们在WHERE子句中规定多个值。

**语法：**

```plsql
SELECT column_name(s)
FROM table_name
WHERE column_name IN (value1,value2,...);
```

**举例**

找出person表中姓氏为Adams 和 Carter的人。

```plsql
SELECT * FROM WHERE Lastname IN('Adams','Carter');
```

## 2.5 SQL BETWEEN  AND操作符

**作用：**

BETWEEN  AND操作符在WHERE子句中使用，选取介于两个值之间的数据范围。这些值可以是文本、数值或日期。

**语法：**

```plsql
SELECT column_name(s)
FROM table_name
WHERE column_name
BETWEEN value1 AND value2;
```

**举例1：**

按字母显示出介于Adams(包括)和Carter（不包括）之间的人。

```plsql
SELECT * FROM persons WHERE Lastname BETWEEN 'Adams' AND 'Carter'
```

**重要事项：**不同的数据库对 BETWEEN...AND 操作符的处理方式是有差异的。某些数据库会列出介于 "Adams" 和 "Carter" 之间的人，但不包括 "Adams" 和 "Carter" ；某些数据库会列出介于 "Adams" 和 "Carter" 之间并包括 "Adams" 和 "Carter" 的人；而另一些数据库会列出介于 "Adams" 和 "Carter" 之间的人，包括 "Adams" ，但不包括 "Carter" 。

所以，请检查你的数据库是如何处理 BETWEEN....AND 操作符的！

**举例2：**

```plsql
SELECT * FROM persons WHERE lastname NOT BETWEEN 'Adams' AND 'Carter';
```

## 2.6 SQL Alias

**通过使用SQL，可以为列名称和表名称指定别名（Alias）。**

表的SQL Alias 语法

```plsql
SELECT column_name(s)
FROM table_name
AS alias_name;
```

列的SQL Alias 语法

```plsql
SELECT column_name AS alias_name 
FROM table_name;
```

**举例1(使用表名称别名)：**

假设我们有两个表分别是：persons和product_orders。分别指定它们的别名为p和po。

列出John Adams的所有订单。

```plsql
SELECT po.OrderID, p.LastName, p.FirstName
FROM persons AS p, product_Orders AS po
WHERE p.Lastname='Adams' AND p.Firstname='John'
```

不是用别名的SELECT语句

```plsql
SELECT Product_Orders.OrderID, Persons.LastName, Persons.FirstName
FROM Persons, Product_Orders
WHERE Persons.LastName='Adams' AND Persons.FirstName='John'
```

****

**从上面两条 SELECT 语句您可以看到，别名使查询程序更易阅读和书写。**

**举例2：（使用一个列的别名）**

```plsql
SELECT LastName AS Family, Firstname AS Name
FROM Persons
```

## 2.6 SQL JOIN 语句

**作用：**

SQL JOIN 用于根据两个或多个表中列的关系，从这些表中查询数据。

**JOIN 和 KEY**

有时为了得到完整的结果，我们需要从两个或更多的表中获取结果。我们就需要执行 join。

数据库中的表可通过键将彼此联系起来。主键（Primary Key）是一个列，在这个列中的每一行的值都是唯一的。在表中，每个主键的值都是唯一的。这样做的目的是在不重复每个表中的所有数据的情况下，把表间的数据交叉捆绑在一起。

请看 "persons" 表：

| Id_P | Lastname | Firstname | Address        | City     |
| :--- | :------- | :-------- | :------------- | :------- |
| 1    | Adams    | John      | Oxford Street  | London   |
| 2    | Bush     | George    | Fifth Avenue   | New York |
| 3    | Carter   | Thomas    | Changan Street | Beijing  |

请注意，"Id_P" 列是 persons 表中的的主键。这意味着没有两行能够拥有相同的 Id_P。即使两个人的姓名完全相同，Id_P 也可以区分他们。

接下来请看 "Orders" 表：

| Id_O | OrderNo | Id_P |
| :--- | :------ | :--- |
| 1    | 77895   | 3    |
| 2    | 44678   | 3    |
| 3    | 22456   | 1    |
| 4    | 24562   | 1    |
| 5    | 34764   | 65   |

请注意，"Id_O" 列是 orders 表中的的主键，同时，"orders" 表中的 "Id_P" 列用于引用 "persons" 表中的人，而无需使用他们的确切姓名。

请留意，"Id_P" 列把上面的两个表联系了起来。

**引用两个表**

我们可以通过引用两个表的方式，从表中获取数据。

```plsql
SELECT persons.Lastname,persons.Firstname,orders.OrderNo
FROM persons , orders
WHERE persons.Id_P=orders.Id_P;
```

**除了上述方法，还可以使用JOIN来从两个表中获取数据。**

```mysql
SELECT persons.Lastname,persons.Firstname,orders.OrderNo
FROM persons
INNER JOIN orders
ON persons.Id_P=Orders.Id_P
ORDER BY persons.Lastname;
```

**不同的 SQL  JOIN**

除了我们在上面的例子中使用的INNER JOIN（内连接），我们还可以使用其它几种连接。

下面列出了可以使用的JOIN类型，以及它们之间的差异。

- JOIN：如果表中有至少一个匹配，则返回行。
- LEFT JOIN：即使右表中没有匹配也从左表返回所有的行
- RIGHT JOIN：即使左表中没有匹配，也从右表中返回所有的行
- FULL JOIN：只要其中一个表中存在匹配，就返回行

### 2.6.1 INNER JOIN 关键字

**作用：**

在表中存在至少一个匹配时，INNER JOIN 关键字返回行。

**语法：**

```mysql
SELECT column_name(s)
FROM table_name1
INNER JOIN table_name2
ON table_name1.column_name=table_name2.column_name
```

**注释：**INNOR JOIN 和JOIN是相同的

**原始表：**

"Persons" 表：

| Id_P | Lastname | Firstname |    Address     |   City   |
| :--: | :------: | :-------: | :------------: | :------: |
|  1   |  Adams   |   John    | Oxford Street  |  London  |
|  2   |   Bush   |  George   |  Fifth Avenue  | New York |
|  3   |  Carter  |  Thomas   | Changan Street | Beijing  |

"Orders" 表：

| Id_O | OrderNo | Id_P |
| :--: | :-----: | :--: |
|  1   |  77895  |  3   |
|  2   |  44678  |  3   |
|  3   |  22456  |  1   |
|  4   |  24562  |  1   |
|  5   |  34764  |  65  |

**内连接（INNER JOIN）实例**

现在，我们希望列出所有人的定购。

您可以使用下面的 SELECT 语句：

```mysql
SELECT persons.Lastname, persons.Firstname, orders.OrderNo
FROM Persons
INNER JOIN orders
ON persons.Id_P=orders.Id_P
ORDER BY persons.Lastname;
```

结果集：

| Lastname | Firstname | OrderNo |
| :------- | :-------- | :------ |
| Adams    | John      | 22456   |
| Adams    | John      | 24562   |
| Carter   | Thomas    | 77895   |
| Carter   | Thomas    | 44678   |

INNER JOIN 关键字在表中存在至少一个匹配时返回行。如果 "Persons" 中的行在 "Orders" 中没有匹配，就不会列出这些行。

### 2.6.2 SQL LEFT JOIN 关键字

**作用：**LEFT JOIN 关键字会从左表 (table_name1) 那里返回所有的行，即使在右表 (table_name2) 中没有匹配的行。

**语法：**

```mysql
SELECT column_name(s)
FROM table_name1
LEFT JOIN table_name2
ON table_name1.column_name=table_name2.column.name;
```

**注释：**在某些数据库中，LEFT JOIN 称为 LEFT OUTER JOIN。

**左连接（LEFT JOIN）实例（使用2.6.1的原始表）**

现在，我们希望列出所有的人，以及他们的订购-如果有的话；没有订购则为空。

```mysql
SELECT parents.Lastname,persons.Firstname,orders.OrderNo
FROM parsons
LEFT JOIN orders
ON persons.Id_p=orders.Id_p
ORDER BY persons.Lastname;
```

结果集：

| Lastname | Firstname | OrderNo |
| :------- | :-------- | :------ |
| Adams    | John      | 22456   |
| Adams    | John      | 24562   |
| Carter   | Thomas    | 77895   |
| Carter   | Thomas    | 44678   |
| Bush     | George    |         |

LEFT JOIN 关键字会从左表 (Persons) 那里返回所有的行，即使在右表 (Orders) 中没有匹配的行。

### 2.6.3 SQL RIGHT JOIN 关键字

**作用：**

RIGHT JOIN 关键字会右表 (table_name2) 那里返回所有的行，即使在左表 (table_name1) 中没有匹配的行。

**语法：**

```mysql
SELECT column_name(s)
FROM table_name1
RIGHT JOIN table_name2
ON table_name1.column_name=table_name2.column.name;
```

**右连接(RIGHT JOIN)实例（使用2.6.1的原始表）：**

现在，我们希望列出所有订单，以及订购他们的人-如果有的话；没有则为空。

```mysql
SELECT persons.Lastname,persons.Firstname,orders.OrderNo
FROM persons
LEFT JOIN orders
ON persons.Id_p=orders.Id_p
ORDER BY persons.Lastname;
```

结果集：

| Lastname | Firstname | OrderNo |
| :------- | :-------- | :------ |
| Adams    | John      | 22456   |
| Adams    | John      | 24562   |
| Carter   | Thomas    | 77895   |
| Carter   | Thomas    | 44678   |
|          |           | 34764   |

RIGHT JOIN 关键字会从右表 (Orders) 那里返回所有的行，即使在左表 (Persons) 中没有匹配的行。

### 2.6.4 SQL FULL JOIN 关键字

**作用：**

只要其中某个表存在匹配， FULL JOIN 关键字就会返回行

**语法：**

```mysql
SELECT column_name(s)
FROM table_name1
FULL JOIN table_name2 
ON table_name1.column_name=table_name2.column_name;
```

**注释：**在某些数据库中， FULL JOIN 称为 FULL OUTER JOIN。

**全连接（FULL JOIN）实例（使用2.6.1的原始表）**

```mysql
SELECT persons.Lastname ,persons.Firstname,orders.OrderNo
FROM persons
FULL JOIN orders
ON persons.Id=order.Id
ORDER BY persons.Lastname;
```

结果集：

| Lastname | Firstname | OrderNo |
| :------- | :-------- | :------ |
| Adams    | John      | 22456   |
| Adams    | John      | 24562   |
| Carter   | Thomas    | 77895   |
| Carter   | Thomas    | 44678   |
| Bush     | George    |         |
|          |           | 34764   |

FULL JOIN 关键字会从左表 (Persons) 和右表 (Orders) 那里返回所有的行。如果 "Persons" 中的行在表 "Orders" 中没有匹配，或者如果 "Orders" 中的行在表 "Persons" 中没有匹配，这些行同样会列出。

## 2.7 SQL UNION 操作符

**作用：**

UNION 操作符用于合并两个或多个 SELECT 语句的结果集。

请注意，UNION 内部的 SELECT 语句必须拥有相同数量的列。列也必须拥有相似的数据类型。同时，每条 SELECT 语句中的列的顺序必须相同。

UNION 结果集总是等于UNION 中第一个SELECT语句的列名。

**语法：**

```mysql
SELECT column_name(s) FROM table_name1
UNION
SELECT column_name(s) FROM table_name2;
```

**注释：**默认地，UNION 操作符选取不同的值。如果允许重复的值，请使用 UNION ALL。

```mysql
SELECT column_name(s) FROM table_name1
UNION ALL
SELECT column_name(s) FROM table_name2;
```

****

**原始表**

**Employees_China:**

| E_ID | E_Name         |
| :--- | :------------- |
| 01   | Zhang, Hua     |
| 02   | Wang, Wei      |
| 03   | Carter, Thomas |
| 04   | Yang, Ming     |

**Employees_USA:**

| E_ID | E_Name         |
| :--- | :------------- |
| 01   | Adams, John    |
| 02   | Bush, George   |
| 03   | Carter, Thomas |
| 04   | Gates, Bill    |

**实例：**

列出所有在中国和美国所有不同的雇员名

```mysql
SELECT E_Nmae FROM Employees_China
UNION
SELECT E_Nmae FROM Employees_China;
```

结果集：

|     E_Name     |
| :------------: |
|   Zhang, Hua   |
|   Wang, Wei    |
| Carter, Thomas |
|   Yang, Ming   |
|  Adams, John   |
|  Bush, George  |
|  Gates, Bill   |

**另外，可以自己动手测试下UNION ALL 命令，观察 两者输出的区别**

## 2.8 SQL SELECT INTO 语句

**作用：**

**SQL SELECT INTO 语句可用于创建表的备份附件。**

SELECT INTO 语句从一个表中选取数据，然后把数据插入另一个表中。

SELECT INTO 语句常用于创建表的备份复件或者用于对记录进行存档。

**语法：**

把所有列插入新表

```mysql
SELECT * INTO new_table_name [IN externaldatabase]
FROM old_tablename;
```

插入指定列

```mysql
SELECT column_name(s)
INTO new_table_name [IN externaldatabase]
FROM old_tablename;
```

**举例：**

- 制作表的备份文件

  ```mysql
  SELECT *
  INTO parsons_backup
  FROM persons;
  ```

- IN 子句用于向另一个数据库中拷贝文件

  ```mysql
  SELECT * 
  INTO persons IN 'backup.mdb'
  FROM persons;
  ```

- 只拷贝某些列

  ```mysql
  SELECT LastName,FirstName
  INTO persons_backup
  FROM persons
  ```

**SQL SELECT INTO实例-带有WHERE子句**

例如从persons表中提取居住在Beijing的人的信息，创建了一个带有两个列的名为 "Persons_backup" 的表

```mysql
SELECT Lastname,Firstname
INTO person_backup
FROM persons
WHERE City='Beijing';
```

**SQL SELECT INTO 实例-被连接的表**

从多个表中选取数据也是可以做到的

下面的例子会创建一个名为 "Persons_Order_Backup" 的新表，其中包含了从 Persons 和 Orders 两个表中取得的信息：

```mysql
SELECT persons.Lastname,order.OrderNo
INTO persons_order_backup
FROM persons
INNER JOIN orders
ON persons.Id_p=orders.Id_p;
```

## 2.9 CREATE DATABASE&TABLE语句

**作用：**创建数据库或者数据表。

## 2.10 SQL中的约束

**作用：**

约束用于限制加入表的数据的类型。

可以在创建表时规定约束（通过 CREATE TABLE 语句），或者在表创建之后也可以（通过 ALTER TABLE 语句）。

**SQL 中主要有以下几种约束**

- NOT NULL
- UNIQUE
- PRIMARY KEY
- FOREIGN KEY
- CHECK
- DEFAULT

### 2.10.1 NOT NULL 约束

**作用：** 约束强制列不接受NULL值。

**举例：**

下面SQL语句强制Id_P列和Lastname列不接受NULL值。

```mysql
CREATE TABLE Persons
(
Id_P int NOT NULL,
LastName varchar(255) NOT NULL,
FirstName varchar(255),
Address varchar(255),
City varchar(255)
);
```



### 2.10.2 UNIQUE 约束

**作用：**

UNIQUE 约束唯一标识数据库表中的每条记录。

UNIQUE 和 PRIMARY KEY 约束均为列或列集合提供了唯一性的保证。

PRIMARY KEY 拥有自动定义的 UNIQUE 约束。

请注意，每个表可以有多个 UNIQUE 约束，但是每个表只能有一个 PRIMARY KEY 约束。

**举例：**

在创建表时设置UNIQUE约束

**MySQL:**

```mysql
CREATE TABLE Persons
(
Id_P int NOT NULL,
LastName varchar(255) NOT NULL,
FirstName varchar(255),
Address varchar(255),
City varchar(255),
UNIQUE (Id_P)
);
```

**SQL  Server / Oracle/ MS  Access:**

```plsql
CREATE TABLE Persons
(
Id_P int NOT NULL UNIQUE,
LastName varchar(255) NOT NULL,
FirstName varchar(255),
Address varchar(255),
City varchar(255)
);
```

如果需要**命名** UNIQUE 约束，以及为多个列定义 UNIQUE 约束，请使用下面的 SQL 语法：

**MySQL / SQL Server / Oracle / MS Access:**

```plsql
CREATE TABLE Persons
(
Id_P int NOT NULL,
LastName varchar(255) NOT NULL,
FirstName varchar(255),
Address varchar(255),
City varchar(255),
CONSTRAINT uc_PersonID UNIQUE (Id_P,LastName)
);
```

当表已经被创建，添加UNIQUE约束

**MySQL / SQL Server / Oracle / MS Access:**

```mysql
ALTER TABLE persons
ADD UNIQUE (Id_p);
```

如需命名 UNIQUE 约束，并定义多个列的 UNIQUE 约束，请使用下面的 SQL 语法：

**MySQL / SQL Server / Oracle / MS Access:**

```mysql
ALTER TABLE persons
ADD CONSTRAINT uc_personID UNIQUE(Id_p,Lastname);
```

**撤销 UNIQUE 约束**

如需撤销 UNIQUE 约束，请使用下面的 SQL：

**MySQL:**

```mysql
ALTER TABLE Persons
DROP INDEX uc_PersonID
```

**SQL Server / Oracle / MS Access:**

```mysql
ALTER TABLE Persons
DROP CONSTRAINT uc_PersonID
```

### 2.10.3 PRIMARY KEY 约束

**作用：**

PRIMARY KEY 约束唯一标识数据库表中的每条记录。

主键必须包含唯一的值。

主键列不能包含 NULL 值。

每个表都应该有一个主键，并且每个表只能有一个主键。

**创建主键约束**

**Mysql**

```mysql
CREATE TABLE Persons
(
Id_P int NOT NULL,
LastName varchar(255) NOT NULL,
FirstName varchar(255),
Address varchar(255),
City varchar(255),
PRIMARY KEY (Id_P)
);
```

**SQL Serer / Oracle /MS Access:**

```plsql
CREATE TABLE Persons
(
Id_P int NOT NULL PRIMARY KEY,
LastName varchar(255) NOT NULL,
FirstName varchar(255),
Address varchar(255),
City varchar(255)
);
```

如需命名PRIMARY KEY ，以及为多个列定义 PRIMARY KEY ，使用以下语句

**Mysql / SQL Serer / Oracle /MS Access:**

```mysql
CREATE TABLE Persons
(
Id_P int NOT NULL,
LastName varchar(255) NOT NULL,
FirstName varchar(255),
Address varchar(255),
City varchar(255),
CONSTRAINT pk_PersonID PRIMARY KEY (Id_P,LastName)
);
```

表已存在，声明PRIMARY KEY，使用以下语句。

**Mysql / SQL Serer / Oracle /MS Access:**

```mysql
ALECT TABLE persons
ADD PRIMARY KEY (Id_p);
```

如果需要命名 PRIMARY KEY 约束，以及为多个列定义 PRIMARY KEY 约束，请使用下面的 SQL 语法：

**Mysql / SQL Serer / Oracle /MS Access:**

```mysql
ALTER TABLE Persons
ADD CONSTRAINT pk_PersonID PRIMARY KEY (Id_P,LastName);
```

**注释：**如果您使用 ALTER TABLE 语句添加主键，必须把主键列声明为不包含 NULL 值（在表首次创建时）。

**撤销PRIMARY KEY 约束**

**Mysql**

```mysql
ALTER TABLE Persons
DROP PRIMARY KEY
```

**SQL Serer / Oracle /MS Access:**

```plsql
ALTER TABLE Persons
DROP CONSTRAINT pk_PersonID
```



### 2.10.4 FOREIGN KEY(外键) 约束

**作用：**

一个表中的FOREIGN KEY指向另个一表中的 PRIMARY KEY。

FOREIGN KEY 约束用于预防破坏表之间连接的动作。

FOREIGN KEY约束也能防止非法数据插入外键列，因为它必须是它指向的那个表中的值之一。

**下面通过一个例子来解释外键约束。**

persons 表

| Id_P | Lastname | Firstname | Address        | City     |
| :--- | :------- | :-------- | :------------- | :------- |
| 1    | Adams    | John      | Oxford Street  | London   |
| 2    | Bush     | George    | Fifth Avenue   | New York |
| 3    | Carter   | Thomas    | Changan Street | Beijing  |

"orders" 表：

| Id_O | OrderNo | Id_P |
| :--- | :------ | :--- |
| 1    | 77895   | 3    |
| 2    | 44678   | 3    |
| 3    | 22456   | 1    |
| 4    | 24562   | 1    |

这里，orders表中的Id_P指向persons表中的Id_P列。

persons 表中的Id_P列是persons表的PRIMARY KEY。

orders 表中的Id_P列是orders表的FOREIGN KEY。

**下面的SQL在orders表创建时为Id_P列创建FOERIGN KEY：**

MySQL

```mysql
CREATE TABLE orders
(
Id_O int NOT NULL,
OrderNo int NOT NULL,
Id_P int,
PRIMARY KEY (Id_O),
FOREIGN KEY (Id_P) REFERENCES Persons(Id_P)
);
```

SQL Server /Oracel /MS Access:

```plsql
CREATE TABLE orders
(
Id_O int NOT NULL PRIMARY KEY,
OrderNo int NOT NULL,
Id_P int FOREIGN KEY REFERENCES Persons(Id_P)
);
```

**如果需要命名 FOREIGN KEY 约束，以及为多个列定义 FOREIGN KEY 约束，请使用下面的 SQL 语法：**

MySQL / SQL Server / Oracle / MS Access:

```mysql
CREATE TABLE orders
(
Id_O int NOT NULL,
OrderNo int NOT NULL,
Id_P int,
PRIMARY KEY (Id_O),
CONSTRAINT fk_PerOrders FOREIGN KEY (Id_P)
REFERENCES Persons(Id_P)
);
```

**如果在 "Orders" 表已存在的情况下为 "Id_P" 列创建 FOREIGN KEY 约束，请使用下面的 SQL：**

Mysql /SQL Server / Oracle /MS Access:

```mysql
ALTER TABLE orders
ADD FOREIGN KEY (Id_P)
REFERENCES Persons(Id_P);
```

**如果需要命名 FOREIGN KEY 约束，以及为多个列定义 FOREIGN KEY 约束，请使用下面的 SQL 语法：**

Mysql / sql server /oracle /MS Access:

```mysql
ALTER TABLE orders
ADD CONSTRAINT fk_PerOrders
FOREIGN KEY (Id_P)
REFERENCES Persons(Id_P);
```

**如需撤销 FOREIGN KEY 约束，命令如下**

mysql

```mysql
ALTER TABLE orders
DROP FOREIGN KEY fk_PerOrders;
```

SQL Server / Oracle / MS Access:

```plsql
ALTER TABLE Orders
DROP CONSTRAINT fk_PerOrders;
```

### 2.10.5 CHECK 约束

**作用：**

CHECK约束用于限制列中的值的范围。

如果对单个列定义CHECK约束，那么该列只允许特定的值。

如果对一个表定义CHECK约束，那么此约束会在特定的列中对值进行限制。

**在创建persons表时创建CHECK约束**

MySQL

```mysql
CREATE TABLE persons
(
Id_P int NOT NULL,
Lastname varchar(255) NOT NULL,
Firstname varchar(255),
Address varchar(255),
City varchar(255),
CHECK (Id_P>0)
);
```

SQL Server / Oracle /MS Access:

```plsql
CREATE TABLE Persons
(
Id_P int NOT NULL CHECK (Id_P>0),
Lastname varchar(255) NOT NULL,
Firstname varchar(255),
Address varchar(255),
City varchar(255)
);
```

**如果需要命名 CHECK 约束，以及为多个列定义 CHECK 约束，请使用下面的 SQL 语法：**

Mysql / SQL Server / Oracle /MS Access:

```mysql
CREATE TABLE Persons
(
Id_P int NOT NULL,
LastName varchar(255) NOT NULL,
FirstName varchar(255),
Address varchar(255),
City varchar(255),
CONSTRAINT chk_Person CHECK (Id_P>0 AND City='Sandnes')
);
```

**在表存在的情况下添加CHECK约束：**

Mysql / SQL Server / Oracle /MS Access:

```mysql
ALTER TABLE persons
ADD CHECK (Id_P>0);
```

**如果需要命名 CHECK 约束，以及为多个列定义 CHECK 约束，请使用下面的 SQL 语法：**

Mysql / SQL Server / Oracle /MS Access:

```mysql
ALTER TABLE Persons
ADD CONSTRAINT chk_Person CHECK (Id_P>0 AND City='Sandnes');
```

**如需撤销 CHECK 约束，请使用下面的 SQL：**

SQL Server / Oracle /MS Access:

```plsql
ALTER TABLE Persons
DROP CONSTRAINT chk_Person;
```

MySQL:

```mysql
ALTER TABLE Persons
DROP CHECK chk_Person;
```

### 2.10.6 DEFAULT 约束

**作用：**

DEFAULT 约束用于向列中插入默认值。

如果没有规定其他的值，那么会将默认值添加到所有的新纪录。

## 2.11 CREAT INDEX语句

**作用：**

create index 语句用于在表中创建索引。

在不读取整个表的情况下，索引使数据库应用程序可以更快地查找数据。

用户无法看到索引，它们只能被用来加速搜索/查询。

**注释：**更新一个包含索引的表需要比更新一个没有索引的表更多的时间，这是由于索引本身也需要更新。因此，理想的做法是仅仅在常常被搜索的列（以及表）上面创建索引。

**语法：**

在表上创建一个简单的索引。允许使用重复的值：

```mysql
CREATE UNIQUE INDEX index_name
ON table_name (column_name);
```

**实例：**

创建一个简单的索引，名为personIndex,在person表中Lastname列：

```mysql
CREAT INDEX personIndex
ON person (Lastname);
```

如果您希望以***降序***索引某个列中的值，您可以在列名称之后添加保留字 *DESC*：

```mysql
CREATE INDEX PersonIndex
ON Person (LastName DESC);
```

假如您希望索引不止一个列，您可以在括号中列出这些列的名称，用逗号隔开：

```mysql
CREATE INDEX PersonIndex
ON Person (LastName, FirstName);
```

## 2.12 DROP 语句：

**作用：**

通过使用DROP语句，可以轻松地删除索引、表、和数据库。

**使用DROP INDEX 命令删除表格中的索引**

MS Access:

```sql
DROP INDEX index_name ON table_name;
```

SQL Server:

```sql
DROP INDEX table_name.index_name;
```

oracle:

```plsql
DROP INDEX index_name;
```

mysql

```mysql
ALTER TABLE table_name DROP INDEX index_name;
```

**删除数据库**

```mysql
DROP DATABASE [database_name];
```

**删除表**

```plsql
DROP TABLE [table_name];
```

## 2.13 ALTER TABLE 语句

**作用：**

ALTER TABLE 语句用于在已有的表中添加、修改或删除列。

**语法：**

在表中添加列

```plsql
ALTER TABLE table_name
ADD column_name datatype
```

删除表中的列

```plsql
ALTER TABLE table_name
DROP COLUMN column_name;
```

**注释：**某些数据库系统不允许这种在数据库表中删除列的方式 (DROP COLUMN column_name)。

要改变表中列的数据类型，请使用下列语法：

```plsql
ALTER TABLE table_name
ALTER COLUMN column_name datatype;
```

