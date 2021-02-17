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

  public void extractBlock(@PositiveOrZero final long no) throws Exception{

    final EthBlock.Block blk = this.web3j.ethGetBlockByNumber(
        new DefaultBlockParameterNumber(no), false).send().getBlock();

    this.blockRepo.save(Block.fromWeb3jBlock(blk));
  }


}
