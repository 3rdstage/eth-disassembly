package thirdstage.eth.disassembly.repos;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import thirdstage.eth.disassembly.values.Block;
import thirdstage.eth.disassembly.values.Transaction;

public interface TransactionRepository  extends MongoRepository<Transaction, String>{


}
