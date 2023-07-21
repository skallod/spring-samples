//package ru.galuzin.jdbc.component;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
//import org.springframework.stereotype.Component;
//import ru.galuzin.jdbc.entity.BaseEntity;
//import ru.galuzin.jdbc.entity.BaseEntityEmbedded;
//import ru.galuzin.jdbc.entity.UuidGenerated;
//
//import java.time.Instant;
//import java.util.UUID;
//
///**
// * Автоматическое заполнение служебных полей.
// */
//@Component
//@Slf4j
//public class UuidGeneratedBeforeConvertCallback implements BeforeConvertCallback<UuidGenerated> {
//    @Override
//    public UuidGenerated onBeforeConvert(UuidGenerated entity) {
//        log.info("BeforeConvertCallback start {}", entity.getClass());
//        BaseEntityEmbedded baseEntityEmbedded = null;
//        if (entity instanceof BaseEntityEmbedded) {
//            baseEntityEmbedded = (BaseEntityEmbedded) entity;
//        }
//        if (entity.getId() == null) {
//            log.info("BeforeConvertCallback Insert prepare");
//            insertPrepare(entity, baseEntityEmbedded);
//        } else {
//            log.info("BeforeConvertCallback Update prepare");
//            updatePrepare(baseEntityEmbedded);
//        }
//        return entity;
//    }
//
//    private void insertPrepare(UuidGenerated entity, BaseEntityEmbedded baseEntityEmbedded) {
//        entity.setId(UUID.randomUUID());
//        if (baseEntityEmbedded == null) {
//            return;
//        }
//        BaseEntity baseEntity;
//        if (baseEntityEmbedded.getBaseEntity() == null) {
//            baseEntity = new BaseEntity();
//        } else {
//            baseEntity = baseEntityEmbedded.getBaseEntity();
//        }
//        baseEntity.setCreated(Instant.now());
//        baseEntity.setEdited(Instant.now());
//        //todo setCreator, setEditor
//        baseEntityEmbedded.setBaseEntity(baseEntity);
//    }
//
//    private void updatePrepare(BaseEntityEmbedded baseEntityEmbedded) {
//        if (baseEntityEmbedded == null) {
//            return;
//        }
//        final BaseEntity baseEntity = baseEntityEmbedded.getBaseEntity();
//        baseEntity.setEdited(Instant.now());
////      todo  baseEntity.setEditor();
//    }
//}
//
