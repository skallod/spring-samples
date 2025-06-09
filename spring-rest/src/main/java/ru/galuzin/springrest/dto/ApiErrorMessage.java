package ru.galuzin.springrest.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@ApiModel(parent = GenericErrorMessage.class)
public class ApiErrorMessage extends GenericErrorMessage {
    private String uri;
    private String statusText;
    private int statusCode;
    private String errorBodyText;
    private JsonNode errorBodyObject;

}
