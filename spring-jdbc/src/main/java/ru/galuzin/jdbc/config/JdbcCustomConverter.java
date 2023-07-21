package ru.galuzin.jdbc.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import ru.galuzin.jdbc.entity.collections.JsonHolder;
import ru.galuzin.jdbc.entity.json.EntityData;
import ru.galuzin.jdbc.entity.json.Service;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JdbcCustomConverter extends AbstractJdbcConfiguration {

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        final ObjectMapper objectMapper = new ObjectMapper();
//        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
//            .allowIfSubType("ru.galuzin.jdbc.entity.json.EntityData")
//            .build();
//        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        return new JdbcCustomConversions(
            Arrays.asList(
                new UserIdReadConverter(),
                new JsonHolderWriteConverter(),
                new EntityDataReadConverter(objectMapper),
                new EntityDataWriteConverter(objectMapper)
                ));
    }

    @ReadingConverter
    static class UserIdReadConverter implements Converter<PGobject, JsonHolder> {

        @Override
        public JsonHolder convert(PGobject pGobject) {
            log.info("Pgobject to json");
            return new JsonHolder(pGobject.getValue());
        }

    }
    @WritingConverter
    static class JsonHolderWriteConverter implements Converter<JsonHolder, PGobject> {

        @Override
        @SneakyThrows
        public PGobject convert(JsonHolder source) {
            log.info("Json to pgobject");
            var pGobject = new PGobject();
            pGobject.setType("json");
            pGobject.setValue(source.getData());
            return pGobject;
        }
    }


    @RequiredArgsConstructor
    @ReadingConverter
    static class EntityDataReadConverter implements Converter<PGobject, EntityData> {

        private final ObjectMapper objectMapper;

//        private final TypeReference<> reference = new TypeReference<>();

        @Override
        @SneakyThrows
        public EntityData convert(PGobject pGobject) {
            log.info("Pgobject to json");
            return objectMapper.readValue(pGobject.getValue(), Service.getDataType());
        }
    }

    @RequiredArgsConstructor
    @WritingConverter
    static class EntityDataWriteConverter implements Converter<EntityData, PGobject> {

        private final ObjectMapper objectMapper;

        @Override
        public PGobject convert(EntityData source) {
            try {
                log.info("Json to pgobject");
                var pGobject = new PGobject();
                pGobject.setType("json");
                final String s = objectMapper.writeValueAsString(source);
                pGobject.setValue(s);
                return pGobject;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
