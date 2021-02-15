package thirdstage.eth.disassembly.services;

import java.math.BigDecimal;
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


  private MongoCollection<Account> accounts;

  @PostConstruct
  public void postConstruct() {

    this.accounts = this.mongo.getDatabase("eth")
        .getCollection("accounts", Account.class);
  }


  public boolean isContractAccount(
      @NotBlank @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") final String addr) {

    return this.findAccount(addr).isContract();
  }


  public Account findAccount(@NotBlank final String addr) {

    Account acct = this.accounts.find(Filters.eq("addr", addr)).first();

    if(acct == null) {

      try {
        final BigInteger bal = this.web3j
            .ethGetBalance(addr, DefaultBlockParameterName.LATEST)
            .send()
            .getBalance();

        final String code = this.web3j
            .ethGetCode(addr, DefaultBlockParameterName.LATEST)
            .send()
            .getCode();

        this.logger.debug(String.format("Found an account - address: %s, balance: %,d, code: %s",
            addr, bal, StringUtils.left(code, 10)));

        acct = new Account(addr, new BigDecimal(bal), !StringUtils.isBlank(code) && !StringUtils.equalsAnyIgnoreCase(code, "0x"));
        this.accounts.insertOne(acct);

      }catch(Throwable th) {
        this.logger.error("Fail to find or insert account data for {}", addr);
        ExceptionUtils.wrapAndThrow(th);
      }
    }

    return acct;
  }

}
