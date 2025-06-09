package ru.galuzin.jdbc.entity.collections;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("dcenter")
public class DCenter  implements Persistable<UUID> {

    @Id
    UUID id;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private BaseEntity baseEntity;


    @Column("availability_zone_id")
    private UUID availabilityZoneId;

    private String address;

    @Transient
    private boolean isNew;

    @PersistenceConstructor
    private DCenter(UUID id, BaseEntity baseEntity, UUID availabilityZoneId, String address) {
        this(id, baseEntity, availabilityZoneId, address, false);
    }

    @Builder
    public DCenter(UUID id, BaseEntity baseEntity, UUID availabilityZoneId, String address, boolean isNew) {
        this.id = id;
        this.baseEntity = baseEntity;
        this.availabilityZoneId = availabilityZoneId;
        this.address = address;
        this.isNew = isNew;
    }
}
