import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

/**
 * ● 编写测试方法：@Test标注（注意需要使用junit5版本的注解）
 * ● Junit类具有Spring的功能，@Autowired、比如 @Transactional 标注测试方法，测试完成后自动回滚
 */
@DisplayName("Junit5功能测试类")
public class Junit5Test {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String environment = "DEV";

    /**
     * 为测试类或者测试方法设置展示名称
     */
    @DisplayName("测试DisplayName注解")
    @Test
    void testDisplayName(){
        System.out.println(1);
        System.out.println(jdbcTemplate);
    }


    @Disabled //表示测试类或测试方法不执行，类似于JUnit4中的@Ignore
    @DisplayName("测试方法2")
    @Test
    void test2(){
        System.out.println(2);
    }

    @BeforeEach //表示在每个单元测试之前执行
    void testBeforeEach(){
        System.out.println("测试要开始了....");
    }

    @AfterEach //表示在每个单元测试之后执行
    void testAfterEach(){
        System.out.println("测试要结束了....");
    }

    @BeforeAll //表示在所有单元测试之前执行
    static void testBeforeAll(){
        System.out.println("所有测试就要开始了....");
    }

    @AfterAll //表示在所有单元测试之后执行
    static void testAfterAll(){
        System.out.println("所有测试已经结束了....");
    }

    /**
     * 表示测试方法运行如果超过了指定时间将会返回错误
     * @throws InterruptedException
     */
    @Timeout(value = 5, unit = TimeUnit.MILLISECONDS)
    @Test
    void testTimeout() throws InterruptedException {
        Thread.sleep(50);
    }

    /**
     * 断言：前面断言失败，后面的代码都不会执行
     * assertEquals --> 判断两个对象或两个原始类型是否相等
     * assertNotEquals --> 判断两个对象或两个原始类型是否不相等
     * assertSame --> 判断两个对象引用是否指向同一个对象
     * assertNotSame --> 判断两个对象引用是否指向不同的对象
     * assertTrue --> 判断给定的布尔值是否为 true
     * assertFalse --> 判断给定的布尔值是否为 false
     * assertNull --> 判断给定的对象引用是否为 null
     * assertNotNull --> 判断给定的对象引用是否不为 null
     */
    @DisplayName("测试简单断言")
    @Test
    void testSimpleAssertions(){
        int cal = cal(2, 3);
        //Assertions.assertEquals(5, cal);
        //assertEquals(5, cal);
        //assertNotEquals(5, cal);
        //assertEquals(6, cal); //org.opentest4j.AssertionFailedError
        //assertEquals(6, cal, "业务逻辑计算失败"); //org.opentest4j.AssertionFailedError: 业务逻辑计算失败

        Object o1 = new Object();
        Object o2 = new Object();
        //assertSame(o1, o2, "两个对象不一样");
        //assertNotSame(o1, o2, "两个对象一样");

        //assertTrue(1 > 2);
        assertFalse(1 > 2);

        assertNull(null);
        assertNotNull(null);
    }

    int cal(int i, int j){
        return i+j;
    }

    /**
     * 通过 assertArrayEquals 方法来判断两个对象或原始类型的数组是否相等
     */
    @Test
    @DisplayName("array assertion")
    public void array() {
        assertArrayEquals(new int[]{1, 2}, new int[] {1, 2});
        assertArrayEquals(new int[]{2, 1}, new int[] {1, 2});
    }

    /**
     * 通过 assertAll 所有断言都需要成功
     */
    @Test
    @DisplayName("组合断言")
    public void all() {
        assertAll("testAll",
                () -> assertTrue(true && false, "结果不为true"),
                () -> assertEquals(1, 1, "结果不是1"));
        System.out.println("==================");
    }

    @Test
    @DisplayName("异常测试")
    public void exceptionTest() {
        Assertions.assertThrows(
                //扔出断言异常-断定业务逻辑一定出现异常
                ArithmeticException.class, () -> {
                    int i = 10 % 2;
                }, "业务逻辑竟然正常运行了？");
    }

    @Test
    @DisplayName("超时测试")
    public void timeoutTest() {
        //如果测试方法时间超过1s将会异常
        Assertions.assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(500));
    }

    @Test
    @DisplayName("快速失败")
    public void shouldFail() {
        if(2 == 2){
            fail("测试失败");
        }
    }

    @Test
    @DisplayName("测试前置条件")
    public void testAssumptions() {
        Assumptions.assumeTrue(true, "结果不是true");
        //assumingThat 的参数是表示条件的布尔值和对应的 Executable 接口的实现对象。只有条件满足时，Executable 对象才会被执行；当条件不满足时，测试执行并不会终止。
        assumingThat(Objects.equals(this.environment, "DEV"), () -> {
            System.out.println("In DEV");
        });
    }
}
