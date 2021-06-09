package ru.galuzin.springrest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(ApiErrorMessage.class),
        @JsonSubTypes.Type(ErrorMessage.class),
        @JsonSubTypes.Type(FieldErrorMessage.class)
})
//@ApiModel(discriminator = "type", subTypes = {ApiErrorMessage.class, ErrorMessage.class, FieldErrorMessage.class})
abstract class GenericErrorMessage {
    private String message;
//    private String type = this.getClass().getSimpleName();

//    public GenericErrorMessage(String message) {
//        this.message = message;
//    }
}
