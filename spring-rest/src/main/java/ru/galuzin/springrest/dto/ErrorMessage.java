package ru.galuzin.springrest.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
//@ApiModel(parent = GenericErrorMessage.class)
public class ErrorMessage extends GenericErrorMessage {
    public ErrorMessage(String message) {
        super(message);
    }
}
