package thirdstage.eth.disassembly;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroundTest{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void echoEnvVars() {

    var env =  System.getenv();
    for(var entry : env.entrySet()) {
      logger.info("{} : {}", entry.getKey(), entry.getValue());
    }
  }


}
