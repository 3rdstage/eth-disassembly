package thirdstage.eth.disassembly.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import thirdstage.eth.disassembly.repos.AccountRepository;
import thirdstage.eth.disassembly.values.Account;

@Service
public class AccountService{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private Web3j web3j;

  @Autowired
  private AccountRepository accountRepo;

  public boolean isContractAccount(
      @NotBlank @Pattern(regexp = "0x[0-9A-Fa-f]{1,40}") final String addr){

    return this.findAccount(addr).isContract();
  }

  public Account findAccount(@NotBlank final String addr){

    Validate.isTrue(StringUtils.isNotBlank(addr), "Valid address should be specified.");


    final var acct = this.accountRepo.findById(addr);
    if(acct.isPresent()) return acct.get();

    try{
      final BigInteger bal = this.web3j
          .ethGetBalance(addr, DefaultBlockParameterName.LATEST).send().getBalance();
      final String code = this.web3j
          .ethGetCode(addr, DefaultBlockParameterName.LATEST).send().getCode();

      this.logger.debug(String.format(
          "Found an account - address: %s, balance: %,d, code: %s", addr, bal, StringUtils.left(code, 10)));

      final var acct2 = new Account(addr).setBalance(new BigDecimal(bal))
          .setContract(!StringUtils.isBlank(code) && !StringUtils.equalsAnyIgnoreCase(code, "0x"));

      this.accountRepo.save(acct2);
      return acct2;
    } catch(Throwable th){
      this.logger.error("Fail to find or insert account data for {}", addr);
      ExceptionUtils.wrapAndThrow(th);
      return null;  //unreachable but to avoid compile error
    }
  }

}
