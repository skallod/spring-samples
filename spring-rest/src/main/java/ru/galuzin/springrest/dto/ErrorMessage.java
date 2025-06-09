package ru.galuzin.springrest.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
//@ApiModel(parent = GenericErrorMessage.class)
public class ErrorMessage extends GenericErrorMessage {
}
