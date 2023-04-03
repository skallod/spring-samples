package ru.galuzin.jdbc.entity.inherit;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter
public class Item extends InstanceBase {

    protected final String name;

    protected final UUID instanceId;

    @PersistenceConstructor
    public Item(UUID id, @NonNull String name, UUID instanceId) {
        super(id);
        this.name = name;
        this.instanceId = instanceId;
    }
}
