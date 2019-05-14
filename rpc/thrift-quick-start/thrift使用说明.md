# Thrift使用说明
## 一、下载thrift文件编译器：[地址][1]
[1]: http://thrift.apache.org/download
## 二、thrift文件使用说明
```
/**
* thrift文件的编写教程（大部分语法都是C语言风格的）
*
* 这是thrift文件的注释内容
* 
* 以下是thrift支持的数据类型
*
* bool Boolean, one byte
* i8 有符号8字节的int（byte）
* i16 有符号16字节的int
* i32 有符号32字节的int
* i64 有符号64字节的int
* double 64字节的浮点数
* string 字符串类型
* binary 字节数据byte[]
* map<t1,t2> 键值对,t1为键类型,t2为值类型
* list<t1> list数组
* set<t1> 集合（值唯一）
*
*/


/**
* 导入其他thrift文件
*/
include "shared.thrift"

/**
* 命名空间
* 可根据不同语言不同定义
* 在Java里表现为定义package
*/
namespace cl tutorial
namespace cpp tutorial
namespace d tutorial
namespace dart tutorial
namespace java tutorial
namespace php tutorial
namespace perl tutorial
namespace haxe tutorial
namespace netcore tutorial
namespace netstd tutorial

/**
* 定义常亮
* 复杂的类型使用JSON方式编写
*/
const i32 INT32CONSTANT = 9853
const map<string,string> MAPCONSTANT = {'hello':'world', 'goodnight':'moon'}

/**
* 允许常用类型定义为自定义类型
*/
typedef i32 MyInteger

/**
* 定义枚举类型
* 值是32字节的int类型，可自定义值。如果没有提供，默认从1开始
*/
enum Operation {
    ADD = 1,
    SUBTRACT = 2,
    MULTIPLY = 3,
    DIVIDE = 4
}

/**
* struct是自定义数据类型。
* 字段定义方式：序号 : 类型  字段名 [= 默认值],
* 字段可以声明optional,表示如果值没有传入时不参与序列化.注意：需要语言的支持
*/
struct Work {
    1: i32 num1 = 0,
    2: i32 num2,
    3: Operation op,
    4: optional string comment,
}

/**
* 定义异常类型
*/
exception InvalidOperation {
    1: i32 whatOp,
    2: string why
}

/**
* 定义服务
* 
*/


/**
* 这个是shared.thrift文件里的服务
*
*/
service SharedService {
    SharedStruct getStruct(1: i32 key)
}

/**
* 继承shared.thrift文件里的服务
*
*/
service Calculator extends shared.SharedService {
    //无返回类型的方法
    void ping(),    
    //参数为常用类型的方法
    i32 add(1:i32 num1, 2:i32 num2),    
    //抛出异常的方法
    i32 calculate(1:i32 logid, 2:Work w) throws (1:InvalidOperation ouch),
    //oneway:表示客户端只请求异常，并且完全不监听返回值。所以有改修饰的方法必须是void返回类型
    oneway void zip()
}

```
## 三、代码生成
```
thrift -r -gen <语言名称> <thrift文件名>
```