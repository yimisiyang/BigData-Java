# 1.`public class`与`class`区别 #
`public class`定义的类：若一个类声明的时候用了`public class`声明，则类名必须与文件名称完全一致，且一个文件中只有一个`public class`类。
`class`定义的类，类名可以与文件名不一致，但是执行的时候是生成后的类名称。

# 2.JAVASE基础（一）#

## 1.java中的关键字 ##

abstract | continue | for | new | switch
-|-|-|-|-|-
asser | default | goto | package | synchronized
boolean | do | if | private | this
break | double | implements | protected | throw
byte | else | import | public | throws 
case | enum |instanceof | return | transient
catch | extends | int | short |try
char | final | interface | static | void 
class | finally | long | strictfp | volatile 
const | float | native | super | while

**注意：**上述关键字中goto 和 const 不在使用。

## 2.标识符 ##

**定义：** 标识符可以标识类名、接口名变量名、方法名。

**命名规则：**
（a）标识符由数字、字母、下划线、$和￥构成。其它符号不可以

（b）标识符不能以数字开头。

**特点：**
（a）关键字不能作为标识符。

（b）标识符区分大小写。

（c）标识符理论上没有长度限制。

## 3.变量（Var）##

变量是内存中用来存放特定数据类型数据的一块空间，它的值是可以改变的。java中的变量有四个基本属性：**变量名、变量的数据类型、存储单元、变量值**。

**变量的声明格式：**

类型 变量名;

## 4.数据类型 ##

Java共有两种数据类型，**基本类型**和**引用类型**，基本类型有**8**种，引用数据类型有**3**种。

**（a）基本数据类型**

整数型（byte,short,int,long）

浮点型（float，double）

字符类型（char）

布尔类型（boolean,只能取值true或false）


**（b）引用数据类型**

数组、类、接口

## 5.运算符 ##

运算符种类 | 符号
-|-
赋值运算符 | = 、+=、-=、*=、/=、%=
算术运算符 | ++、--、 + 、-、*、/、%
关系运算符 | > 、>= 、<、<=、==、!=
逻辑运算符 | &&、II、!、&、I
三目运算符 | a?b:c

## 6.Scanner ##
**作用：** 接收键盘输入，实现人机交互。

导入依赖包

    import java.util.Scanner;

语法格式

    Scanner input = new Scanner(system.in);

## 7.`System.out.print`和`System.out.println` 区别##

`System.out.print`输出后不追加换行。

`System.out.println` 输出后追加一个换行。

## 8.java语法 ##

1.`for`语句

```
for(初始化表达式1；布尔表达式2；表达式3){
	语句；
	
}
```
2.foreach语句

```
for(type x:obj){
	引用了x的java语句；
}
```

type x循环变量，依次将obj中的值赋给x。

obj 一个可以被遍历的集合，例如数组。

例：输出数组中的所有元素

```
int arr[] = {7,10,11,56,99}
for(int x:arr){
	System.out.println(x);
}
```

3.`switch`语句

```
switch(用于判断的参数){
	case 常量表达式1：语句1;break;
	case 常量表达式2：语句2;break;
	...
	case 常量表达式n：语句n;break;
	default:语句n+1;break;
}
```

## 9.字符串 ##

1.声明字符串就是创建一个字符串对象。

**语法：** `String a;``String a,b,c;`

**注意：** 像这种声明没有赋值的时候相当于`String a = null;`

2.给字符串赋值的方法

（a）引用字符串常量

（b）利用构造方法直接实例化

语法：`public String（String original）`original是文本内容。

```
String a = new String("I love You");
String b = new String(a);
```
（c）利用字符数组实例化（了解）

语法1：`public String（char[]value）`value是一个字符数组。

```
char[] charArray = {'t','i','m','e'};
String a = new String(charArray);
```
语法2：`public String(char[] value,int offest,int count)` value 字符数组 offset 起始位置 count 获取个数

```
char[] charArray = {'t','i','m','e'};
String a = new String(charArray,3,2);
```
（d）利用字节数组实例化（了解）

**注意：**
1.java中连接字符串采用‘+’号实现。

2.使用String类中的length()方法获取声明字符串对象的长度。
语法：`str.length();`

3.String类中提供了两种搜索字符或字符串的方法，即`indexOf()`和`lastIndexOf()`前者返回搜索字符或字符串第一次出现的地方，后者返回最后一次出现的地方。返回值为一个索引。

