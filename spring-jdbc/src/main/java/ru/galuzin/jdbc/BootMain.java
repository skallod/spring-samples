package ru.galuzin.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.galuzin.jdbc.entity.collections.*;
import ru.galuzin.jdbc.repository.AvailabilityZoneRepository;
import ru.galuzin.jdbc.repository.DCenterRepository;
import ru.galuzin.jdbc.repository.RegionRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class BootMain {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(BootMain.class, args);
//        region1(regionRepository);
        final RegionRepository region2Repository = context.getBean(RegionRepository.class);
        final AvailabilityZoneRepository availabilityZoneRepository = context.getBean(AvailabilityZoneRepository.class);
        final DCenterRepository dCenterRepository = context.getBean(DCenterRepository.class);
        region2(region2Repository, availabilityZoneRepository, dCenterRepository);
//        final Region region3 = Region.builder()
//            .id(UUID.randomUUID())
//            .availabilityZone(UUID.randomUUID())
//            .baseEntity(
//                BaseEntity.builder()
//                    .description("descr33")
//                    .created(Instant.now())
//                    .build()
//            )
//            .build();
//        final Region region3_0 = regionRepository.save(region3);
//        region1_1.getBaseEntity().setDescription("descr 1_1");
//        region3_0.getBaseEntity().setDescription("descr 3_0");
//        regionRepository.saveAll(List.of(region1_1, region3_0));
    }

//    private static void region1(RegionRepository regionRepository) {
//        final Region region1 = Region.builder()
//            .id(UUID.randomUUID())
//            .availabilityZone(UUID.randomUUID())
//            .baseEntity(
//                BaseEntity.builder()
//                    .description("descr33")
//                    .build()
//            )
//            .build();
//        // это не новый объект, а старый + сетеры
//        final Region region1_0 = regionRepository.save(region1);
//        region1_0.getBaseEntity().setDescription("descr2");
//        final Region region1_1 = regionRepository.save(region1_0);
//        if (region1_0 == region1_1) {
//            log.warn("Same object"); // this
//        } else {
//            log.warn("Diff object");
//        }
//        Region byId = regionRepository.findById(region1_1.getId()).get();
//        if (byId == region1_1) {
//            log.warn("Same object");
//        } else {
//            log.warn("Diff object"); // this
//        }
//    }

    @SneakyThrows
    private static void region2(RegionRepository regionRepository, AvailabilityZoneRepository availabilityZoneRepository,
                                DCenterRepository dCenterRepository) {
        var regionBE = BaseEntity.builder()
            .description("region descr")
            .data(new JsonHolder("{\"region_id\":\"1\"}"))
            .created(Instant.now())
            .edited(Instant.now())
            .creator("sub1")
            .editor("sub1")
            .build();

        final Region region1 = Region.builder()
            .id(UUID.randomUUID())
            .isNew(true)
            .baseEntity(regionBE)
            .build();

        final AvailabilityZone zone = getZone("zone 1");
        final ArrayList<AvailabilityZone> zones = new ArrayList<>();
        zones.add(zone);
        region1.setAvailabilityZones(zones);
        log.info("Region obj {}", region1);
        final Region regionSaved1 = regionRepository.save(region1);
        log.warn("regionSaved1 id {}, {}, {}", regionSaved1.getId(), regionSaved1.getBaseEntity().getCreated(), regionSaved1.getBaseEntity().getEdited());
        Region regionFound1 = regionRepository.findById(regionSaved1.getId()).get();
        log.warn("Region {}", regionFound1);
        AvailabilityZone zoneFound = availabilityZoneRepository.findById(regionFound1.getAvailabilityZones().iterator().next().getId()).get();
        log.warn("Zone found {}", zoneFound);
        zoneFound.getBaseEntity().setDescription("zone found");
        availabilityZoneRepository.save(zoneFound);

        final AvailabilityZone z2 = getZone("zone 2");
        regionFound1.getAvailabilityZones().add(z2);
        final Region regionSaved2 = regionRepository.save(regionFound1);
        log.warn("regionSaved2 {}", regionSaved2);
        final Region regionFound2 = regionRepository.findById(regionSaved2.getId()).get();
        log.warn("regionFound2 {}", regionFound2);

        String name = "dcenter-1";
        final DCenter dCenter = getdCenter(name, z2.getId());
        dCenterRepository.save(dCenter);
        final Region regionFound3 = regionRepository.findById(regionSaved2.getId()).get();
        log.warn("regionFound3 {}", regionFound3);


//        byId.getBaseEntity().setDescription("change from main");
//        Thread.sleep(1_000);
//        final Region save = region2Repository.save(byId);
//        save.getBaseEntity().setDeleted(Instant.now());

//        region2Repository.delete(save);
//        log.warn("Anoj");
    }

    private static DCenter getdCenter(String name, UUID zoneId) {
        var dCenterBE = BaseEntity.builder()
            .name(name)
            .description(name)
            .data(new JsonHolder("{\"dcenter_id\":\""+name+"\"}"))
            .created(Instant.now())
            .edited(Instant.now())
            .creator("sub1")
            .editor("sub1")
            .build();
        return DCenter.builder()
            .id(UUID.randomUUID())
            .isNew(true)
            .availabilityZoneId(zoneId)
            .address("Naberezhnaya-1")
            .coordinates("23,4234221;45,4234221")
            .baseEntity(dCenterBE)
            .build();
    }

    private static AvailabilityZone getZone(String s) {
        final BaseEntity zoneBE = BaseEntity.builder()
            .name(s)
            .description(s + " descr")
            .data(new JsonHolder("{\"zone_id\":\""+ s +"\"}"))
            .created(Instant.now())
            .edited(Instant.now())
            .creator("sub1")
            .editor("sub1")
            .build();
        return AvailabilityZone.builder()
            .id(UUID.randomUUID())
            .baseEntity(zoneBE)
            .isNew(true)// insert
            .build();
    }
}
