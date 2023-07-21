package ru.galuzin.jdbc.entity.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@AllArgsConstructor
@Getter
@Jacksonized
public class ServiceData extends EntityData {
    private final int count;
    private final String dbUrl;
    private final TransitSettings transitSettings;
}
