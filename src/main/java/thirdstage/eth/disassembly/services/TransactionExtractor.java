package thirdstage.eth.disassembly.services;

import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;

@Service
public class TransactionExtractor{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private Web3j web3j;


  public void extractTransactions(@Positive final int fromNo, @Positive final int toNo)
    throws Exception{

    BigInteger txs = null;
    for(int i = fromNo; i < toNo; i++) {

      txs = this.web3j.ethGetBlockTransactionCountByNumber(
                new DefaultBlockParameterNumber(i)).send().getTransactionCount();


    }



  }
}
