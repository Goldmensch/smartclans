import net.pretronic.databasequery.api.Database;
import net.pretronic.databasequery.api.collection.field.FieldOption;
import net.pretronic.databasequery.api.datatype.DataType;
import net.pretronic.databasequery.api.driver.DatabaseDriver;
import net.pretronic.databasequery.api.driver.DatabaseDriverFactory;
import net.pretronic.databasequery.api.driver.config.DatabaseDriverConfig;
import net.pretronic.databasequery.sql.dialect.Dialect;
import net.pretronic.databasequery.sql.driver.config.SQLDatabaseDriverConfigBuilder;
import net.pretronic.libraries.document.type.DocumentFileType;
import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.logging.PretronicLoggerFactory;
import net.pretronic.libraries.logging.bridge.slf4j.SLF4JStaticBridge;
import org.junit.Test;

import java.io.File;
import java.net.InetSocketAddress;

public class DatabaseTest {

    private Database database;

    public void testConfig() {
        PretronicLogger logger = PretronicLoggerFactory.getLogger();
        SLF4JStaticBridge.setLogger(logger);

        DatabaseDriverConfig<?> config =
                DatabaseDriverFactory.create(DocumentFileType.YAML.getReader().read(
                        new File("src/test/databases/driver-config.yml")));

        DatabaseDriver driver = DatabaseDriverFactory.create("MyDriver", config, logger);
        driver.connect();
    }

    public void testDriver() {
        PretronicLogger logger = PretronicLoggerFactory.getLogger();
        SLF4JStaticBridge.setLogger(logger);

        DatabaseDriverConfig<?> config = new SQLDatabaseDriverConfigBuilder()
                .setName("H2-Portable")
                .setLocation(new File("src/test/databases/"))
                .setDialect(Dialect.H2_PORTABLE)
                .build();

        DatabaseDriver driver = DatabaseDriverFactory.create("MyDriver", config, logger);

        driver.connect();
        database = driver.getDatabase("test-smartclans");

        createTable();
    }

    private void createTable() {
        database.createCollection("Benutze")
                .field("name", DataType.STRING, FieldOption.PRIMARY_KEY)
                .field("alter", DataType.INTEGER)
                .field("tres", DataType.UUID)
                .create();
    }

    @Test
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
