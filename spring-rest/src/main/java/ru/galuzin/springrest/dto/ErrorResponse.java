package ru.galuzin.springrest.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class ErrorResponse {
    @NonNull
    private final String exceptionName;

    @NonNull
    private Type type;

    @Singular

    private List<? extends GenericErrorMessage> errors;

    public enum Type {
        VALIDATION,
        GENERAL,
        AUTH,
        INTERNAL
    }
}
