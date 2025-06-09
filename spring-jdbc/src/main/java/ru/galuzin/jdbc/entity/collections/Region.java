package ru.galuzin.jdbc.entity.collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Data
@Table("region")
public class Region implements Persistable<UUID> {

    @Id
    private UUID id;

//    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
//    private BaseEntity baseEntity;

    @Singular
    @MappedCollection(idColumn = "region_id", keyColumn = "name")
    private List<AvailabilityZone> availabilityZones;

    @Transient
    private boolean isNew;

    @PersistenceConstructor
    private Region(UUID id, /*BaseEntity baseEntity,*/ List<AvailabilityZone> availabilityZones) {
        this.id = id;
//        this.baseEntity = baseEntity;
        this.availabilityZones = availabilityZones;
        this.isNew = false;
    }

    @Builder
    public Region(UUID id, BaseEntity baseEntity, List<AvailabilityZone> availabilityZones, boolean isNew) {
        this.id = id;
//        this.baseEntity = baseEntity;
        this.availabilityZones = availabilityZones;
        this.isNew = isNew;
    }

    //    @Builder
//    public Region2(BaseEntity baseEntity, UUID availabilityZone) {
//        this.baseEntity = baseEntity;
//        this.availabilityZone = availabilityZone;
//    }
}
