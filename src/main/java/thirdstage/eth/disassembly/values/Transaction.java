package thirdstage.eth.disassembly.values;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ch.qos.logback.core.subst.Token.Type;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
@Document(collection = "transactions")
public class Transaction implements java.io.Serializable{

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
  @AccessType(AccessType.Type.FIELD)
  @Field(targetType = DECIMAL128)
  private BigDecimal value;

  public BigInteger getValue() {
    return this.value.toBigInteger();
  }

  public Transaction setValue(BigInteger val) {
    this.value = new BigDecimal(val);
    return this;
  }

  @Indexed
  private Instant at;

}
