package com.niulijie.jdk8.lambda;

/**
 * @author 86176
 * @create 2021/3/28 23:11
 */
public class LambdaWithParam {
    public static void main(String[] args) {

        /**
         * 1匿名内部类
         */
        ILove iLove = new ILove() {
            @Override
            public void love(int a) {
                System.out.println("1匿名内部类："+a);
            }
        };
        iLove.love(1);

        /**
         * 2简化为lambda表达式
         */
        iLove = (int a)->{
            System.out.println("2简化为lambda表达式："+a);
        };
        iLove.love(2);

        /**
         * 3简化掉参数类型
         */
        iLove = (a)->{
            System.out.println("3简化掉参数类型："+a);
        };
        iLove.love(3);

        /**
         * 4简化掉()
         */
        iLove = a->{
            System.out.println("4简化掉()："+a);
            System.out.println("多行代码用代码块");
        };
        iLove.love(4);

        /**
         * 5简化掉{}
         */
        iLove = a-> System.out.println("5简化掉{}："+a);
        iLove.love(5);
        /**
         * 总结：
         * 1.lambda表示式只能有一行的情况下才能简化成一行，如果有多行，就用代码块包裹
         * 2.必须是函数式接口（接口只有一个方法）,可以使用@FunctionalInterface 修饰,检查是否是函数式接口
         * 3.多个参数也可以去掉参数类型，要去掉就多去掉
         *
         * 语法格式一:无参数,无返回值
         *   () -> System.out.println("Hello world");
         *
         * 语法格式二:有一个参数,并且无返回值
         *   (x) -> System.out.println(x);
         *
         * 语法格式三:若有一个参数,小括号可以省略不写
         *   x -> System.out.println(x);
         *
         * 语法格式四:若有两个以上的参数,有返回值,并且lambda体中有多条语句,需用大括号包围
         *   (x,y) -> {
         *      System.out.println(x)
         *      return Inter.compare(x,y);
         *   }
         *
         * 语法格式五:若lambda体重只有一条语句,return和大括号可以省略不写
         *   Comparator<Integer> com = (x,y) -> Inter.compare(x,y);
         *
         * 语法格式六:lambda表达式参数列表的数据类型可以省略不写,JVM编译器通过上下文推断出数据类型,即"类型推断"
         */
    }
}

interface ILove{
    void love(int a);
}
