package tacos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tacos.spring.main.TacoCloudApplication;

@ExtendWith(SpringExtension.class) // <1>
//@SpringBootTest
@SpringBootTest(classes = TacoCloudApplication.class) // Explicitly specify the main class
// <2>
public class TacoCloudApplicationTests {

  @Test                         // <3>
  public void contextLoads() {
  }

}
