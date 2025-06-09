package ru.galuzin.jdbc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class Config {

//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

//    @Bean
//    public DataSource dataSource() {
//        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setUrl("jdbc:postgresql://localhost:25432/demo");
//        dataSource.setUser("postgres");
//        dataSource.setPassword("mysecretpassword");
//        return dataSource;
//    }

    /**
     * Заполняет общие поля для сущностей
     */
//    @Bean
//    BeforeConvertCallback<UuidGenerated> beforeConvertCallback() {
//        return (entity) -> {
//            log.info("BeforeConvertCallback start");
//            BaseEntityContainable baseEntityContainable = null;
//            if (entity instanceof BaseEntityContainable) {
//                baseEntityContainable = (BaseEntityContainable) entity;
//            }
//            if (entity.getId() == null) {
//                log.info("BeforeConvertCallback Insert prepare" );
//                entity.setId(UUID.randomUUID());
//                if (baseEntityContainable != null) {
//                    baseEntityContainable.setBaseEntity(
//                        BaseEntity.builder()
//                            .created(Instant.now())
//                            //todo modified etc
//                            .description("inserted")
//                            .build()
//                    );
//                }
//            } else {
//                log.info("BeforeConvertCallback Update prepare");
//
//                //todo modified
//            }
//            return entity;
//        };
//    }
}
