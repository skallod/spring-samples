package ru.galuzin.jdbc.entity.collections;

import java.util.UUID;

/**
 * В сущности есть id типа UUID.
 * Заполняется автоматически во время операции insert {@link ru.galuzin.jdbc.component.UuidGeneratedBeforeConvertCallback}.
 */
public interface UuidGenerated {
    void setId(UUID id);
    UUID getId();
}
