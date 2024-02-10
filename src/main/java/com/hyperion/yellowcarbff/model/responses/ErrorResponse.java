package com.hyperion.yellowcarbff.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private Integer status;

    private String message;

    private String cause;

}
