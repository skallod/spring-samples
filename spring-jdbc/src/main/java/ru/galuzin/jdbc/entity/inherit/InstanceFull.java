package ru.galuzin.jdbc.entity.inherit;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Table("instance")
@Getter
@SuperBuilder(/*builderMethodName = "InstanceFullBuilder",*/ toBuilder = true)
public class InstanceFull extends InstancePlain {

    @Singular
    @MappedCollection(idColumn = "id")
    private final Set<Item> items;

    @PersistenceConstructor
    public InstanceFull(UUID id, @Singular Set<Item> items, @NonNull String name) {
        super(id, name);
        this.items = items;
    }
}
