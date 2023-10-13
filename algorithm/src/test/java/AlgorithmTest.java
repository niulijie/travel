import com.sun.glass.ui.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class AlgorithmTest {

    /**
     * << 左移n 相当于乘以2的n次方
     * >> 右移n 相当于除以2的n次方
     * & 与运算 二进制 1&1 为1，其他都是0
     * ~ 取反操作 0~ 为1； 1~ 为0
     * | 或运算 只要有1就是1
     * ^ 亦或 相同为0，不同为1
     */
    @Test
    public void bitOperation(){

        //int num = 987862434;
        // int num = 2;
        // int num = Integer.MAX_VALUE;
        int num = -1;
        // int num = Integer.MIN_VALUE;
        for (int i = 31; i >= 0; i--) {
            System.out.print((num & 1 << i) == 0 ? "0" : "1");
        }
    }

    @Test
    public void bitOperation2(){
        int b = 12383963;
        int c = ~b;
        printBit(b);
        printBit(c);
        System.out.println("=====================");
        printBit(b & c);
    }

    @Test
    public void bitOperation3(){
        int a = 3597645;
        int b = 12383963;
        printBit(a);
        printBit(b);
        System.out.println("a & b =====================");
        printBit(a & b);
        System.out.println("a | b =====================");
        printBit(a | b);
        System.out.println("a ^ b =====================");
        printBit(a ^ b);
    }

    @Test
    public void bitOperation4(){
        int a = Integer.MIN_VALUE;
        printBit(a); //10000000000000000000000000000000
        // 带符号移动 11000000000000000000000000000000 用符号位来补位
        printBit(a >> 1 );
        // 不带符号移动
        printBit(a >>> 1 );
        System.out.println("=============");
        int b = 1024;
        printBit(b);
        printBit(b >> 1 );
        printBit(b >>> 1 );
        System.out.println("=============");
        int c = Integer.MAX_VALUE;
        printBit(c);
        // 带符号移动 11000000000000000000000000000000 用符号位来补位
        printBit(c << 1 );
        System.out.println("=============");
    }

    @Test
    public void bitOperation5(){
        int a = 5;
        int b = -a;
        System.out.println(a);
        System.out.println(b);
        int c = ~a + 1;
        System.out.println(c);
        int d = Integer.MIN_VALUE;
        int e = ~d + 1;
        System.out.println(d);
        System.out.println(e);
    }

    private void printBit(int num){
        for (int i = 31; i >= 0; i--) {
            System.out.print((num & 1 << i) == 0 ? "0" : "1");
        }
        System.out.println();
    }

    /**
     * 计算：1! + 2! + 3! + 4! +...+ N!
     */
    @Test
    public void f2(){
        int N = 10;
        long ans = 0;
        long cur = 1;
        for (int i = 1; i <= N; i++) {
            cur = cur * i;
            ans += cur;
        }
        System.out.println(ans);
    }

}
