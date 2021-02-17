package thirdstage.eth.disassembly.values;

import java.util.List;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
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
@Document(collection = "accounts")
public class Account implements java.io.Serializable{

  /**
   *
   */
  private static final long serialVersionUID = -1264157210607478095L;

  @Id
  final private String address;

  @Field(targetType = FieldType.DECIMAL128)
  private BigDecimal balance;

  //@BsonProperty("is_contr") //not working for Spring Data MongoDB
  @Field(name = "is_contr")
  private boolean isContract;

}
