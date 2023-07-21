package ru.galuzin.jdbc.entity.json;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Value
@Builder
@Table("services")
public class Service {
    @Id
    UUID id;
    public static Class<ServiceData> getDataType() {
        return ServiceData.class;
    }
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    CommonInfo<ServiceData> common;

    String name;
}

