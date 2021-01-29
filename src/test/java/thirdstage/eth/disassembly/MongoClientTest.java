package thirdstage.eth.disassembly;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

@SpringBootTest(
    properties = {"management.endpoints.enabled-by-default=false", "security=", "spring.security.enabled=false"},
    webEnvironment = WebEnvironment.NONE)
@TestInstance(Lifecycle.PER_CLASS)
public class MongoClientTest{

  final private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private MongoClient mongo;


  @Test
  public void listDatabases() {

    final List<String> dbs = new ArrayList<>();

    this.logger.info("MongoDB / databases");
    this.logger.info(mongo.getClusterDescription().toString());

    final MongoDatabase db = mongo.getDatabase("admin");
    mongo.listDatabaseNames().forEach(
        name -> {
          dbs.add(name);
          this.logger.info("  - {}", name);
        });

    Assertions.assertTrue(dbs.contains("admin"));
    Assertions.assertTrue(dbs.contains("config"));
  }

}
