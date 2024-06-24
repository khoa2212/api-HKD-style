package com.example.product_service.exception;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorBody {
    private String message;
    private String code;
}
