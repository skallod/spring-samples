package ru.galuzin.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

public class JdbcMain {
    static {
        SLF4JBridgeHandler.install();
    }
    private static final Logger log = Logger.getLogger(JdbcMain.class.getName());
    private static final org.slf4j.Logger logback = LoggerFactory.getLogger(JdbcMain.class);
    //local
    public static final String ROOT_CRT = "c:/users/[user]/Documents/tasks/postgres/root.crt";
    public static final String SSL_CRT = "c:/users/[user]/Documents/tasks/postgres/client.crt";
    public static final String SSL_KEY = "c:/users/[user]/Documents/tasks/postgres/client.key.pk8";

    public static void main(String[] args) throws SQLException {
        log.info("jjj start");
        logback.debug("lll start");

        String rootCert = "sslrootcert=" + ROOT_CRT;
        String sslCert = "sslcert=" + SSL_CRT;
        String sslKey = "sslkey=" + SSL_KEY;
//        String url = "jdbc:postgresql://[host]:5432/streaming_dev";
//        String url = "jdbc:postgresql://[host]:5432/streaming_dev?loggerLevel=TRACE&ssl=true&sslmode=verify-full&"
//            + rootCert+"&"+sslCert+"&"+sslKey;
        String url = "jdbc:postgresql://postgres.local:5432/postgres";
//        targetServerType=primary

        Properties props = new Properties();
        props.setProperty("ssl","false");
//        props.setProperty("driverClassName", "org.postgresql.Driver");
//        props.setProperty("user","user1");
        props.setProperty("loggerLevel","TRACE");
//        props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
//        props.setProperty("password","user1");
//        props.setProperty("ssl","false");
//        props.setProperty("sslmode","require");
//        props.setProperty("sslmode","verify-full");
//        props.setProperty("sslrootcert",ROOT_CRT);
//        props.setProperty("sslkey",SSL_KEY);
//        props.setProperty("sslcert",SSL_CRT);
//        -Djava.util.config.file=log.properties

//        final HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl(url);
//        hikariConfig.setUsername("postgres");
//        hikariConfig.setPassword("mysecretpassword");
//        hikariConfig.setUsername("streaming_ms");
//        hikariConfig.setPassword("streaming_ms");

        final Properties hikariProps = new Properties();
//        hikariProps.setProperty("jdbcUrl", url);
//        hikariProps.setProperty("username", "postgres");
//        hikariProps.setProperty("password", "mysecretpassword");
        hikariProps.setProperty("connectionTimeout", "70000");
        final HikariConfig hikariConfig = new HikariConfig(hikariProps);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("mysecretpassword");

        hikariConfig.setDataSourceProperties(props);

        logback.info("hikari config {}", hikariConfig);
        final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);


        try (final Connection connection = hikariDataSource.getConnection();
            Statement st = connection.createStatement()) {
            final boolean execute = st.execute("select 1");
//            final boolean execute = st.execute("select count(*) from temp");
            System.out.println("execute = " + execute);
        }
//        try (final Connection connection = DriverManager.getConnection(url, props);
//            Statement st = connection.createStatement()) {
//            final boolean execute = st.execute("select 1");
//            System.out.println("execute = " + execute);
//        }
    }

}