例

```
String str = "We are students";
int size = str.indexOf("a")  //变量size值为3
```
4.使用String中的`charAt()`方法可将指定索引处的字符串返回。

5.通过String类中的`substring()`方法可以对字符串进行截取。
语法1：`str.substring(int beginIndex)`beginIndex指定从某一索引处开始截取字符串。

语法2：`str.substring(int beginIndex，int endIndex)` beginIndex起始位置，endIndex结束位置。

6.`trim()`方法返回字符串的副本，忽略前导空格和尾部空格。

7.`replace()`方法实现将指定字符或字符串换成新的字符或字符串。
语法：`str.replace(char oldChar, char newChar)`

8.`startsWith()`方法和endsWith()方法分别用于判断字符串是否以指定的内容开始或结束。该方法返回`boolean`类型。

9.使用`equals（）`方法进行比较字符串是否相等。

## 10.正则表达式 ##
**定义：** 正则表达式是一组公式，描述了一种字符串匹配的格式。

**作用：** 常用来检查某一字符串是否满足某一格式。可以调用`String`中的`matches(String regex)`方法，判断字符串是否匹配给定的正则表达式，**返回布尔值**。

正则表达式中有**元字符**和**限定符**两种。具体代表含义可以查询手册。

## 11.StringBuffer类 ##

**1.作用：** `StringBuffer`是线程安全的可变字符序列。一个类似于`String`的字符缓冲区。`String`创建的字符串对象是不可修改的，而`StringBuffer`类创造的是可修改的字符串序列，且实体容量会随着存放字符串增加而增加。

**2.创建StringBuffer类**

创建`StringBuffer`类必须使用`new`方法，不能像`String`对象直接引用字符串常量。

```
StringBuffer sbf = new StringBuffer();  //创建一个对象，无初始值
StringBuffer sbf = new StringBuffer("abc");  //创建一个对象，初始值为“abc”
StringBuffer sbf = new StringBuffer(32);  //创建一个对象，初始容量为32个字符。32为`int`型

```

new StringBuffer(32)|new String("32")
-|-|-
初始字符序列容量为32字符 | 初始字符序列值为 “32”
内存所占容量：32字符 | 内存所占容量：16字符

**3.常用的方法**

**追加字符串**

    public StringBuffer append（Object obj）

例：

```
StringBuffer sbf = new StringBuffer("门前大桥下，")；
sbf.append("游过一群鸭")；
StringBuffer tmp = new StringBuffer("快来数一数，")；
sbf.append(tmp);
int x = 12345;
sbf.append(x);
```
**修改指定索引处的字符**

    public void setCharAt（int index，char ch）

例

```
StringBuffer sbf = new StringBuffer("12345");
sbf.setCharAt(3,'A');
```

**插入字符串**

    public StringBuffer insert(int offset,String str)

**字符串的反序**

    public StringBuffer reverse()

**删除子字符串**

    public StringBuffer delate（int start，int end）

该方法从指定索引start处开始到索引end-1处。若end-1超过了最大索引范围，则一直到尾部。当start=end时，不发生变化。

**与String类似的方法**

如`length()`获取字符串长度；`charAt()`获取索引下的字符；`indexOf()`获取字符串所在索引位置；`substring()`截取字符串；`replace()`替换字符串。

## 12.StringBuilder类 ##

该类提供了一个与`StringBuffer`类兼容的API，但是是现场不安全的，不保证线程同步，但是运行效率要比`StringBuffer`高。

**注意：**
StringBuffer、StringBuilder、String 可以互相转换。

方法1：string 转 stringbuffer和stringbuilder

```
String str = "String";
StringBuffer sbf = new StringBuffer(str);
StringBuffer sbd = new StringBuilder(str);
```
方法2：stringbuffer和stringbuilder转string调用`toString()`方法

```
str =sbf.toString();
str =sbd.toString();
```

方法3：stringbuffer转builder 或stringbuilder转stringbuffer

```
StringBuilder bufferToBuilder = new StringBuilder（sbf.toString()）;
StringBuffer builderToBuffer = new StringBuffer（sbd.toString()）;
```

## 13.数组 ##

**1常见的一维数组**

整形数组、小数数组、字符数组、字符串数组

**2.创建一维数组**

语法：

```
数组元素类型 数组名字[];
数组元素类型[] 数组名字;
```

