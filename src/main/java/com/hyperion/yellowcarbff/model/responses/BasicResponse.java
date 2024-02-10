package com.hyperion.yellowcarbff.model.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicResponse {

    private Integer status;

    private String message;

}
