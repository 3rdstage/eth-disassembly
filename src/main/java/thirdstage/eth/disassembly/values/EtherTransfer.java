package thirdstage.eth.disassembly.values;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.bson.codecs.pojo.annotations.BsonId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EtherTransfer implements java.io.Serializable{


  @BsonId
  private String hash;


  private long blockNo;

  private long index;


  private String from;

  private String to;

  private BigDecimal value;



}
