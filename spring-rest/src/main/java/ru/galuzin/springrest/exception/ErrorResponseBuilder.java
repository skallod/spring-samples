package ru.galuzin.springrest.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import ru.galuzin.springrest.dto.ApiErrorMessage;
import ru.galuzin.springrest.dto.ErrorMessage;
import ru.galuzin.springrest.dto.ErrorResponse;

public class ErrorResponseBuilder {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static ErrorResponse of(Throwable ex) {
        return ErrorResponse.builder()
                .exceptionName(ex.getClass().getName())
                .type(ErrorResponse.Type.INTERNAL)
                .error(new ErrorMessage(ex.getLocalizedMessage()))
                .build();
    }

    @SneakyThrows
    public static ErrorResponse of(NotFoundException ex) {
        return ErrorResponse.builder()
                .exceptionName(ex.getClass().getName())
                .type(ErrorResponse.Type.GENERAL)
                .error(
                        ApiErrorMessage.builder().
                                message("message1").
                                uri("uri-localhost").
                                statusText("status-text").
                                statusCode(HttpStatus.OK).
                                errorBodyText("errorBody").
                                errorBodyObject(mapper.readTree(new byte[0])).build()
                ).build();
    }
}
