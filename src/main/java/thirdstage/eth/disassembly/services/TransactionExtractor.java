package thirdstage.eth.disassembly.services;

import java.util.List;
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

@Service
public class TransactionExtractor{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private Web3j web3j;

  @Autowired
  private MongoClient mongo;

  @Autowired
  private AccountService acctService;

  private MongoCollection<Document> ethTransferCollection = null;


  @PostConstruct
  public void postConstruct() {

    //MongoDatabase and MongoCollection are both 'thread-safe' classes
    this.ethTransferCollection = this.mongo.getDatabase("eth").getCollection("etherTransfers");
  }


  public void extractTransactions(@Positive final long fromNo, @Positive final long toNo)
    throws Exception{

    BigInteger cnt = null;
    for(long i = fromNo; i < toNo; i++) {

      cnt = this.web3j.ethGetBlockTransactionCountByNumber(
                new DefaultBlockParameterNumber(i)).send().getTransactionCount();

      this.logger.info("Found {} transactions in block {}", cnt, i);

      for(long j = 0; j < cnt.longValue(); j++) {
        final Optional<Transaction> tx = this.web3j.ethGetTransactionByBlockNumberAndIndex(
            new DefaultBlockParameterNumber(i), BigInteger.valueOf(j)).send().getTransaction();

        if(!tx.isPresent()) {
          this.logger.warn("Can't find a transaction - block #: {}, tx index: {}", i, j);
          continue;
        }

        final boolean fromContr = this.acctService.isContractAccount(tx.get().getFrom());
        final boolean toContr = this.acctService.isContractAccount(tx.get().getTo());

        Document doc = new Document("blockNo", tx.get().getBlockNumber().longValue())
            .append("txIndex", tx.get().getTransactionIndex().longValue())
            .append("fromAddr", tx.get().getFrom())
            .append("toAddr", tx.get().getTo())
            .append("value", tx.get().getValueRaw());

        this.ethTransferCollection.insertOne(doc);
      }
    }

  }
}
