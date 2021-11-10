package net.ttddyy.dsproxy.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.example.proxy.ReturnProxyFactory;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import oracle.jdbc.OraclePreparedStatement;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Spring java based configuration using {@link net.ttddyy.dsproxy.support.ProxyDataSourceBuilder}.
 *
 * @author Tadaya Tsuyukubo
 */
@SpringBootApplication
public class Application {

    ThreadLocal<Boolean> isReturningParamsRegistered = ThreadLocal.withInitial(() -> false);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    // use hibernate to format queries
    private static class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {
        private Formatter formatter = FormatStyle.BASIC.getFormatter();

        @Override
        protected String formatQuery(String query) {
            return this.formatter.format(query);
        }
    }

    @Bean
    public DataSource hikariDataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String user,
            @Value("${spring.datasource.password}") String pas,
            @Value("${spring.datasource.driverClassName}") String dr
            ) {
        System.out.println("gal 78");
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pas);
        hikariConfig.setDriverClassName(dr);
        return new HikariDataSource(hikariConfig);
    }

    @DependsOn("hikariDataSource")
    @Bean
    @Primary
    public DataSource dataSourceProxy(DataSource actualDataSource) {
        // use pretty formatted query with multiline enabled
        PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
        creator.setMultiline(true);

        SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
        listener.setQueryLogEntryCreator(creator);

        return ProxyDataSourceBuilder
                .create(actualDataSource)
                .name("MyDS")
                .listener(new QueryExecutionListener() {
                    @Override
                    public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
                        //todo skip
                        System.out.println("before query");
                        try {
                            Statement statement = execInfo.getStatement();
                            System.out.println("statement = " + statement);
                            oracleBeforeQuery(statement);
                        } catch (Exception throwables) {
                            throwables.printStackTrace();
                            throw new RuntimeException(throwables);
                        }
                    }

                    @Override
                    public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
                        System.out.println("after query");
                        try {
                            Statement statement = execInfo.getStatement();
                            //statement.getResultSet()
                            System.out.println("statement = " + statement);
                            oracleAfterQuery(statement);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .proxyResultSet()  // enable resultset proxy
                .parameterTransformer((replacer, transformInfo) -> {
                    System.out.println("transformer");
//                    replacer.registerOutParameter(2, Types.NUMERIC);
//                    replacer.registerOutParameter(3, Types.VARCHAR);
//                  not work  transformInfo.setClazz(OraclePreparedStatement.class);
                })
                .beforeMethod(methodExecutionContext-> {
                    Method method = methodExecutionContext.getMethod();
                    Class<?> targetClass = methodExecutionContext.getTarget().getClass();
                    System.out.println("JDBC: " + targetClass.getSimpleName() + "#" + method.getName());
                    if ("prepareStatement".equals(method.getName())) {
                        final Object[] methodArgs = methodExecutionContext.getMethodArgs();
                        if (methodArgs.length > 1) {
                            if (methodArgs[1] instanceof String[]) {
                                String[] originReturnArgs = (String[]) methodArgs[1];
                                final String[] returnArgs = new String[originReturnArgs.length + 1];
                                System.arraycopy(originReturnArgs, 0, returnArgs, 0, originReturnArgs.length);
                                returnArgs[returnArgs.length - 1] = "until";//todo collect columns
                                methodArgs[1] = returnArgs;
                            }
                            isReturningParamsRegistered.set(true);
//                            methodExecutionContext.setMethodArgs(returnArgs)
                        }
                    }
                })
                .afterMethod(executionContext -> {
                    // print out JDBC API calls to console
//                    Method method = executionContext.getMethod();
//                    Class<?> targetClass = executionContext.getTarget().getClass();
//                    System.out.println("JDBC: " + targetClass.getSimpleName() + "#" + method.getName());
                })
                .jdbcProxyFactory(new ReturnProxyFactory())
//                .beforeQuery(new ProxyDataSourceBuilder.SingleQueryExecution() {
//                    @Override
//                    public void execute(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
//                        System.out.println("execInfo = " + execInfo.getStatementType());
//                    }
//                })
//                .afterQuery((execInfo, queryInfoList) -> {
//                    System.out.println("Query took " + execInfo.getElapsedTime() + "msec");
//                })
                .build();
    }

    private void oracleBeforeQuery(Statement statement) throws SQLException {
        OraclePreparedStatement ops = OracleStatementUnwrapper.unwrap(statement);
        if (isReturningParamsRegistered.get()) {
            // params already registered
            return;
        }
        ops.registerReturnParameter(3, Types.NUMERIC);
        ops.registerReturnParameter(4, Types.VARCHAR);
        ops.registerReturnParameter(5, Types.VARCHAR);
    }

    private void oracleAfterQuery(Statement statement) throws SQLException {
        OraclePreparedStatement ops = OracleStatementUnwrapper.unwrap(statement);
        if (isReturningParamsRegistered.get()) {
            isReturningParamsRegistered.remove();
            ResultSet rs = ops.getGeneratedKeys();
            int size = rs.getMetaData().getColumnCount();
            while(rs.next())
            {
                for (int i = 1; i <= size; i++) {
                    if("TIMESTAMP".equals(rs.getMetaData().getColumnTypeName(i))){
                        System.out.println("gal after query " + rs.getMetaData().getColumnName(i) + " ; " + rs.getDate(i));
                    } else {
                        System.out.println("gal afger query " + rs.getMetaData().getColumnName(i) + " ; " + rs.getObject(i));
                    }
                }
            }
        } else {
            ResultSet rs = ops.getReturnResultSet();
            rs.next();
            System.out.println("gal after query " + rs.getObject(1));//not work rs.getMetaData().getColumnName(1));
            System.out.println("gal after query " + rs.getObject(2));
            System.out.println("gal after query " + rs.getObject(3));
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    CommandLineRunner init(
            JdbcTemplate jdbcTemplate, DataSource ds) {    // DataSourceAutoConfiguration creates jdbcTemplate
        return args -> {

            System.out.println("**********************************************************");

//            jdbcTemplate.execute("CREATE TABLE users (id INT, name VARCHAR(20))");

            Connection connection = null;
            Statement statement = null;

            int result;
            try {
                connection = jdbcTemplate.getDataSource().getConnection();
                getColumns(connection);
//                statement = connection.createStatement();
//                result = statement.executeUpdate("insert into streaming(info) values('lalal') returning into ");

//                prepStatement(connection);

                namedJdbcTemplate(jdbcTemplate);


            } finally {
                JdbcHelper.close(null, statement, connection);
            }

        };
    }

    private void generatedKeys(Connection connection) throws SQLException {
//        PreparedStatement pstm = connection.prepareStatement("insert into streaming(info,until) values(?,?)",
//                new String[]{"id", "info", "geo"});
//        pstm.setString(1, "my val 4");
//        pstm.setDate(2, new Date(Instant.now().toEpochMilli()));
        PreparedStatement pstm = connection.prepareStatement("update streaming " +
                "set geo=?, until=? where id=?",
                new String[]{"id", "info", "geo"});
        pstm.setString(1, "geo15");
        pstm.setDate(2, new java.sql.Date(Instant.now().toEpochMilli()));
        pstm.setLong(3, 190);
        int j = pstm.executeUpdate();
        if (j > 0)
        {
            ResultSet rs = pstm.getGeneratedKeys();
            int size = rs.getMetaData().getColumnCount();
            while(rs.next())
            {
                for (int i = 1; i <= size; i++) {
                    if("TIMESTAMP".equals(rs.getMetaData().getColumnTypeName(i))){
                        System.out.println("gal 55 " + rs.getMetaData().getColumnName(i) + " ; " + rs.getDate(i));
                    } else {
                        System.out.println("gal 55 " + rs.getMetaData().getColumnName(i) + " ; " + rs.getObject(i));
                    }
                }
            }
        }
        pstm.close();
    }

    private void namedJdbcTemplate(JdbcTemplate jdbcTemplate) {
        final NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        final GeneratedKeyHolder holder = new GeneratedKeyHolder();
        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("geo", "geo17");
        paramSource.addValue("until", new java.sql.Date(Instant.now().toEpochMilli()));
        paramSource.addValue("id", 190);
        final int numAffected = template.update("update streaming " +
                "set geo=:geo, until=:until where id=:id", paramSource, holder);
        final Map<String, Object> keys = holder.getKeys();
        System.out.println("keys = " + keys);
    }

    private void prepStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into streaming(info,geo) values(?,?) " +
//                "returning id,info,geo");
                "returning id,info,geo into ?,?,?");
//        Statement ps = connection.createStatement();
//        if (psH instanceof HikariProxyPreparedStatement) {
//
//        }
//        final OraclePreparedStatement ps = psH.unwrap(OraclePreparedStatement.class);
        ps.setString(1, "myvalue 18");
        ps.setString(2, "geo 18");
//        ps.registerReturnParameter(2, Types.NUMERIC);
//        ps.registerReturnParameter(3, Types.VARCHAR);
        final boolean execute = ps.execute();//"insert into streaming(info) values('myvalue 16') returning id,info into ?,? ");
        ps.close();
//        psH.close();
    }

    public void batch () {
        //            jdbcTemplate.batchUpdate("INSERT INTO users (id, name) VALUES (?, ?)",
//                    Arrays.asList(new Object[][] { { 1, "foo" }, { 2, "bar" } }));
//
//            PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
//                    .prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
//            preparedStatement.setString(2, "FOO");
//            preparedStatement.setInt(1, 3);
//            preparedStatement.addBatch();
//            preparedStatement.setInt(1, 4);
//            preparedStatement.setString(2, "BAR");
//            preparedStatement.addBatch();
//            preparedStatement.executeBatch();
//
//            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
//            System.out.println("**********************************************************");
    }

//    public long bytesToLong(byte[] bytes) {
//        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
//        buffer.put(bytes);
//        buffer.flip();//need flip
//        return buffer.getLong();
//    }

    //                Using CallableStatement : begin, end needs
//                final CallableStatement cs = connection.prepareCall("insert into streaming(info) values(?) " +
//                        "returning id,info into ?,?");
//                cs.setString(1, "myvalue2");
//                cs.registerOutParameter(2, Types.NUMERIC);
//                cs.registerOutParameter(3, Types.VARCHAR);
//                cs.execute();
//                System.out.println("gal 44" + cs.getLong(1));
//                System.out.println("gal 44" + cs.getString(2));
//                cs.close();

    public void getColumns(Connection connection) {
        ResultSet rs = null;
        String schema = "JOHNY";
        String table = "STREAMING";
//        List<ColumnInfo> columns = new ArrayList<>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            if (databaseMetaData != null) {
//                int size = primaryKeyInfo.getColumns().size();
//                for (int i = 0; i < size; i++) {
                    rs = databaseMetaData.getColumns(
                            null,
                            schema,
                            table,
                            "UNTIL"
                    );

                    if (rs.next()) {
                        System.out.println("rs = " + rs.getInt(5));//java.sql.Types 93 - timestamp
                    }
                    rs.close();
//                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Problem to get columns for table {" + schema + "}.{" + table + "}", e);
        } finally {
            JdbcHelper.close(rs);
        }
//        return columns;
    }
}
