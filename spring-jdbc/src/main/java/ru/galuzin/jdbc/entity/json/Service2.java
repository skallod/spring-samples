package ru.galuzin.jdbc.entity.json;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("services")
@Value
@Builder
public class Service2 {

    @Id
    UUID id;

    String name;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    CommonInfo<KafkaResource> common;
}
