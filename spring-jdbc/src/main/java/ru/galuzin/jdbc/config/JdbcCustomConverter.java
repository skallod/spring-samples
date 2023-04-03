package ru.galuzin.jdbc.config;

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
import ru.galuzin.jdbc.entity.JsonHolder;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JdbcCustomConverter extends AbstractJdbcConfiguration {

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {

        return new JdbcCustomConversions(Arrays.asList(new UserIdReadConverter(), new JsonHolderWriteConverter()));
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
}
