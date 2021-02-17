package thirdstage.eth.disassembly.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
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
public class AccountServiceTest{

  final private Logger logger =  LoggerFactory.getLogger(this.getClass());

  @Autowired
  Web3j web3j;

  @Autowired
  AccountService testee;

  @Test
  public void testFindAccount() {

    final var acct = testee.findAccount("0x2158d8331b509961b3f739305dfa78f532f1a77a");

    Assertions.assertTrue(StringUtils.isNotBlank(acct.getAddress()));

  }


}
