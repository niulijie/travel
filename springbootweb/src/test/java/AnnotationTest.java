import com.niuljie.springboot.config.AnnotationConfig;
import com.niuljie.springboot.config.AnnotationConfig2;
import com.niuljie.springboot.dto.Pet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationTest {

    @DisplayName("测试@Configuration")
    @Test
    public void test1(){
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnnotationConfig.class);
        Pet pet = annotationConfigApplicationContext.getBean(Pet.class);
        System.out.println(pet);

        String[] beanNamesForType = annotationConfigApplicationContext.getBeanNamesForType(Pet.class);
        for (String s : beanNamesForType) {
            System.out.println(s);
        }

    }

    @DisplayName("测试@ComponentScan")
    @Test
    public void test2(){
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnnotationConfig.class);
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }

    @DisplayName("")
    @Test
    public void test3(){
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnnotationConfig2.class);
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }

        Object pet2 = annotationConfigApplicationContext.getBean("pet2");
        Object pet1 = annotationConfigApplicationContext.getBean("pet2");
        System.out.println(pet1 == pet2);
    }
}
