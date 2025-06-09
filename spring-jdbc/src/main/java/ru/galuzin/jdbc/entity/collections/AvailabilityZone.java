package ru.galuzin.jdbc.entity.collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@Table("availability_zone")
public class AvailabilityZone implements Persistable<UUID> {

    @Id
    UUID id;

//    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
//    private BaseEntity baseEntity;

    @Column("region_id")
    private UUID regionId;

//    @Singular
//    @MappedCollection(idColumn = "availability_zone_id", keyColumn = "name")
//    private List<DCenter> dCenters;

    @Transient
    private boolean isNew;

    @PersistenceConstructor
    private AvailabilityZone(UUID id, /*BaseEntity baseEntity,*/ UUID regionId/*, List<DCenter> dCenters*/) {
        this(id, null, regionId, null, false);
    }

    @Builder
    public AvailabilityZone(UUID id, BaseEntity baseEntity, UUID regionId, List<DCenter> dCenters, boolean isNew) {
        this.id = id;
//        this.baseEntity = baseEntity;
        this.regionId = regionId;
        this.isNew = isNew;
//        this.dCenters = dCenters;
    }
}
