package thirdstage.eth.disassembly;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ApplicationTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ApplicationContext springContext;


  @Test
  public void testGetBeanDefinitionNames() {

    int cnt = springContext.getBeanDefinitionCount();

    String[] names = springContext.getBeanDefinitionNames();
    Arrays.sort(names);

    this.logger.info("Defined Beans : {}", cnt);
    for(String name: names) {
      this.logger.info("  - {}", name);
    }

    Assertions.assertTrue(cnt > 0, "The Spring container has no bean which makes no sense.");
    Assertions.assertTrue(Arrays.binarySearch(names, "web3j") > -1, "The Spring container doens't contain 'web3j' bean which is never expected.");
    Assertions.assertTrue(Arrays.binarySearch(names, "mvcViewResolver") > -1, "The Spring cotainer doensn't contain 'mvcViewResolver' bean.");
  }

}
