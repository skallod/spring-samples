package ru.galuzin.jdbc.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.galuzin.jdbc.entity.inherit.InstanceFull;
import ru.galuzin.jdbc.entity.inherit.InstancePlain;
import ru.galuzin.jdbc.entity.inherit.Item;

import java.util.Optional;
import java.util.UUID;

public class InstanceRepositoryTest extends AbstractDaoTest {

    @Autowired
    InstancePlainRepository instancePlainRepository;

    @Autowired
    InstanceFullRepository instanceFullRepository;

    /**
     * снчала плейн затем full
     * тест в обратном порядке
     */
    @Test
    public void test1() {
        final InstancePlain instance1 = InstancePlain.builder()
            .name("instance1")
            .build();
        final InstancePlain save1 = instancePlainRepository.save(instance1);
        final Optional<InstancePlain> load1 = instancePlainRepository.findById(save1.getId());
        System.out.println("load1 = " + load1);
        final Item item1 = Item.builder()
            .name("item1")
            .instanceId(load1.get().getId())
            .build();
        final Optional<InstanceFull> loadFull = instanceFullRepository.findById(save1.getId());
        loadFull.get().getItems().add(item1);
        instanceFullRepository.save(loadFull.get());
        final Optional<InstanceFull> loadFull2 = instanceFullRepository.findById(save1.getId());
        System.out.println("loadFull2 = " + loadFull2);
//        instancePlainRepository.delete(save1);//не может удалить, т.к. item все еще ссылается на него
        System.out.println("finish");
    }

    @Test
    public void test2() {
        final UUID uuid = UUID.randomUUID();
        final InstanceFull instanceFull = InstanceFull.builder()
            .item(
                Item.builder()
                    .name("item1")
//                    .instanceId(uuid)
                    .build()
            )
            .name("instance-2")
//            .id(uuid)
//            .isNew(true)
            .build();
        final InstanceFull save = instanceFullRepository.save(instanceFull);
        final Optional<InstanceFull> byId = instanceFullRepository.findById(save.getId());
        System.out.println("byId = " + byId);
    }

    @Test
    public void test3() {
        boolean notEmpty = instanceFullRepository.isNotEmpty();
        System.out.println("notEmpty = " + notEmpty);
        Assert.assertTrue(notEmpty);
    }
}
