package com.niulijie.jdk8.lambda;

/**
 * 推导lambda表达式
 * @author 86176
 * @create 2021/3/28 22:43
 */
public class Lambda {
    /**
     * 3.静态内部类
     */
    static class Like2 implements ILike{
        @Override
        public void lambda() {
            System.out.println("静态内部类，I like lambda2");
        }
    }

    public static void main(String[] args) {
        //创建接口对象
        ILike like = new Like();
        like.lambda();

        like = new Like2();
        like.lambda();

        /**
         * 4.局部内部类
         */
        class Like3 implements ILike{
            @Override
            public void lambda() {
                System.out.println("局部内部类，I like lambda3");
            }
        }
        like = new Like3();
        like.lambda();

        /**
         * 5.匿名内部类：没有类的名称，必须借助接口或者父类
         */
        like = new ILike() {
            @Override
            public void lambda() {
                System.out.println("匿名内部类，I like lambda4");
            }
        };
        like.lambda();

        /**
         * 6.用lambda简化
         */
        like = ()->{
            System.out.println("lambda简化，I like lambda5");
        };
        like.lambda();
    }
}

/**
 * 1.定义一个函数式接口：任何接口，如果只包含唯一一个抽象方法，那么它就是一个函数式接口
 *    对于一个函数式接口，我们可以通过lambda表达式来创建该接口对象
 */
interface ILike{
    void lambda();
}
/**
 * 2.实现类
 */
class Like implements ILike{
    @Override
    public void lambda() {
        System.out.println("实现类，I like lambda");
    }
}
