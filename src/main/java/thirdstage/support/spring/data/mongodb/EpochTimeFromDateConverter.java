package thirdstage.support.spring.data.mongodb;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * @author 3rdstage
 *
 * @see https://docs.spring.io/spring-data/mongodb/docs/3.2.x/reference/html/#mongo.custom-converters.writer
 */
@ReadingConverter
public class EpochTimeFromDateConverter implements Converter<Date, Long>{


  /**
   *
   */
  // @TODO How about null?
  @Override
  public Long convert(Date src) {

    return src.toInstant().getEpochSecond();
  }



}
