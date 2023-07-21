//package ru.galuzin.jdbc.entity;
//
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.Version;
//import org.springframework.data.relational.core.mapping.Column;
//import org.springframework.data.relational.core.mapping.Embedded;
//import org.springframework.data.relational.core.mapping.Table;
//
//import java.util.UUID;
//
//@Data
//@Table("region")
//public class Region {
//
//    @Id
//    private UUID id;
//    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
//    private BaseEntity baseEntity;
//
//    @Column("availability_zone")
//    private UUID availabilityZone;
//
//    @Version
//    @Setter(AccessLevel.PRIVATE)
//    private Long version;
//
//    @Builder
//    public Region(UUID id, BaseEntity baseEntity, UUID availabilityZone) {
//        this.id = id;
//        this.baseEntity = baseEntity;
//        this.availabilityZone = availabilityZone;
//    }
//}
