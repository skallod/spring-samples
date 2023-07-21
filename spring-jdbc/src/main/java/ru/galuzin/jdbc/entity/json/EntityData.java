package ru.galuzin.jdbc.entity.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(ServiceData.class),
    @JsonSubTypes.Type(KafkaResource.class)
})
public abstract class EntityData {
}
