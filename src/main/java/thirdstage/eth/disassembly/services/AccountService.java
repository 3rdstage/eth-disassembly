package thirdstage.eth.disassembly.services;

import java.math.BigInteger;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterName;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import thirdstage.eth.disassembly.values.Account;

@Service
public class AccountService{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());


  @Autowired
  private Web3j web3j;


  @Autowired
  private MongoClient mongo;

  MongoCollection<Document> acctCollection = null;

  @PostConstruct
  public void postConstruct() {

    this.acctCollection = this.mongo.getDatabase("eth")
        .getCollection("accounts");
  }


  public boolean isContractAccount(
      @NotBlank @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") final String addr) {



    return false;


  }


  public Account findAccount(@NotBlank final String addr) {

    Document doc = this.acctCollection.find(Filters.eq("addr", addr)).first();

    if(doc == null) {

      try {
        final BigInteger bal = this.web3j
            .ethGetBalance(addr, DefaultBlockParameterName.LATEST)
            .send()
            .getBalance();

        final String code = this.web3j
            .ethGetCode(addr, DefaultBlockParameterName.LATEST)
            .send()
            .getCode();

        this.logger.debug(String.format("Inquired an acccount - address: %s, balance: %,d, code: %s",
            addr, bal, StringUtils.left(code, 10)));

        doc = new Document("addr", addr)
            .append("balance", bal)
            .append("is_contr", !StringUtils.isBlank(code));

        this.acctCollection.insertOne(doc);

      }catch(Throwable th) {
        this.logger.error("Fail to find or insert account data for {}", addr);
        ExceptionUtils.wrapAndThrow(th);
      }
    }

    return null;
  }

}
