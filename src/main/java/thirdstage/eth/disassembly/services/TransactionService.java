package thirdstage.eth.disassembly.services;

import java.util.List;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.Transaction;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import thirdstage.eth.disassembly.repos.EtherTransferRepository;
import thirdstage.eth.disassembly.values.EtherTransfer;
import thirdstage.utils.NumberComparisonUtils;

@Service
public class TransactionService{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private Web3j web3j;

  @Autowired
  private EtherTransferRepository ethTransferRepo;

  @Autowired
  private AccountService acctService;

  public void extractTransactionsBetweenBlocks(@Positive final long fromNo, @Positive final long toNo)
    throws Exception{

    for(long i = fromNo; i <= toNo; i++) {
      this.extractTransactionsInBlock(i);
    }
  }

  public void extractTransactionsInBlock(@PositiveOrZero final long blockNo) {

    BigInteger cnt = null;
    try {
      cnt = this.web3j.ethGetBlockTransactionCountByNumber(
        new DefaultBlockParameterNumber(blockNo)).send().getTransactionCount();
    }catch(Throwable th) {
      ExceptionUtils.wrapAndThrow(th);
    }

    for(long i = 0; i < cnt.longValue(); i++) {
      this.extractTransaction(blockNo, i);
    }
  }


  public void extractTransaction(@PositiveOrZero final long blockNo,
      @PositiveOrZero final long index){

    Optional<Transaction> tx = null;
    try {

      tx = this.web3j.ethGetTransactionByBlockNumberAndIndex(
        new DefaultBlockParameterNumber(blockNo), BigInteger.valueOf(index)).send().getTransaction();
    }catch(Throwable th) {
      ExceptionUtils.wrapAndThrow(th);
    }

    if(!tx.isPresent()) {
      this.logger.warn("Can't find a transaction - block #: {}, tx index: {}", blockNo, index);
      throw new IllegalStateException(String.format("There's no transaction in block %,d at index %,d", blockNo, index));
    }

    final var fromIsContr = this.acctService.isContractAccount(tx.get().getFrom());
    final var toIsContr = this.acctService.isContractAccount(tx.get().getTo());

    if(NumberComparisonUtils.isPositive(tx.get().getValue())) {  //assume ether transfer

      final var trsf = new EtherTransfer(tx.get().getHash())
                      .setBlockNo(tx.get().getBlockNumber().longValue())
                      .setIndex(tx.get().getTransactionIndex().longValue())
                      .setFrom(tx.get().getFrom())
                      .setFromIsContract(fromIsContr)
                      .setTo(tx.get().getTo())
                      .setToIsContract(toIsContr)
                      .setValue(tx.get().getValue());

      this.ethTransferRepo.save(trsf);
    }
  }

}
