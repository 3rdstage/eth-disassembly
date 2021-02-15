package thirdstage.support.spring.data.mongodb;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class EpochTimeToDateConverter implements Converter<Long, Date>{

  @Override
  public Date convert(@PositiveOrZero Long epoch) {
    return new java.util.Date(epoch);
  }

}
