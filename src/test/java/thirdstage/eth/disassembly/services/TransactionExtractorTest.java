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


@SpringBootTest(
    classes = {TransactionExtractor.class, EthereumClientConfig.class, MongoAutoConfiguration.class},
    properties = {"management.endpoints.enabled-by-default=false", "security=", "spring.security.enabled=false"},
    webEnvironment = WebEnvironment.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class TransactionExtractorTest{

  @Autowired
  Web3j web3j;

  @Autowired
  TransactionExtractor testee;

  @Test
  void testExtractTransactions() throws Exception{

    long latest = this.web3j.ethBlockNumber().send().getBlockNumber().longValue();
    long from = Math.max(latest - 1_000_000L, 0L);

    testee.extractTransactions(from, from + 5);
  }

}