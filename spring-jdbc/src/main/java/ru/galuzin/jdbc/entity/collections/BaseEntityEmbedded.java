package ru.galuzin.jdbc.entity.collections;

/**
 * В сущность встроено поле BaseEntity baseEntity.
 */
public interface BaseEntityEmbedded {
    BaseEntity getBaseEntity();

    void setBaseEntity(BaseEntity baseEntity);
}
