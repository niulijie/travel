import com.atguigu.admin.AdminApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Junit4测试   需要@SpringBootTest + @RunWith(SpringTest.class)
 * 1、该测试类在测试包test下的包名和类路径java下的包需一致，即
 *  AdminTest所在包名 需要与 com.atguigu.admin.AdminApplication 一致
 * 2、或者通过classes = AdminApplication.class 标明包名
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminApplication.class)
public class AdminTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void test1(){
        System.out.println("##############:"+jdbcTemplate);
    }
}
