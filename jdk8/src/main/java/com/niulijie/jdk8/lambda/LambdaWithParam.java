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
         * 2.必须是函数式接口（接口只有一个方法）
         * 3.多个参数也可以去掉参数类型，要去掉就多去掉
         */
    }
}

interface ILove{
    void love(int a);
}
