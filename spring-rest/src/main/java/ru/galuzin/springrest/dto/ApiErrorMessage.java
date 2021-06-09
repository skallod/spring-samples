package ru.galuzin.springrest.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
//@ApiModel(parent = GenericErrorMessage.class)
public class ApiErrorMessage extends GenericErrorMessage {
    final String uri;
    final String statusText;
    final int statusCode;
    final String errorBodyText;
    final JsonNode errorBodyObject;

    @Builder
    public ApiErrorMessage(
            final String message,
            final String uri,
            final String statusText,
            final HttpStatus statusCode,
            final String errorBodyText,
            final JsonNode errorBodyObject) {
        super(message);
        this.uri = uri;
        this.statusText = statusText;
        this.statusCode = statusCode.value();
        this.errorBodyText = errorBodyText;
        this.errorBodyObject = errorBodyObject;
    }
}
