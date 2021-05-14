import net.pretronic.databasequery.api.Database;
import net.pretronic.databasequery.api.collection.DatabaseCollection;
import net.pretronic.databasequery.api.collection.field.FieldOption;
import net.pretronic.databasequery.api.datatype.DataType;
import net.pretronic.databasequery.api.driver.DatabaseDriver;
import net.pretronic.databasequery.api.driver.DatabaseDriverFactory;
import net.pretronic.databasequery.api.driver.config.DatabaseDriverConfig;
import net.pretronic.databasequery.sql.dialect.Dialect;
import net.pretronic.databasequery.sql.driver.config.SQLDatabaseDriverConfigBuilder;
import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.logging.PretronicLoggerFactory;
import net.pretronic.libraries.logging.bridge.slf4j.SLF4JStaticBridge;
import org.junit.Test;

import java.io.File;
import java.net.InetSocketAddress;

public class DatabaseTest {

    private Database database;

    @Test
    public void testDriver() {
        PretronicLogger logger = PretronicLoggerFactory.getLogger();
        SLF4JStaticBridge.setLogger(logger);

        DatabaseDriverConfig<?> config = new SQLDatabaseDriverConfigBuilder()
                .setName("H2-Portable")
                .setLocation(new File("databases/"))
                .setDialect(Dialect.H2_PORTABLE)
                .build();

        DatabaseDriver driver = DatabaseDriverFactory.create("MyDriver", config, logger);

        driver.connect();
        database = driver.getDatabase("test-smartclans");

        //createTable();
    }

    private void createTable() {
        DatabaseCollection customers = database.createCollection("Benutze")
                .field("name", DataType.STRING, FieldOption.PRIMARY_KEY)
                .field("alter", DataType.INTEGER)
                .create();
    }

    public void testDriverMaria() {
        PretronicLogger logger = PretronicLoggerFactory.getLogger();
        SLF4JStaticBridge.setLogger(logger);

        DatabaseDriverConfig<?> config = new SQLDatabaseDriverConfigBuilder()
                .setName("MariaDb")
                .setAddress(new InetSocketAddress("127.0.0.1", 3306))
                .setDialect(Dialect.MARIADB)
                .setUsername("root")
                .setPassword("root")
                .build();

        DatabaseDriver driver = DatabaseDriverFactory.create("MyDriver", config, logger);

        driver.connect();
        database = driver.getDatabase("test-smartclans");

        createTable();
    }

}
