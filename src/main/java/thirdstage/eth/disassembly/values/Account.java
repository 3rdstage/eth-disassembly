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
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Account implements java.io.Serializable{

  /**
   *
   */
  private static final long serialVersionUID = -1264157210607478095L;

  @BsonProperty("addr")
  private String address;

  private BigDecimal balance;

  private boolean isContract;

}
