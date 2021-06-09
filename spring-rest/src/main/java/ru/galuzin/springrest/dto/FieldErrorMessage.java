package ru.galuzin.springrest.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
//@ApiModel(parent = GenericErrorMessage.class)
public class FieldErrorMessage extends GenericErrorMessage {
    private String fieldName;
    private String violatedValue;

    @Builder
    public FieldErrorMessage(String message, String fieldName, String violatedValue) {
        super(message);
        this.fieldName = fieldName;
        this.violatedValue = violatedValue;
    }
}
