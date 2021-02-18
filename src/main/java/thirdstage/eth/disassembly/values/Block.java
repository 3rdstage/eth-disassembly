package thirdstage.eth.disassembly.values;

import java.time.Instant;
import javax.annotation.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.web3j.protocol.core.methods.response.EthBlock;
import lombok.Getter;
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
  private Instant at;

  @Nullable
  public final static Block fromWeb3jBlock(@Nullable EthBlock.Block blk) {

    if(blk == null) return null;
    else{
      return new Block(blk.getNumber().longValue())
                        .setHash(blk.getHash())
                        .setAt(Instant.ofEpochSecond(blk.getTimestamp().longValue()));
    }
  }

}
