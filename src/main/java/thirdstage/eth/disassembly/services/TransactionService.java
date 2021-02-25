package thirdstage.eth.disassembly.services;

import java.math.BigInteger;
import java.util.Optional;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import thirdstage.eth.disassembly.repos.TransactionRepository;
import thirdstage.eth.disassembly.values.Transaction;

@Service
public class TransactionService{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private Web3j web3j;

  @Autowired
  private TransactionRepository transactionRepo;

  @Autowired
  private AccountService accountSrv;

  @Autowired
  private BlockService blockSrv;

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

      this.logger.info("Found {} transactions in the block {}", cnt, blockNo);
    }catch(Throwable th) {
      ExceptionUtils.wrapAndThrow(th);
    }

    for(long i = 0; i < cnt.longValue(); i++) {
      this.extractTransaction(blockNo, i);
    }
  }


  public void extractTransaction(@PositiveOrZero final long blockNo,
      @PositiveOrZero final long index){

    Optional<org.web3j.protocol.core.methods.response.Transaction> tx = null;
    try {

      this.logger.debug("Extracting transaction {} from the block {}", index, blockNo);

      tx = this.web3j.ethGetTransactionByBlockNumberAndIndex(
        new DefaultBlockParameterNumber(blockNo), BigInteger.valueOf(index)).send().getTransaction();
    }catch(Throwable th) {
      ExceptionUtils.wrapAndThrow(th);
    }

    if(!tx.isPresent()) {
      this.logger.warn("Can't find a transaction - block #: {}, tx index: {}", blockNo, index);
      throw new IllegalStateException(String.format("There's no transaction in block %,d at index %,d", blockNo, index));
    }

    final var fromIsContr = this.accountSrv.isContractAccount(tx.get().getFrom());
    final var toIsContr = this.accountSrv.isContractAccount(tx.get().getTo());
    final var blkNo = tx.get().getBlockNumber().longValue();
    final var at = this.blockSrv.findBlock(blkNo).getAt();

    final var trsf = new Transaction(tx.get().getHash())
                      .setBlockNo(blkNo)
                      .setIndex(tx.get().getTransactionIndex().longValue())
                      .setFrom(tx.get().getFrom())
                      .setFromIsContract(fromIsContr)
                      .setTo(tx.get().getTo())
                      .setToIsContract(toIsContr)
                      .setValue(tx.get().getValue())
                      .setAt(at);

    this.transactionRepo.save(trsf);
  }

}
