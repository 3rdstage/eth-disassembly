package thirdstage.eth.disassembly;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions.MongoConverterConfigurationAdapter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import thirdstage.support.spring.data.mongodb.EpochTimeFromDateConverter;
import thirdstage.support.spring.data.mongodb.EpochTimeToDateConverter;

/**
 * @author 3rdstage
 *
 * @see <tt><a href='https://github.com/spring-projects/spring-data-mongodb/blob/3.1.3/spring-data-mongodb/src/main/java/org/springframework/data/mongodb/config/AbstractMongoClientConfiguration.java'>AbstractMongoClientConfiguration.java</a></tt>
 * @see <tt><a href='https://github.com/spring-projects/spring-data-mongodb/blob/3.1.3/spring-data-mongodb/src/main/java/org/springframework/data/mongodb/config/MongoConfigurationSupport.java'>MongoConfigurationSupport.java</a></tt>
 */
@Configuration
@EnableMongoRepositories
public class MongoClientConfig extends AbstractMongoClientConfiguration{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Bean
  @Override
  public MongoClient mongoClient() {
    return super.mongoClient();
  }

  @Override
  protected String getDatabaseName() {
    return "eth";
  }

  @Override
  protected String getMappingBasePackage() {
    return "thirdstage.eth.disassembly.repos";
  }

  // https://docs.spring.io/spring-data/mongodb/docs/3.2.x/reference/html/#mongo.custom-converters.xml
  @Override
  protected void configureConverters(MongoConverterConfigurationAdapter adapter) {
    //adapter.registerConverter(new EpochTimeToDateConverter());
    //adapter.registerConverter(new EpochTimeFromDateConverter());
  }

  @Bean
  public MongoClientSettingsBuilderCustomizer customizer() {
    return (builder) -> {

      List<Convention> cvns = Conventions.DEFAULT_CONVENTIONS;
      //cvns.add(Conventions.SET_PRIVATE_FIELDS_CONVENTION);

      CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
              .automatic(true)
              .conventions(cvns)
              .build()));

      builder.codecRegistry(pojoCodecRegistry);
    };
  }

}
