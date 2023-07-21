package ru.galuzin.jdbc.entity.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

/**
 * Вынесены общие поля для сущностей.
 * Некоторые поля могут заполняться автоматически во время insert/update операций {@link ru.galuzin.jdbc.component.UuidGeneratedBeforeConvertCallback}.
 */
@Data
@AllArgsConstructor
@Builder
public class BaseEntity {
    private Instant created;

    private Instant edited;

    private String creator;

    private String editor;

    private Instant deleted;

    private String code;

    private String name;

    private String description;

    @Column("data")
    private JsonHolder data;

}