初始化（三种方法）：

方法一：每个元素分别赋值

方法二： 初始化时赋值 `int b[] = new int[]{4,5,6}`

方法三：省去new int[] `int b[] = {4,5,6}`

**3.二维数组**

创建二维数组与一维数组类似。

二维数组的初始化

    int tdarr1[][] = {{1,2,5},{6,5,7}}
    int tdarr1[][] = new int[][]{{1,2,5},{6,5,7}}
    
	int tdarr3[][] = new int[2][3];
    
	tdarr3[0] = new int[]{6,54,71}
    
	tdarr3[1][0] = 1;
    tdarr3[1][0] = 1;
	tdarr3[1][2] = 1;

**填充和批量替换数组元素的方法**

`Arrays.fill(arr, int value)`  //arr数组 ，value填充的值。

`Arrays.fill(arr, int formIndex, int tolndex, int value)` 

**对数组进行排序**

    Arrays.Sort()方法 //只能进行升序排序。

例

```
int a[] = new int[]{65, 12, 48, 98, 7};
Arrays.sort(a);
```
**复制数组**

 语法

```
Arrays.copyOf(arr, newlength)  //newlength 指复制后的数组长度
Arrays.copyOfRange(arr,formIndex,tolndex)
```

# 3.面向对象 #

## 1.面向对象的三或四个特征 ##

封装、继承、多态、（抽象）

封装：面向对象编程的核心思想，将对象的属性和行为封装起来。

继承：处理一个问题时，可以将一些有用的类保留下来，这些类通常有相同的属性甚至相同的方法，当遇到同样问题时可以拿来复用。

继承主要利用特定对象之间的共同属性。

多态：将父类对象应用于子类的特征就是多态。

## 2.类 ##

**1.成员变量**

成员变量就是java中类的属性。

**2.成员方法**

成员方法就是java中类的行为。

**3.局部变量**

如果在成员方法内定义一个变量，这个变量被称为局部变量。局部变量在方法执行时创建，在方法执行结束时被销毁。局部变量在使用时必须进行赋值操作或被初始化。

**成员变量不初始化不会被报错，局部变量会报错。**

有效范围：局部变量从声明开始，到结束为止。在互补嵌套区域中可以定义相同局部变量，在嵌套区域中不可以定义相同名称和类型的局部变量。

**4.this 关键字**

java提供了this关键字，在类中，this代表类本身的对象。

this可以调用类的成员变量和成员方法，事实上this还可以调用类中的构造方法。

例

```
public class Book{
String name = "cxk"
public void showName(String name){
	System.out.println(this.name)
	}
public static void main(String[] args){
	Book b = new Book();
	b.showName("abc")
	}
}
```
如果是`this.name`输出为cxk,如果是`name`输出为abc.

```
public class Mytest{
	int i;
	public Mytest(){
		this(1);     //使用this调用本类的构造方法
	}
	public Mytest(int i){
	this.i = i;   //使用this调用本类成员变量。	
	}

}
```

**5.权限修饰符**

**private：** 本类可见，同包其他类或子类不可见，其他包的类或子类也不可见。

**protected：** 本类可见，同包其他类或子类可见，其他包的类或子类也不可见。

**public：** 本类可见，同包其他类或子类可见，其他包的类或子类也可见。

**6.类的构造方法**

构造方法是一个与类同名的方法，对象的创建就是通过构造方法完成的。每当类实例化一个对象时，类都会自动调用构造方法。

**构造方法，就是创建类的对象过程中运行的方法，也就是对象的初始化方法。**

**构造方法没有返回值，但和普通没有返回值的方法不同，普通没有返回值的方法需要`void`修饰，但是构造方法不需要。**

```
public class Bird{
	public Bird(){}   //Bird类的构造方法
}
```

**对象初始化：** 在构造方法中可以为成员变量赋值，这样当实例化一个本类的对象时，相应的成员变量也将被初始化。如果类中没有明确定义构造方法，则编译器会自动创建一个不带参数的默认构造方法。

**私有构造方法**

构造方法同其他方法一样，也可以用private修饰，私有构造方法无法在本类外部使用，也就导致本类无法用new实例化，这样可以控制对象的生成。

## 13.静态修饰符 ##

由static修饰的变量、常量和方法被称作静态变量、静态常量和静态方法，他们都存放在内存的“静态区”中，这些变量和方法有独立的生存周期。

