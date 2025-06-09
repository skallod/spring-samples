package ru.galuzin.springrest.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@ApiModel(parent = GenericErrorMessage.class)
public class FieldErrorMessage extends GenericErrorMessage {
    private String fieldName;
    private String violatedValue;

}
