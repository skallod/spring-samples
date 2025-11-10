package ru.galuzin.jdbc.repository;

import org.junit.runner.RunWith;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.galuzin.jdbc.config.JdbcCustomConverter;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    AbstractDaoTest.SpringConfiguration.class
})
//@Transactional
@EnableAutoConfiguration
//@EnableTransactionManagement
public abstract class AbstractDaoTest {

    @Configuration
    @Import(JdbcCustomConverter.class)
    @ComponentScan(
        basePackageClasses = {InstancePlainRepository.class}

    )

    public static class SpringConfiguration {

        @Bean
        public JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(dataSource());
        }

        @Bean
        public DataSource dataSource() {
            final PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl("jdbc:postgresql://localhost:5432/test?currentSchema=springjdbc");
            dataSource.setUser("postgres");
            dataSource.setPassword("mysecretpassword");
            return dataSource;
        }
    }
}
