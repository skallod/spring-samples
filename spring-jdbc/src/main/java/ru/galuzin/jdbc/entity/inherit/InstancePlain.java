package ru.galuzin.jdbc.entity.inherit;


import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("instance")
@Getter
@SuperBuilder(toBuilder = true)
public class InstancePlain extends IdHolder {

    protected final String name;

//    @Builder
    @PersistenceConstructor
    public InstancePlain(UUID id, @NonNull String name) {
        super(id);
        this.name = name;
    }
}