内存中的静态区在整个程序运行结束之后才会释放，所以用静态修饰的代码的生命周期，是整个程序的生命周期。

**静态区** 内存中静态区的变量可以被本类共享，其他类调用本类静态变量和静态方法时，无需实例化。**静态区就是内存的公共区**

**java程序中，把共享的变量用static修饰，该变量就是静态变量。**

**静态常量** 用`final static` 修饰一个成员变量，这个成员变量就会变成一个静态常量。

**注意：静态常量一定是大写的，这是java编码规范。静态常量不能被修改。**

**静态方法:** 若想要使用类中的成员方法，需要先将这个类进行实例化，但有些时候我们不想或者无法创建类的对象时，还要调用类中的方法才能够完成业务逻辑，此时我们可以使用静态方法。调用类的静态方法，无需创建类的对象。

其实我们一直用的`System.out.println()`方法和类中的Main方法都是静态方法。

在类中成员方法之外，用static修饰代码区域可以称之为静态代码块，定义一块静态代码块，可以完成类的初始化操作，再类声明时就会运行。

```
public class StaticTest {
    static{
        System.out.println("这里是静态代码块！！！");
    }
    {
        System.out.println("这里是非静态代码块！！！")
    }
    public StaticTest{
        System.out.println("这里是构造方法！！！")
    }
    public void method(){
        System.out.println("这里是成员方法！！！")
    }
    public static void main(String[] args){
        StaticTest test = new StaticTest();
        test.method();
    }
}

```
 
**运行顺序** 静态代码块->非静态代码块->构造方法->成员方法(调用时才会运行。)

## 14.类的主方法 ##

每个类都可以有main方法，但是每个项目必须指定唯一的main方法。

主方法是类的入口点，它定义了程序从何处开始；主方法提供对程序流向的控制，java编译器通过主方法来执行程序。

**主方法语法**

```
public static void main(String[] args){
	//方法体
}
```

**注意：** 

主方法是静态的，所以在主方法中调用其他方法，则该方法必须也是静态的。若不是静态的，必须创建对象的实例。

主方法没有任何返回值。

主方法的形参是数组其中 args[0]~args[n]分别代表程序的第一个参数到第n个参数，可以使用args.length获取参数的个数。

## 15.对象 ##

**1.对象的创建**

java语言中使用new操作符调用构造方法就可以创建一个对象。

例

```
Test test = new Test();
Test test = new Test("a");
```

特殊例子：

    String str = "abc";

这里str也是一个对象，虽然代码里没有new关键字，但Java虚拟机在创建字符串的同时，也创建了一个匿名的字符串对象，并提交给str引用。

**2.对象的引用**

在Java语言中尽管一切都可以看作引用，但真正的操作标识符实质上是一个引用。

    Book book = new Book（）；

Book是类,创建一个对象；book是常说的对象其实是引用，因为它可以调用对象的成员变量和成员方法;new Booke() 真正的对象实体。

引用只是存放一个对象的内存地址，并非存放一个对象，严格地说引用和对象是不同的，但是可以将这种区别忽略，如可以简单地说book是Book类的一个对象，而事实上应该是book包含Book对象的一个引用。

 **3.对象的比较**

java中有两种对象比较方式`==` 和 `equals()`,这两种比较有本质的区别。

使用`==`比较的是两个对象引用的地址是否相等。而`equals`比较的是内容是否相同。

**4.对象的销毁**

对象引用超过其作用范围，这个对象将被视为垃圾.

将对象赋值为null.

# 4.包装类 #

## 1.Integer类 ##

java.lang 包中的Integer类、Long类、Short类。包含了基本类型int、long、short。这些类都是Number的子类。

**Integer构造方法：**

例：

    Integer（int number）；//方法1

    Integer number = new Integer(7);

例：

    Integer(String str); //方法2

    Integer number = new Integer("45");

## 2.Boolean类 ##

Boolean类将基本类型为boolean的值包装在一个对象中。一个Boolean类型的对象只包含一个类型为boolean 的字段。

**Boolean类构造方法**

例：

    Boolean（boolean value） //方法
    Boolean b = new Boolean(true);

**Boolean 提供了3个常量。**

TRUE：对应基值true的Boolean对象。

FALSE：对应基值false的Boolean对象。

TYPE:基本类型boolean的Class对象。

