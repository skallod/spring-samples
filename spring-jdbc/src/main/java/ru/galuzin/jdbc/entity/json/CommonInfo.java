package ru.galuzin.jdbc.entity.json;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.relational.core.mapping.Column;

@Value
@Builder
public class CommonInfo<T extends EntityData> {
    @Column("data")
    T entityData;

    TypeReference<T> getDataType() {
        return new TypeReference<T>() {};
    }
}
