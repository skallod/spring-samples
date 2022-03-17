package ru.pesok.graviy.spring.orm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.pesok.graviy.spring.orm.service.PersonService;
import ru.pesok.graviy.spring.orm.service.PersonServiceNested;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(HikariProperties.class)
public class HiberConf {

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(
                "ru.pesok.graviy.spring.orm.domain");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource(HikariProperties hikariProperties) {
        final Properties hikariProps = new Properties();
        Optional.ofNullable(hikariProperties.getProperties()).ifPresent(hikariProps::putAll);
//        hikariProps.setProperty("connectionTimeout", "60000");
        final HikariConfig hikariConfig = new HikariConfig(hikariProps);
        String url = "jdbc:postgresql://postgres.local:5432/postgres";
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("mysecretpassword");


        final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        return hikariDataSource;
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
////        dataSource.setDriverClassName("org.h2.Driver");
////        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
////        dataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");
//        dataSource.setJdbcUrl("jdbc:postgresql://postgres.local:5432/postgres");
//        dataSource.setUsername("postgres");
////        dataSource.setUsername("johny");
//        dataSource.setPassword("mysecretpassword");//password
////        dataSource.setPassword("1234");
//        dataSource.setAutoCommit(false);
//        dataSource.setMaximumPoolSize(30);
//        dataSource.setMinimumIdle(10);
//        dataSource.setIdleTimeout(600_000);
//        dataSource.setMaxLifetime(600_000);
//        return dataSource;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "none");
//        hibernateProperties.setProperty(
//                "hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.flushMode", "COMMIT");
        //hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", "false");
        hibernateProperties.setProperty("hibernate.connection.release_mode", "after_transaction");
        hibernateProperties.setProperty("hibernate.use_sql_comments", "true");
        return hibernateProperties;
    }

    @Bean
    public PersonService personService(LocalSessionFactoryBean sessionFactory, PersonServiceNested personServiceNested) {
        return new PersonService(personServiceNested, sessionFactory);
    }

    @Bean
    public PersonServiceNested personServiceNested(LocalSessionFactoryBean sessionFactory) {
        return new PersonServiceNested(sessionFactory);
    }
}
