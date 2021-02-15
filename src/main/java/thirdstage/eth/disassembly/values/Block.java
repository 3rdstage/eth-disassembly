package thirdstage.eth.disassembly.values;

import java.util.List;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.web3j.protocol.core.methods.response.EthBlock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 3rdstage
 *
 * @see <a href='https://eth.wiki/json-rpc/API#eth_getblockbyhash'>web3j / block object</a>
 */
@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
@Document(collection = "blocks")
public class Block{

  @Id
  private final long no;

  private String hash;

  //@BsonRepresentation(BsonType.DATE_TIME)
  //@Field(targetType = FieldType.DATE_TIME)
  private Timestamp timestamp;

  @Nullable
  public final static Block fromWeb3jBlock(@Nullable EthBlock.Block blk) {

    if(blk == null) return null;
    else{
      return new Block(blk.getNumber().longValue())
                        .setHash(blk.getHash())
                        .setTimestamp(Timestamp.from(
                            Instant.ofEpochSecond(
                                blk.getTimestamp().longValue())));
    }
  }

}
