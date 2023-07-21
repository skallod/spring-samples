package ru.galuzin.jdbc.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.galuzin.jdbc.entity.collections.AvailabilityZone;
import ru.galuzin.jdbc.entity.collections.BaseEntity;
import ru.galuzin.jdbc.entity.collections.JsonHolder;
import ru.galuzin.jdbc.entity.collections.Region;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class RegionRepositoryTest extends AbstractDaoTest {

    @Autowired
    RegionRepository regionRepository;


    @Test
    public void test1() {
        final AvailabilityZone az1 = AvailabilityZone.builder()
            .isNew(true)
            .id(UUID.randomUUID())
            .baseEntity(
                BaseEntity.builder()
                    .created(Instant.now())
                    .edited(Instant.now())
                    .description("av z 1")
                    .name("az1")
                    .data(new JsonHolder("{\"key1\":\"value1\"}"))
                    .build()
            )
            .build();
        final Region region1 = Region.builder()
            .id(UUID.randomUUID())
            .isNew(true)
            .availabilityZones(List.of(az1))
            .baseEntity(
                BaseEntity.builder()
                    .created(Instant.now())
                    .edited(Instant.now())
                    .description("descr1")
                    .name("n1")
                    .data(new JsonHolder("{\"key1\":\"value1\"}"))
                    .build()
            )
            .build();
        final Region save = regionRepository.save(region1);
        final Region regionFound = regionRepository.findById(save.getId()).get();
        Assert.assertNotNull(regionFound);
    }

}
