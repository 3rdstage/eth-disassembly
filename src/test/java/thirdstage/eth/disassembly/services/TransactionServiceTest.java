package thirdstage.eth.disassembly.services;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.web3j.protocol.Web3j;
import thirdstage.eth.disassembly.EthereumClientConfig;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class TransactionServiceTest{

  @Autowired
  Web3j web3j;

  @Autowired
  TransactionService testee;

  @Test
  void testExtractTransactionsBetweenBlocks() throws Exception{

    long latest = this.web3j.ethBlockNumber().send().getBlockNumber().longValue();
    long from = Math.max(latest - 1_000_000L, 0L);

    testee.extractTransactionsBetweenBlocks(from, from + 10);
  }

  @Test
  void testExtractTransactionsInBlock() throws Exception{
    long latest = this.web3j.ethBlockNumber().send().getBlockNumber().longValue();

    testee.extractTransactionsInBlock(latest - 10L);
  }


}
