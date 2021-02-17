package thirdstage.eth.disassembly.values;

import java.util.List;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.bson.codecs.pojo.annotations.BsonId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
@Document(collection = "etherTransfers")
public class EtherTransfer implements java.io.Serializable{


  @Id
  private final String hash;

  @Field(name = "block_no")
  private long blockNo;

  private long index;

  private String from;

  // denormalized field from account
  @Field("from_is_contr")
  private boolean fromIsContract;

  private String to;

  // denormalized field from account
  @Field("to_is_contr")
  private boolean toIsContract;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Field(targetType = FieldType.DECIMAL128)
  private BigDecimal value;

  public BigInteger getValue() {
    return this.value.toBigInteger();
  }

  public EtherTransfer setValue(BigInteger val) {
    this.value = new BigDecimal(val);
    return this;
  }

  @Field("at")
  private Timestamp timestamp;


}
