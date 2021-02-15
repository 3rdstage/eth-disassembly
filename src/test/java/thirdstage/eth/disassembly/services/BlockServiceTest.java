package thirdstage.eth.disassembly.services;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.Web3j;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class BlockServiceTest{

  @Autowired
  Web3j web3j;

  @Autowired
  BlockService testee;

  @Test
  void testExtractBlock() throws Exception{

    long latest = this.web3j.ethBlockNumber().send().getBlockNumber().longValue();

    testee.extractBlock(RandomUtils.nextLong(100, latest));

  }

}
