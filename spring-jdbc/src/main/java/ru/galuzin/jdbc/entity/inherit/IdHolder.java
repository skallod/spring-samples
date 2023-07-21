package ru.galuzin.jdbc.entity.inherit;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

//@Data
@EqualsAndHashCode
//@Builder
//@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Getter
public class IdHolder implements Persistable<UUID> {
    @Id
    protected final UUID id;

    @Transient
    protected boolean isNew;

    @PersistenceConstructor
    public IdHolder(UUID id) {
        this.id = id;
    }
    
    @Override
    public boolean isNew() {
        return id == null || isNew;
    }
}
