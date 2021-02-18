package thirdstage.eth.disassembly.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import thirdstage.eth.disassembly.repos.BlockRepository;
import thirdstage.eth.disassembly.values.Block;

@Service
public class BlockService{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private Web3j web3j;

  @Autowired
  private BlockRepository blockRepo;

  public Block findBlock(@PositiveOrZero final long no){

    Validate.isTrue(no >= 0);
    return this.blockRepo.findById(no)
        .orElseGet(() -> this.extractBlock(no));

  }

  public Block extractBlock(@PositiveOrZero final long no){

    try {
      final var blk = this.web3j.ethGetBlockByNumber(
        new DefaultBlockParameterNumber(no), false).send().getBlock();

      final var blk2 = Block.fromWeb3jBlock(blk);
      this.blockRepo.save(blk2);
      return blk2;
    }catch(Throwable th) {
      this.logger.error("Fail to extract a block for the block number of {}", no);
      ExceptionUtils.wrapAndThrow(th);
      return null;
    }
  }

}
